package com.woory.presentation.ui.history


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.di.PromiseHistoryModule
import com.woory.presentation.model.Promise
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.mapper.promise.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class PromiseHistoryViewModel @Inject constructor(
    @PromiseHistoryModule.HistoryType private val promiseHistoryType: PromiseHistoryType,
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _promiseList: MutableStateFlow<List<Promise>?> = MutableStateFlow(null)
    val promiseList: StateFlow<List<Promise>?> = _promiseList.asStateFlow()

    init {
        observePromiseList()
    }

    private fun observePromiseList() {
        viewModelScope.launch {
            _isLoading.value = true

            promiseRepository.getJoinedPromiseList()
                .onSuccess { promises ->
                    _promiseList.value =
                        promises.filter { promise ->
                            if (promiseHistoryType == PromiseHistoryType.PAST) {
                                OffsetDateTime.now().isAfter(promise.endTime)
                            } else {
                                OffsetDateTime.now().isBefore(promise.endTime)
                            }
                        }.map { promise ->
                            viewModelScope.async {
                                promiseRepository.getPromiseByCode(promise.promiseCode).getOrThrow()
                                    .asUiModel()
                            }
                        }.awaitAll().toList().sortedBy { promise ->
                            promise.data.gameDateTime
                        }

                    _isLoading.value = false
                    _isError.value = false
                }.onFailure {
                    _isLoading.value = false
                    _isError.value = true
                }

//                .map { list ->
//                    list.filter { promise ->
//                        if (promiseHistoryType == PromiseHistoryType.PAST) {
//                            OffsetDateTime.now().isAfter(promise.endTime)
//                        } else {
//                            OffsetDateTime.now().isBefore(promise.endTime)
//                        }
//                    }
//                }
//                .onStart {
//                    _isLoading.value = true
//                }
//                .onEach { list ->
//                    val promises = mutableListOf<Promise>()
//
//                    list.onEach { promise ->
//                        promiseRepository.getPromiseByCodeAndListen(promise.promiseCode)
//                            .collect { result ->
//                                result.onSuccess {
//                                    promises.add(PromiseMapper.asUiModel(it))
//                                }
//                            }
//                    }
//
//                    _promiseList.value = promises
//                }
//                .catch {
//                    _isError.value = true
//                }
//                .onCompletion {
//                    _isLoading.value = false
//                }
        }
    }
}