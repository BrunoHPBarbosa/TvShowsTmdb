package com.example.tvshowstmdb.ui.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.tvshowstmdb.databinding.FragmentDetalhesBinding
import com.example.tvshowstmdb.databinding.FragmentListaBinding
import com.example.tvshowstmdb.resourcestate.ResourceState
import com.example.tvshowstmdb.ui.adapters.ListSeriesAdapter
import com.example.tvshowstmdb.ui.base.BaseFragment
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentListSearch: BaseFragment<FragmentListaBinding,SearchListViewModel>() {

    private val listSerieAdapter by lazy { ListSeriesAdapter()}

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
        val searchEditText = binding.edtSearch


        // Configura o clique no ícone de busca
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                true
            } else {
                false
            }
        }

        // Adiciona um TextWatcher para buscar automaticamente enquanto o texto é digitado
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Pode-se optar por buscar automaticamente enquanto o texto é digitado
            }
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchEditText.postDelayed({
                    performSearch(query)
                }, 500) // 500 ms de atraso
            }
        })
    }

    private fun performSearch(query: String) {
        viewModel.searchTvShows(query)
    }



    private fun setupRecyclerView() = with(binding){
        rvListSeries.apply {

            val layoutManager = GridLayoutManager(requireContext(),2)
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
                    shimmerFrameLayout.visibility = View.GONE

                    if (resource?.data?.results.isNullOrEmpty()) {
                        binding.rvListSeries.visibility = View.GONE
                        binding.imgNotFound.visibility = View.VISIBLE
                        binding.txtNotContent.visibility = View.VISIBLE
                    } else {

                        binding.rvListSeries.visibility = View.VISIBLE
                        binding.imgNotFound.visibility = View.GONE
                        binding.txtNotContent.visibility = View.GONE
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
                    shimmerFrameLayout.visibility = View.GONE
                    Log.i("FragmentListSearch", "Erro: ${resource.message}")
                }
                is ResourceState.Empty -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    binding.imgNotFound.visibility = View.VISIBLE
                    binding.txtNotContent.visibility = View.VISIBLE
                    Log.i("FragmentListSearch", "Estado vazio detectado")
                }
                is ResourceState.Loading -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.VISIBLE
                    binding.rvListSeries.visibility =View.GONE
                    binding.imgNotFound.visibility = View.GONE
                    binding.txtNotContent.visibility = View.GONE
                    Log.i("FragmentListSearch", "Carregando dados...")
                }
            }
        }
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListaBinding =
        FragmentListaBinding.inflate(inflater,container,false)


}