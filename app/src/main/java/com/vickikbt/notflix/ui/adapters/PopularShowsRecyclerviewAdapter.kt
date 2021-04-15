package com.vickikbt.notflix.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vickikbt.data.util.DataFormatter.loadImage
import com.vickikbt.domain.models.Movie
import com.vickikbt.notflix.R
import com.vickikbt.notflix.databinding.PopularShowItemBinding
import com.vickikbt.notflix.util.log

class PopularShowsRecyclerviewAdapter constructor(private val showList: List<Movie>) :
    RecyclerView.Adapter<PopularShowsRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularShowsRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PopularShowItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.popular_show_item, parent, false)

        return PopularShowsRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularShowsRecyclerViewHolder, position: Int) {
        val context = holder.itemView.context
        val movie = showList[position]

        holder.bind(context, movie)
    }

    override fun getItemCount() = 8

}

class PopularShowsRecyclerViewHolder(private val binding: PopularShowItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, movie: Movie) {

        Glide.with(context)
            .load(loadImage(movie.poster_path))
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(binding.imageViewShowCover)

        binding.textViewShowTitle.text = movie.title

    }

}