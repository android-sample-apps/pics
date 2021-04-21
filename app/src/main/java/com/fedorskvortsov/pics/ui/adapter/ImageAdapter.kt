package com.fedorskvortsov.pics.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fedorskvortsov.pics.R
import com.fedorskvortsov.pics.databinding.ListItemImageBinding

class ImageAdapter(
    private val listener: ImageAdapterListener
) : ListAdapter<String, ImageAdapter.ViewHolder>(ImageDiffCallback) {

    interface ImageAdapterListener {
        fun onImageClicked(view: View, url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

    class ViewHolder(
        private val binding: ListItemImageBinding,
        private val listener: ImageAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {
            binding.root.apply {
                transitionName = context.getString(R.string.image_transition_name, url)
                setOnClickListener { view ->
                    listener.onImageClicked(view, url)
                }
            }
            bindImageFromUrl(binding.root, url)
        }

        private fun bindImageFromUrl(imageView: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            }
        }
    }
}

object ImageDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
