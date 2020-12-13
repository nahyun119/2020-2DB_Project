package com.example.dbproject.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dbproject.Model.ActorModel
import com.example.dbproject.R

class ActorAdapter(val context: Context,private var list:ArrayList<ActorModel>) : RecyclerView.Adapter<ActorAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actor, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    fun updateList(actorModelList: ArrayList<ActorModel>){
        list.removeAll(list)
        list = actorModelList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profile = itemView.findViewById<ImageView>(R.id.img_actor)
        var name = itemView.findViewById<TextView>(R.id.text_actor_name)
        var sex = itemView.findViewById<TextView>(R.id.text_actor_sex)
        var age = itemView.findViewById<TextView>(R.id.text_actor_age)
        var rating = itemView.findViewById<RatingBar>(R.id.rating_actor)

        fun bind(position: Int) {
            Glide.with(context).load(R.drawable.user2).circleCrop().into(profile)
            name.text = list[position].actorName
            sex.text = list[position].actorSex
            age.text = list[position].actorAge.toString()
            rating.rating = list[position].actorRating.toFloat()
        }
    }
}
