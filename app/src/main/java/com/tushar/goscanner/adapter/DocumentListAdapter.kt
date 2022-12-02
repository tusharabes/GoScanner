package com.tushar.goscanner.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tushar.goscanner.R
import com.tushar.goscanner.Ui.DocsListActivity

class DocumentListAdapter(val context: Context) : RecyclerView.Adapter<DocumentListAdapter.InnerViewHolder>() {

    private  var images = ArrayList<Bitmap>()
    private  var names = ArrayList<String>()
    inner class InnerViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
         var imageView: ImageView
         var textView: TextView
         var delete : ImageButton
        init {
            imageView=view.findViewById(R.id.docsImage)
            textView=view.findViewById(R.id.docName)
            delete=view.findViewById(R.id.deleteImages)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentListAdapter.InnerViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.docs_list,parent,false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.imageView.setImageBitmap(images[position])
        holder.textView.text = names[position]
        holder.delete.setOnClickListener{
           val tempCont=context as DocsListActivity
            tempCont.deleteDocs(names[position])
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun updateList(img:ArrayList<Bitmap>,name:ArrayList<String>)
    {
        Toast.makeText(context,"${img.size}",Toast.LENGTH_SHORT).show()
        images.clear()
        names.clear()
        images.addAll(img)
        names.addAll(name)
        notifyDataSetChanged()
    }
}

interface DeleteDocs
{
    fun deleteDocs(dFile: String)
}