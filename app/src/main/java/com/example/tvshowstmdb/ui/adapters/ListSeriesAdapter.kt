package com.example.tvshowstmdb.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tvshowstmdb.R
import com.example.tvshowstmdb.data.model.popular.Result
import com.example.tvshowstmdb.databinding.ItemListaTvShowsBinding
import com.example.tvshowstmdb.util.Constants
import com.squareup.picasso.Picasso

class ListSeriesAdapter : RecyclerView.Adapter<ListSeriesAdapter.ListSeriesViewHolder>() {


    inner class ListSeriesViewHolder(
        val binding: ItemListaTvShowsBinding
    ) : ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {

            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallBack)
    var seriados: List<Result>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSeriesViewHolder {
        return ListSeriesViewHolder(
            ItemListaTvShowsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListSeriesAdapter.ListSeriesViewHolder, position: Int) {

        val series = seriados[position]
        holder.binding.apply {

            val url = Constants.BASE_URL_IMAGE
            val poster = series.poster_path
            val tamanhoPoster = "w780"
            if (!poster.isNullOrEmpty()) {
                val urlImage = url + tamanhoPoster + poster

                Picasso.get()
                    .load(urlImage)
                    .into(imgPoster, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            Log.d("Picasso", "Imagem carregada com sucesso")
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Picasso", "Erro ao carregar imagem", e)
                        }
                    })
            } else {
                txtSemPoster.visibility = View.VISIBLE
                imgPoster.setImageResource(R.drawable.no_poster_found_img)
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let{ listener ->
                listener(series.id) }

        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = seriados.size
}
