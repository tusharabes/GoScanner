package com.tushar.goscanner.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tushar.goscanner.R
import com.tushar.goscanner.Ui.EditActivity


class FilterAdapter(val context:Context ,val list : ArrayList<ImageView>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    val imageNames= arrayListOf("ORIG","B & W", "WARM", "COLD")

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
         var imageView:ImageView
         var imageName:TextView

        init {
            imageView=view.findViewById(R.id.filter_image)
            imageName=view.findViewById(R.id.imageName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       val view =LayoutInflater.from(context).inflate(R.layout.filter_items,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.imageView.setImageDrawable(list[position].drawable)
        holder.imageName.text = imageNames[position]

        holder.imageView.setOnClickListener{
            val listener=context as EditActivity
            listener.onImageClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ImageClickListener{
        fun onImageClick(position : Int)
    }
}