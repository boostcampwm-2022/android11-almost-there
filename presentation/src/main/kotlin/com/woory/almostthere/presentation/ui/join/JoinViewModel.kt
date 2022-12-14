package com.woory.almostthere.presentation.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.presentation.model.UiState
import com.woory.almostthere.presentation.model.exception.AlmostThereException
import com.woory.almostthere.presentation.util.InviteCodeUtil
import com.woory.almostthere.presentation.util.flow.EventFlow
import com.woory.almostthere.presentation.util.flow.MutableEventFlow
import com.woory.almostthere.presentation.util.flow.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    val code: MutableStateFlow<String> = MutableStateFlow("")

    private val _codeState: MutableStateFlow<FormState> = MutableStateFlow(FormState.EMPTY)
    val codeState: StateFlow<FormState> = _codeState.asStateFlow()

    private val _uiState: MutableEventFlow<UiState<String>> = MutableEventFlow()
    val uiState: EventFlow<UiState<String>> = _uiState.asEventFlow()

    fun checkInviteCodeValidation() {
        _codeState.value = InviteCodeUtil.getCodeState(code.value)
    }

    fun fetchPromiseByCode() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)

            promiseRepository.getPromiseByCode(code.value.uppercase())
                .onSuccess { promise ->
                    _uiState.emit(
                        UiState.Success(promise.code)
                    )
                }
                .onFailure { _ ->
                    _uiState.emit(
                        UiState.Error(AlmostThereException.FetchFailedException())
                    )
                }
        }
    }
}