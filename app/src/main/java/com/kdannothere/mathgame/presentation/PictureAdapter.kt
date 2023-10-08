package com.kdannothere.mathgame.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kdannothere.mathgame.databinding.PictureBinding

class PictureAdapter(
    private val list: List<Picture>,
) :
    RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    class PictureViewHolder(val binding: PictureBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val pictureBinding = PictureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return PictureViewHolder(pictureBinding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val resId = list[position].resId
        holder.binding.imagePicture.setImageResource(resId)
    }
}
