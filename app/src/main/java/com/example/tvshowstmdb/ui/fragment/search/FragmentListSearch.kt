package com.example.tvshowstmdb.ui.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tvshowstmdb.databinding.FragmentListaBinding
import com.example.tvshowstmdb.resourcestate.ResourceState
import com.example.tvshowstmdb.ui.adapters.ListSeriesAdapter
import com.example.tvshowstmdb.ui.base.BaseFragment
import com.example.tvshowstmdb.util.hide
import com.example.tvshowstmdb.util.show
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentListSearch : BaseFragment<FragmentListaBinding, SearchListViewModel>() {

    private val listSerieAdapter by lazy { ListSeriesAdapter() }

    override val viewModel: SearchListViewModel by viewModels()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerFrameLayout = binding.shimmerViewContainer
        setupRecyclerView()
        clickAdapter()
        colectObserver()
        setupPesquisa()
        setupShimmer()
    }

    private fun setupShimmer() {
        shimmerFrameLayout.startShimmer()
    }

    private fun setupPesquisa() {
        val searchView = binding.edtSearch

        // Configura o listener no SearchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val query = newText ?: ""

                val handler = android.os.Handler()
                handler.postDelayed({
                    performSearch(query)
                }, 500)
                return true
            }
        })
    }


    private fun performSearch(query: String) {
        viewModel.searchTvShows(query)
    }


    private fun setupRecyclerView() = with(binding) {
        rvListSeries.apply {

            val layoutManager = GridLayoutManager(requireContext(), 2)
            rvListSeries.layoutManager = layoutManager
            adapter = listSerieAdapter

        }

    }

    private fun clickAdapter() {
        listSerieAdapter.setOnClickListener { serieId ->
            val action = FragmentListSearchDirections
                .actionFragmentListSearchToFragmentDetalhes(serieId, 1)
            findNavController().navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun colectObserver() = lifecycleScope.launch {
        viewModel.list.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {

                    delay(3000)
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.hide()

                    if (resource.data?.results.isNullOrEmpty()) {
                        binding.rvListSeries.hide()
                        binding.imgNotFound.show()
                        binding.txtNotContent.show()
                    } else {

                        binding.rvListSeries.show()
                        binding.imgNotFound.hide()
                        binding.txtNotContent.hide()
                        resource.data?.let { values ->
                            Log.d(
                                "FragmentListSearch",
                                "Dados recebidos: ${values.results.size} itens"
                            )
                            listSerieAdapter.seriados = values.results
                            listSerieAdapter.notifyDataSetChanged()
                        }
                    }
                }

                is ResourceState.Error -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.hide()
                    Log.i("FragmentListSearch", "Erro: ${resource.message}")
                }

                is ResourceState.Empty -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.hide()
                    binding.imgNotFound.show()
                    binding.txtNotContent.show()
                    Log.i("FragmentListSearch", "Estado vazio detectado")
                }

                is ResourceState.Loading -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.show()
                    binding.rvListSeries.hide()
                    binding.imgNotFound.hide()
                    binding.txtNotContent.hide()
                    Log.i("FragmentListSearch", "Carregando dados...")
                }
            }
        }
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListaBinding =
        FragmentListaBinding.inflate(inflater, container, false)


}