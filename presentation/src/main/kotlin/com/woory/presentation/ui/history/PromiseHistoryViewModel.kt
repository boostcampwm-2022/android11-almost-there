package com.woory.presentation.ui.history


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.di.PromiseHistoryModule
import com.woory.presentation.model.Promise
import com.woory.presentation.model.UiState
import com.woory.presentation.model.exception.AlmostThereException
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.util.flow.MutableEventFlow
import com.woory.presentation.util.flow.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class PromiseHistoryViewModel @Inject constructor(
    @PromiseHistoryModule.HistoryType private val promiseHistoryType: PromiseHistoryType,
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val joinedPromiseCodes: Flow<List<String>> = promiseRepository.getJoinedPromises()
        .map { promises ->
            promises.filter { promise ->
                if (promiseHistoryType == PromiseHistoryType.PAST) {
                    OffsetDateTime.now().isAfter(promise.endTime)
                } else {
                    OffsetDateTime.now().isBefore(promise.endTime)
                }
            }.map {
                it.promiseCode
            }
        }

    private val _uiState = MutableEventFlow<UiState<List<Promise>?>>()
    val uiState = _uiState.asEventFlow()

    init {
        observePromises()
    }

    private fun observePromises() {
        viewModelScope.launch {
            joinedPromiseCodes.collectLatest { codes ->
                promiseRepository.getPromisesByCodes(codes)
                    .onStart {
                        _uiState.emit(UiState.Loading)
                    }
                    .catch {
                        _uiState.emit(UiState.Error(AlmostThereException.FetchFailedException()))
                    }
                    .collectLatest { list ->
                        if (list.isNullOrEmpty()) {
                            _uiState.emit(UiState.Error(AlmostThereException.FetchFailedException()))
                        } else {
                            val result =
                                list.map { it.asUiModel() }.sortedBy { it.data.gameDateTime }
                            _uiState.emit(UiState.Success(result))
                        }
                    }
            }
        }
    }
}