package com.tushar.goscanner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tushar.goscanner.R
import com.tushar.goscanner.Ui.AadharCard
import com.tushar.goscanner.Ui.DrivingLicence
import com.tushar.goscanner.Ui.Passport
import com.tushar.goscanner.Ui.VoterId
import com.tushar.goscanner.model.DocumentData

class DocumentAdapter(var contexts: Context, documentDataArrayList: ArrayList<DocumentData>) :
    ArrayAdapter<DocumentData?>(contexts, 0, documentDataArrayList as List<DocumentData?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.document_items, parent, false)
        }

        val documentList: DocumentData? = getItem(position)
        val courseTV = listitemView!!.findViewById<TextView>(R.id.idTVCourse)
        val courseIV = listitemView.findViewById<ImageView>(R.id.idIVcourse)
        val document = listitemView.findViewById<LinearLayout>(R.id.document)


        document.setOnClickListener{
            when(position)
            {
                0->{
                    contexts.startActivity(Intent(contexts,DrivingLicence::class.java).apply {
                        putExtra("document","Driving Licence")
                    })

                }
                1->{
                    contexts.startActivity(Intent(contexts, Passport::class.java).apply {
                        putExtra("document","Passport")
                    })
                }

                2->{
                    contexts.startActivity(Intent(context,VoterId::class.java))
                }
                3->{
                    contexts.startActivity(Intent(contexts,AadharCard::class.java))
                }
            }
        }


        courseIV.setImageResource(documentList!!.imgid)
        courseTV.text = documentList.document_name


        return listitemView
    }
}
