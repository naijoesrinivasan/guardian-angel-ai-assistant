package com.example.myapplication

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GuardianDataAdapter(private val dataList: List<GuardianData>) :
    RecyclerView.Adapter<GuardianDataAdapter.GuardianDataViewHolder>() {

    class GuardianDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardianDataViewHolder {
        val layoutId = if (viewType == DataType.REQUEST.ordinal) {
            R.layout.item_guardian_data_request
        } else {
            R.layout.item_guardian_data_response

        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return GuardianDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuardianDataViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.text.text = currentItem.text
//        holder.type.text = currentItem.type.name // Assuming ENUM is an Enum class
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type.ordinal
    }
}
