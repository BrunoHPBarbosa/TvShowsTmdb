package com.example.tvshowstmdb.ui.fragment.detalhes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshowstmdb.R
import com.example.tvshowstmdb.databinding.FragmentDetalhesBinding
import com.example.tvshowstmdb.resourcestate.ResourceState
import com.example.tvshowstmdb.ui.adapters.DetalhesEpisodiosAdapter
import com.example.tvshowstmdb.ui.base.BaseFragment
import com.example.tvshowstmdb.util.Constants
import com.example.tvshowstmdb.util.hide
import com.example.tvshowstmdb.util.show
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDetalhes : BaseFragment<FragmentDetalhesBinding, DetalhesViewModel>() {

    override val viewModel: DetalhesViewModel by viewModels()
    private val episodiosAdapter = DetalhesEpisodiosAdapter()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerFrameLayout = binding.shimmerViewContainer
        val seriesId = arguments?.getInt("serieId") ?: run {
            showError("ID da série não encontrado.")
            return
        }
        val seasonNumber = arguments?.getInt("seasonNumber") ?: run {
            showError("Número da temporada não encontrado.")
            return
        }

        // Configura o RecyclerView para episódios
        binding.rvTemporadas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = episodiosAdapter
        }

        // Inicia as buscas
        viewModel.fetchSeriesDetails(seriesId)
        viewModel.fetchKeyWords(seriesId)
        viewModel.fetchSeasonDetails(seriesId, seasonNumber)


        observeDetails()
        observeTemporadas()
        setupSpinner(seriesId)
        setupShimmer()
        observeKey()
    }

    override fun onStart() {
        super.onStart()
        initEventClick()
    }

    private fun setupShimmer() {
        shimmerFrameLayout.startShimmer()
    }

    private fun initEventClick() = with(binding) {
        imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSpinner(serieId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.details.collect { resource ->
                when (resource) {
                    is ResourceState.Success -> {

                        val detalhes = resource.data
                        delay(2000)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hide()
                        binding.rvTemporadas.show()

                        detalhes?.seasons?.let { seasons ->
                            val spinnerAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                seasons.map { "Temporadas ${it.season_number}" }
                            )
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerTemporadas.adapter = spinnerAdapter

                            binding.spinnerTemporadas.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val selectedSeason = seasons[position]
                                        viewModel.fetchSeasonDetails(
                                            serieId,
                                            selectedSeason.season_number
                                        )
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {

                                    }
                                }
                        }
                    }

                    is ResourceState.Error -> {
                        showError(resource.message)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hide()
                    }

                    is ResourceState.Empty -> {
                        showEmptyState("Nenhuma temporada encontrada.")
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hide()
                    }

                    is ResourceState.Loading -> {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.show()
                        binding.rvTemporadas.hide()

                    }
                }
            }
        }
    }

    private fun observeKey() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.keyWords.collect { resource ->
                when (resource) {
                    is ResourceState.Success -> {
                        val keywords = resource.data
                        binding.apply {
                            txtkeywords.text = keywords?.results?.joinToString("") { "#${it.name}" }
                        }
                    }

                    is ResourceState.Error -> {
                        showError(resource.message)

                    }

                    is ResourceState.Empty -> {
                        showEmptyState("Nenhum detalhe encontrado.")
                    }

                    is ResourceState.Loading -> {

                    }
                }
            }
        }
    }

    private fun observeDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.details.collect { resource ->
                when (resource) {
                    is ResourceState.Success -> {
                        val details = resource.data
                        binding.apply {

                            txtTvTitulo.text = details?.name
                            txtDescricaoTv.text = details?.overview
                            txtGeneros.text = details?.genres?.joinToString(", ") { it.name }

                            if (details?.overview.isNullOrEmpty()) {
                                txtDescricaoTv.text = "Sem Descriscao do conteudo"
                            }
                            val url = Constants.BASE_URL_IMAGE
                            val poster = details?.poster_path
                            val tamanhoPoster = "w780"
                            if (!poster.isNullOrEmpty()) {
                                val urlImage = "$url$tamanhoPoster$poster"
                                Picasso.get().load(urlImage).into(imgPoster)

                                val url = Constants.BASE_URL_IMAGE
                                val poster = details?.backdrop_path
                                val tamanhoPoster = "w780"
                                if (!poster.isNullOrEmpty()) {
                                    val urlImage = "$url$tamanhoPoster$poster"
                                    Picasso.get().load(urlImage).into(imgBackPoster)
                                }
                            } else {
                                imgPoster.setImageResource(R.drawable.no_poster_found_img)
                                imgBackPoster.setImageResource(R.drawable.no_poster_found_img)
                            }
                        }
                    }

                    is ResourceState.Error -> {
                        showError(resource.message)

                    }

                    is ResourceState.Empty -> {
                        showEmptyState("Nenhum detalhe encontrado.")
                    }

                    is ResourceState.Loading -> {

                    }
                }
            }
        }
    }

    private fun observeTemporadas() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detalhesTemporada.collect { resource ->
                when (resource) {
                    is ResourceState.Success -> {
                        delay(3000)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hide()
                        binding.rvTemporadas.show()
                        val detalhesTemporada = resource.data
                        episodiosAdapter.submitList(detalhesTemporada?.episodes)
                    }

                    is ResourceState.Error -> {
                        showError(resource.message)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hide()
                    }

                    is ResourceState.Empty -> {
                        showEmptyState("Nenhuma temporada encontrada.")
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hide()
                    }

                    is ResourceState.Loading -> {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.show()
                        binding.rvTemporadas.hide()
                    }
                }
            }
        }
    }

    private fun showError(message: String?) {

    }

    private fun showEmptyState(message: String) {

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetalhesBinding =
        FragmentDetalhesBinding.inflate(inflater, container, false)
}
