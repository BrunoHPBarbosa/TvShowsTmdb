package com.example.tvshowstmdb.ui.adapters

import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tvshowstmdb.R
import com.example.tvshowstmdb.data.model.temporadas.Episode
import com.example.tvshowstmdb.databinding.ItemDetalhesEpisodiosBinding
import com.example.tvshowstmdb.util.Constants
import com.squareup.picasso.Picasso

class DetalhesEpisodiosAdapter: RecyclerView.Adapter< DetalhesEpisodiosAdapter.DetalhesSeriesViewHolder>() {

    private var episodios : List<Episode> = emptyList()

    inner class DetalhesSeriesViewHolder(
        val binding : ItemDetalhesEpisodiosBinding
    ): ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalhesSeriesViewHolder {
        val binding = ItemDetalhesEpisodiosBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DetalhesSeriesViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DetalhesSeriesViewHolder, position: Int) {
        val episodio = episodios[position]
        holder.binding.apply {

            txtNomeEpisodio.text = episodio.name
            txtTempoEpisodio.text =  "${episodio.runtime} ${holder.itemView.context.getString(R.string.min)}"
            txtDescricaoEpisodio.text = episodio.overview
            if (episodio.overview.isNullOrEmpty()){
                txtDescricaoEpisodio.text = holder.itemView.context.getString(R.string.text_sem_descricao)
            }
            txtNumeroEpisodio.text = episodio.episode_number.toString()

            val url = Constants.BASE_URL_IMAGE
            val tamanhoPoster = "w780"
            val stillPath = episodio.still_path
            if (!stillPath.isNullOrEmpty()) {
                val urlImage = "$url$tamanhoPoster$stillPath"
                Picasso.get().load(urlImage).into(imgTemporada)
            } else {
                imgTemporada.setImageResource(R.drawable.no_poster_found_img)

            }

        }

    }
    override fun getItemCount(): Int  = episodios.size

        fun submitList(novosEpisodios: List<Episode>?) {
            episodios = novosEpisodios ?: emptyList()
            notifyDataSetChanged()

    }
}