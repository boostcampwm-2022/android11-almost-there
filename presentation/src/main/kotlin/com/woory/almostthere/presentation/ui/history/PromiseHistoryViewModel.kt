package com.woory.almostthere.presentation.ui.history


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.data.repository.UserRepository
import com.woory.almostthere.presentation.di.PresentationModule
import com.woory.almostthere.presentation.model.PromiseHistory
import com.woory.almostthere.presentation.model.UiState
import com.woory.almostthere.presentation.model.exception.AlmostThereException
import com.woory.almostthere.presentation.model.mapper.history.asUiModel
import com.woory.almostthere.presentation.util.flow.MutableEventFlow
import com.woory.almostthere.presentation.util.flow.asEventFlow
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
    @PresentationModule.HistoryType private val promiseHistoryType: PromiseHistoryType,
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository
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

    private val _uiState = MutableEventFlow<UiState<List<PromiseHistory>?>>()
    val uiState = _uiState.asEventFlow()

    val userPreferences = userRepository.userPreferences

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
                            userPreferences.collectLatest { user ->
                                val result =
                                    list.map { it.asUiModel(user.userID) }
                                        .sortedBy { it.promise.data.gameDateTime }
                                _uiState.emit(UiState.Success(result))
                            }
                        }
                    }
            }
        }
    }
}