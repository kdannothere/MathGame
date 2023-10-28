package com.kdannothere.mathgame.presentation.elements.level

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kdannothere.mathgame.databinding.ElementLevelBinding

class LevelAdapter(
    private val levelList: List<Level>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    class LevelViewHolder(val binding: ElementLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Level, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(level: Level)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val levelBinding = ElementLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return LevelViewHolder(levelBinding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levelList[position], listener)
        holder.binding.id.text = levelList[position].id.toString()
    }
}
