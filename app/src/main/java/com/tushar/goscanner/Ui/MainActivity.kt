package com.tushar.goscanner.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tushar.goscanner.R
import com.tushar.goscanner.adapter.DocumentAdapter
import com.tushar.goscanner.databinding.ActivityMainBinding
import com.tushar.goscanner.model.DocumentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var openGallery: FloatingActionButton
    private lateinit var _binding: ActivityMainBinding
    private lateinit var documentAdapter:DocumentAdapter



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)

        setContentView(R.layout.activity_main)
        openGallery=findViewById(R.id.clickButton)
        _binding=DataBindingUtil.setContentView(this, R.layout.activity_main)



        val imageResult=registerContractToOpenImage()

        _binding.clickButton.setOnClickListener{
            imageResult.launch("image/*")
        }

        _binding.editDocs.setOnClickListener{
            val intent=Intent(this@MainActivity,DocsListActivity::class.java)
            startActivity(intent)
        }

        val documentList=ArrayList<DocumentData>()

        documentList.add(DocumentData("Driving Licence",R.drawable.drivinglicense))
        documentList.add(DocumentData("Passport",R.drawable.passport))
        documentList.add(DocumentData("Voter Card",R.drawable.voterid))
        documentList.add(DocumentData("Aadhar Card",R.drawable.aadhar))


        documentAdapter= DocumentAdapter(this,documentList)
        _binding.documents.adapter=documentAdapter

        lifecycleScope.launch(Dispatchers.IO)
        {
            _binding.textView= filesDir.listFiles()?.size.toString()+" File"
        }

    }

    override fun onResume() {
        lifecycleScope.launch(Dispatchers.IO)
        {
            _binding.textView= filesDir.listFiles()?.size.toString()+" File"
        }
        super.onResume()
    }

    private fun registerContractToOpenImage(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()){
            if(it==null)
            {
                Toast.makeText(this,"Please Select an Image",Toast.LENGTH_LONG).show()
            }
            else{
                val intent=Intent(this@MainActivity,EditActivity::class.java).apply {
                    putExtra("image",it.toString())
                    putExtra("extra","Hello")
                }
                startActivity(intent)
            }

        }
    }

}