package com.kdannothere.mathgame.presentation.elements.record

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.data.record.Record
import com.kdannothere.mathgame.databinding.ElementRecordBinding
import com.kdannothere.mathgame.presentation.managers.LangMng
import com.kdannothere.mathgame.presentation.managers.TimeMng
import java.util.Date

class RecordAdapter(
    private var recordList: List<Record>,
    var localizedContext: Context? = null,
) :
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    class RecordViewHolder(val binding: ElementRecordBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val recordBinding = ElementRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return RecordViewHolder(recordBinding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        setAllTexts(holder)
        setAllValues(holder, position)
    }

    private fun setAllTexts(holder: RecordViewHolder) {
        holder.binding.time.text = getText(R.string.time)
        holder.binding.topic.text = getText(R.string.topic)
        holder.binding.level.text = getText(R.string.level)
        holder.binding.correct.text = getText(R.string.correct)
        holder.binding.mistakes.text = getText(R.string.mistakes)
        holder.binding.skipped.text = getText(R.string.skipped)
    }

    private fun setAllValues(holder: RecordViewHolder, position: Int) {
        val record = recordList[position]
        holder.binding.timeValue.text = TimeMng.getTimeToShow(Date(
            record.timeStamp.toLong()
        ))
        holder.binding.topicValue.text = getText(LangMng.getTopicResId(record.topic))
        holder.binding.levelValue.text = record.level
        holder.binding.correctValue.text = record.correct
        holder.binding.mistakesValue.text = record.mistakes
        holder.binding.skippedValue.text = record.skipped
    }

    private fun getText(stringResId: Int): String {
        return localizedContext?.getString(stringResId) ?: ""
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecordList: List<Record>) {
        this.recordList = newRecordList
        notifyDataSetChanged()
    }
}
