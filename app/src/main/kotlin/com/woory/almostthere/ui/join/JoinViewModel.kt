package com.woory.almostthere.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.util.InviteCodeUtil.isValidInviteCode
import com.woory.data.model.Promise
import com.woory.data.repository.PromiseRepository
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

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorType: MutableStateFlow<CodeState?> = MutableStateFlow(null)
    val errorType: StateFlow<CodeState?> = _errorType.asStateFlow()

    private val _promise: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promise: StateFlow<Promise?> = _promise.asStateFlow()

    fun checkInviteCodeValidation() {
        _codeState.value = if (code.value.isValidInviteCode()) {
            FormState.Valid()
        } else {
            FormState.Invalid()
        }
    }

    fun fetchPromise() {
        viewModelScope.launch {
            _isLoading.value = true

            promiseRepository.fetchPromise(code.value)
                .onSuccess { promise ->
                    _isLoading.value = false
                    _promise.value = promise
                }
                .onFailure { error ->
                    _isLoading.value = false
                    _errorType.value = when (error) {
                        is NoSuchElementException -> CodeState.NONEXISTENT
                        else -> null
                    }
                }
        }
    }
}