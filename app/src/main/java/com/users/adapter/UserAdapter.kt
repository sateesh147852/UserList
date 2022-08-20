package com.users.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.users.databinding.UserItemBinding
import com.users.model.Data

class UserAdapter(private val data: List<Data>, val selectedItem: SelectedItem) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        context = parent.context
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userItemBinding.tvId.text = data[position].id.toString()
        holder.userItemBinding.tvName.text =
            String.format("%s %s", data[position].first_name, data[position].last_name)
        holder.userItemBinding.tvEmail.text = data[position].email
        if (data[position].avatar.isNotEmpty()) {
            Glide.with(context)
                .load(data[position].avatar)
                .into(holder.userItemBinding.ivImageView)
        }
        holder.userItemBinding.cardView.setOnClickListener {
            selectedItem.onItemSelect(data[position].id)
        }
    }

    override fun getItemCount(): Int = data.size

    class UserViewHolder(val userItemBinding: UserItemBinding) :
        RecyclerView.ViewHolder(userItemBinding.root)

    interface SelectedItem {
        fun onItemSelect(id: Int)
    }
}