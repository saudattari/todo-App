package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.RvItemsBinding
import com.google.firebase.database.ValueEventListener

class rvAdapter(var list: List<rvDataModel>, private var context: Context, private var listener: ClickItems):RecyclerView.Adapter<rvAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //initialize binding
        val binding = RvItemsBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.todoTextView.text = list[position].task
        if(list[position].isComplete){
            holder.binding.complete.setBackgroundColor(ContextCompat.getColor(context, R.color.top))
        }
        else{
            holder.binding.complete.setBackgroundColor(ContextCompat.getColor(context, R.color.bg))
        }
    }
    interface ClickItems{
        fun isComplete1(view: View,data: rvDataModel)
    }
    @SuppressLint("NotifyDataSetChanged")
    inner class MyViewHolder(val binding: RvItemsBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.complete.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    listener.isComplete1(binding.complete,rvDataModel(list[position].task, list[position].isComplete, list[position].refKey))

                }
            }
        }
    }
}