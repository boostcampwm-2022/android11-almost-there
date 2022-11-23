package com.woory.presentation.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.PromiseDataModel
import com.woory.presentation.model.mapper.asDomain
import com.woory.presentation.util.InviteCodeUtil.isValidInviteCode
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

    private val _promise: MutableStateFlow<PromiseDataModel?> = MutableStateFlow(null)
    val promise: StateFlow<PromiseDataModel?> = _promise.asStateFlow()

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

            promiseRepository.getPromiseByCode(code.value.uppercase())
                .onSuccess { promise ->
                    _isLoading.value = false
                    _promise.value = promise.asDomain()
                }
                .onFailure { error ->
                    _isLoading.value = false
                    _errorType.value = when (error) {
                        is IllegalStateException -> CodeState.NONEXISTENT
                        else -> null
                    }
                }
        }
    }
}