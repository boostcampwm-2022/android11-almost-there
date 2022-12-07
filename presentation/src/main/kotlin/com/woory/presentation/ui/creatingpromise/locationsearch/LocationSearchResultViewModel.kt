package com.woory.presentation.ui.creatingpromise.locationsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Location
import com.woory.presentation.model.mapper.searchlocation.SearchResultMapper
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

    private val _uiState: MutableStateFlow<LocationSearchUiState> =
        MutableStateFlow(LocationSearchUiState.Success)
    val uiState: StateFlow<LocationSearchUiState> = _uiState.asStateFlow()

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
            _uiState.emit(LocationSearchUiState.Loading)
        }
    }

    private fun setStateSuccess() {
        viewModelScope.launch {
            _uiState.emit(LocationSearchUiState.Success)
        }
    }

    private fun setStateError(throwable: Throwable) {
        viewModelScope.launch {
            _uiState.emit(LocationSearchUiState.Success)
            _errorEvent.emit(throwable)
        }
    }

    fun setSearchedResult(lst: List<LocationSearchResult>) {
        viewModelScope.launch {
            _locationSearchResult.emit(lst)
        }
    }
}