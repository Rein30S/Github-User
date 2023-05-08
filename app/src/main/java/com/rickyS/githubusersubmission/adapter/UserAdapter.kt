package com.rickyS.githubusersubmission.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rickyS.githubusersubmission.DetailUserActivity
import com.rickyS.githubusersubmission.R
import com.rickyS.githubusersubmission.api.ItemsItem

class UserAdapter(private val listReview: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = listReview[position]
        viewHolder.apply {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(imgUser)
            tvUser.text = user.login
            viewHolder.itemView.setOnClickListener{
                val contexts = viewHolder.itemView.context
                val sendUser = user.login
                val intent = Intent(contexts, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRADATA, sendUser)
                contexts.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = listReview.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUser: TextView = view.findViewById(R.id.tvUser)
        val imgUser: ImageView = view.findViewById(R.id.ivUser)
    }
}