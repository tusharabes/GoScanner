package com.tushar.goscanner.Ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tushar.goscanner.R
import com.tushar.goscanner.adapter.DeleteDocs
import com.tushar.goscanner.adapter.DocumentListAdapter
import com.tushar.goscanner.databinding.ActivityDocsListActivityBinding
import com.tushar.goscanner.viewmodel.DocumentListVM
import java.text.SimpleDateFormat
import java.util.*

class DocsListActivity : AppCompatActivity() , DeleteDocs{

    private lateinit var mBinding: ActivityDocsListActivityBinding
    private lateinit var adapter : DocumentListAdapter
    private lateinit var progress:ProgressBar

    private lateinit var imageList:ArrayList<Bitmap>
    private lateinit var fileName:ArrayList<String>
    private lateinit var time:ArrayList<String>


    private lateinit var documentListVM : DocumentListVM



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.activity_docs_list_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_docs_list_activity)

        initViews()


        documentListVM.documentList.observe(this, Observer {files->

            imageList.clear()
            fileName.clear()
            time.clear()

            files?.filter{ it.isFile && it.name.contains(".jpg")|| it.name.contains(".JPG") && it.canRead()}?.map {
                val bytes=it.readBytes()
                val img=BitmapFactory.decodeByteArray(bytes,0,bytes.size)

                val formatter = SimpleDateFormat("dd/MM/yyyy",Locale.US)

                // Create a calendar object that will convert the date and time value in milliseconds to date.
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it.lastModified()


                time.add(formatter.format(calendar.time))
                imageList.add(img)
                fileName.add(it.name)

            }

            mBinding.bvDocsCount="Documents : ${imageList.size}"

            adapter.updateList(imageList,fileName,time)

            progress.visibility=View.INVISIBLE

        })
    }

    private fun initViews() {
        documentListVM= ViewModelProvider(this)[DocumentListVM::class.java]

        adapter=DocumentListAdapter(this)
        mBinding.docsRecycler.adapter=adapter
        mBinding.docsRecycler.layoutManager=LinearLayoutManager(this)
        progress=findViewById(R.id.progressBar)

        imageList= ArrayList()
        fileName= ArrayList()
        time= ArrayList()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return super.onCreateOptionsMenu(menu)
    }

    override fun deleteDocs(dFile: String) {
        documentListVM.deleteFiles(dFile)
    }
}