package com.example.tvshowstmdb.ui.fragment.detalhes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshowstmdb.data.model.details.DetalhesTvModel
import com.example.tvshowstmdb.data.model.keywords.keywordsModel
import com.example.tvshowstmdb.data.model.temporadas.DetalhesTemporadasModel
import com.example.tvshowstmdb.repositorio.TvShowRespository
import com.example.tvshowstmdb.resourcestate.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class DetalhesViewModel @Inject constructor(
    private val repository: TvShowRespository
) : ViewModel() {

    // StateFlow para Detalhes da Série
    private val _details = MutableStateFlow<ResourceState<DetalhesTvModel>>(ResourceState.Loading())
    val details: StateFlow<ResourceState<DetalhesTvModel>> = _details

    // StateFlow para Detalhes da Temporada
    private val _detalhesTemporada = MutableStateFlow<ResourceState<DetalhesTemporadasModel>>(ResourceState.Loading())
    val detalhesTemporada: StateFlow<ResourceState<DetalhesTemporadasModel>> = _detalhesTemporada

    //stateFlow para keywords
    private val _keyWords = MutableStateFlow<ResourceState<keywordsModel>>(ResourceState.Loading())
    val keyWords: StateFlow<ResourceState<keywordsModel>> = _keyWords

    //funcao para buscar as keywords
    fun fetchKeyWords(seriesId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getKeywords(seriesId)
                _keyWords.value = handleResponseKeyWords(response)
            } catch (t: Throwable) {
                _keyWords.value = ResourceState.Error("Erro ao buscar dados: ${t.message}")
                Log.e("DetalhesViewModel", "Erro ao buscar dados: ${t.message}", t)
            }
        }
    }

    // Função para tratar a resposta da keyWords
    private fun handleResponseKeyWords(response: Response<keywordsModel>): ResourceState<keywordsModel> {
        return if (response.isSuccessful) {
            response.body()?.let { ResourceState.Success(it) }
                ?: ResourceState.Error("Corpo da resposta vazio")
        } else {
            ResourceState.Error(response.message())
        }
    }


    // Função para buscar detalhes da série
    fun fetchSeriesDetails(seriesId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailsTvs(seriesId)
                _details.value = handleResponseSeries(response)
            } catch (t: Throwable) {
                _details.value = ResourceState.Error("Erro ao buscar dados: ${t.message}")
                Log.e("DetalhesViewModel", "Erro ao buscar dados: ${t.message}", t)
            }
        }
    }

    // Função para tratar a resposta da série
    private fun handleResponseSeries(response: Response<DetalhesTvModel>): ResourceState<DetalhesTvModel> {
        return if (response.isSuccessful) {
            response.body()?.let { ResourceState.Success(it) }
                ?: ResourceState.Error("Corpo da resposta vazio")
        } else {
            ResourceState.Error(response.message())
        }
    }

    // Função para buscar detalhes da temporada
    fun fetchSeasonDetails(tvShowId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getTemporadasTvs(tvShowId, seasonNumber)
                _detalhesTemporada.value = handleResponseSeason(response)
            } catch (t: Throwable) {
                _detalhesTemporada.value = ResourceState.Error("Erro ao buscar dados: ${t.message}")
                Log.e("DetalhesViewModel", "Erro ao buscar dados: ${t.message}", t)
            }
        }
    }

    // Função para tratar a resposta da temporada
    private fun handleResponseSeason(response: Response<DetalhesTemporadasModel>): ResourceState<DetalhesTemporadasModel> {
        return if (response.isSuccessful) {
            response.body()?.let { ResourceState.Success(it) }
                ?: ResourceState.Error("Corpo da resposta vazio")
        } else {
            ResourceState.Error(response.message())
        }
    }
}
