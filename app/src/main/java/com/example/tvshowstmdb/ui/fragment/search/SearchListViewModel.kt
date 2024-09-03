package com.example.tvshowstmdb.ui.fragment.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshowstmdb.data.model.popular.PopularTvModel
import com.example.tvshowstmdb.repositorio.TvShowRespository
import com.example.tvshowstmdb.resourcestate.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchListViewModel @Inject constructor(
    private val repository: TvShowRespository
) : ViewModel() {

    private val _list = MutableStateFlow<ResourceState<PopularTvModel>>(ResourceState.Loading())
    val list: StateFlow<ResourceState<PopularTvModel>> = _list


    init {
        fetchPopularTvs()
    }

    private fun fetchPopularTvs() = viewModelScope.launch {
        safeFetch()
    }

    fun searchTvShows(query: String) {
        if (query.isBlank()) {
            fetchPopularTvs()
            return
        }

        viewModelScope.launch {
            _list.value = ResourceState.Loading()
            try {
                val response = repository.searchTvShows(query)
                _list.value = handleResponse(response)
            } catch (e: Exception) {
                _list.value = ResourceState.Error(e.message)
            }
        }
    }

    private suspend fun safeFetch() {
        try {
            val response = repository.getPopularTvs()
            Log.d("ViewModel", "Dados recebidos: ${response.body()?.results?.size} itens")
            _list.value = handleResponse(response)
        } catch (t: Throwable) {
            Log.e("ViewModel", "Erro ao buscar dados: ${t.message}", t)
            _list.value = ResourceState.Error(t.message)
        }
    }

    private fun handleResponse(response: Response<PopularTvModel>): ResourceState<PopularTvModel> {
        return if (response.isSuccessful) {
            response.body()?.let { values ->
                ResourceState.Success(values)
            } ?: ResourceState.Error("Resposta vazia")
        } else {
            ResourceState.Error(response.message())
        }
    }
}