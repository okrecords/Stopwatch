package com.okre.stopwatch

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.okre.stopwatch.databinding.RecyclerItemBinding

class RecyclerViewAdapter(private val list: MutableList<RecordData>, private val owner: Activity)
    :RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        with(holder.binding) {
            rvSection.text = data.section
            rvRecord.text = data.record
            rvWholeTime.text = data.wholeTime
        }
    }

    override fun getItemCount(): Int = list.size

}