package com.example.quizapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class CustomAdapter(private val dataList: List<RankViewModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_rank_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = dataList[position]
        holder.rank.text = items.pos.toString()
        holder.name.text = items.name
        holder.score.text = "${items.score} / 10"

        if (items.imgSrc.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(items.imgSrc).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.avatar)
        }

    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image: CircleImageView = itemView.findViewById(R.id.rankImageView)
        val rank: TextView = itemView.findViewById(R.id.rankTV)
        val name: TextView = itemView.findViewById(R.id.rankNameTV)
        val score: TextView = itemView.findViewById(R.id.rankScoreTV)

    }
}