package com.woory.almostthere.presentation.ui.creatingpromise.locationsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.presentation.model.mapper.searchlocation.SearchResultMapper
import com.woory.almostthere.presentation.ui.creatingpromise.CreatingPromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchResultViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
) : ViewModel() {

    private val _locationSearchResult: MutableStateFlow<List<LocationSearchResult>> =
        MutableStateFlow(
            listOf()
        )

    val locationSearchResult: StateFlow<List<LocationSearchResult>> =
        _locationSearchResult.asStateFlow()

    private val _uiState: MutableStateFlow<CreatingPromiseUiState> =
        MutableStateFlow(CreatingPromiseUiState.Success)
    val uiState: StateFlow<CreatingPromiseUiState> = _uiState.asStateFlow()

    private val _errorEvent: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    fun searchLocation(query: String) {
        viewModelScope.launch {
            setStateLoading()
            promiseRepository.getSearchedLocationByKeyword(query).onSuccess {
                setStateSuccess()
                setSearchedResult(it.map { SearchResultMapper.asUiModel(it) })
            }.onFailure {
                setStateError(it)
            }
        }
    }

    private fun setStateLoading() {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Loading)
        }
    }

    private fun setStateSuccess() {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Success)
        }
    }

    private fun setStateError(throwable: Throwable) {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Success)
            _errorEvent.emit(throwable)
        }
    }

    fun setSearchedResult(lst: List<LocationSearchResult>) {
        viewModelScope.launch {
            _locationSearchResult.emit(lst)
        }
    }
}