package com.tushar.goscanner.Ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tushar.goscanner.R
import com.tushar.goscanner.adapter.DocumentAdapter
import com.tushar.goscanner.databinding.ActivityMainBinding
import com.tushar.goscanner.model.DocumentData


class MainActivity : AppCompatActivity() {

    private lateinit var openGallery: Button
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

        val documentList=ArrayList<DocumentData>()

        documentList.add(DocumentData("Driving Licence",R.drawable.drivinglicense))
        documentList.add(DocumentData("Passport",R.drawable.passport))
        documentList.add(DocumentData("Voter Card",R.drawable.voterid))
        documentList.add(DocumentData("Aadhar Card",R.drawable.aadhar))


        documentAdapter= DocumentAdapter(this,documentList)
        _binding.documents.adapter=documentAdapter



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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.edit_menu,menu)
        return true
    }


}