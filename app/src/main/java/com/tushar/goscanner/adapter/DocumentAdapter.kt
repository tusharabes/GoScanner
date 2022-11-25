package com.tushar.goscanner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tushar.goscanner.R
import com.tushar.goscanner.model.DocumentData

class DocumentAdapter(context: Context, documentDataArrayList: ArrayList<DocumentData>) :
    ArrayAdapter<DocumentData?>(context, 0, documentDataArrayList as List<DocumentData?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.document_items, parent, false)
        }

        val documentModel: DocumentData? = getItem(position)
        val courseTV = listitemView!!.findViewById<TextView>(R.id.idTVCourse)
        val courseIV = listitemView.findViewById<ImageView>(R.id.idIVcourse)

        courseTV.setText(documentModel?.document_name)
        courseIV.setImageResource(documentModel!!.imgid)
        return listitemView
    }
}
