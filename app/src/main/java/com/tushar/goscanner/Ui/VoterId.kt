package com.tushar.goscanner.Ui

import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.tushar.goscanner.R
import com.tushar.goscanner.databinding.VoterIdBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*


class VoterId : AppCompatActivity() {

    private lateinit var mBinding: VoterIdBinding
    private lateinit var imageContract:ActivityResultLauncher<String>
    companion object{
        const val imagePath="/data/user/0/com.tushar.goscanner/app_imageDir"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.voter_id)



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding=DataBindingUtil.setContentView(this,R.layout.voter_id)

        mBinding.bvVoter="Voter ID"

        lifecycleScope.launch(Dispatchers.IO) {
            loadImageFromStorage(imagePath)
        }


        imageContract=registerContractToOpenImage()

    }

    private suspend fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "voterId.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))

            withContext(Dispatchers.Main)
            {
                mBinding.voterIdImage.setImageBitmap(b)
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private  fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "voterId.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        return directory.absolutePath
    }

    private fun registerContractToOpenImage(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()){
            if(it==null)
            {
                Toast.makeText(this,"Please Select an Image", Toast.LENGTH_LONG).show()
            }
            else
            {
                mBinding.voterIdImage.setImageURI(it)
                mBinding.voterIdImage.visibility=View.VISIBLE
               lifecycleScope.launch(Dispatchers.IO)
               {
                   saveToInternalStorage(mBinding.voterIdImage.drawToBitmap())
               }

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->finish()

            R.id.open->{
                imageContract.launch("image/*")
            }
            R.id.delete->
            {
                openAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAlertDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@VoterId)
        builder.setMessage("Do you want to Delete VoterId ?")

        builder.setTitle("Delete VoterId!")

        builder.setCancelable(false)


        builder.setPositiveButton("Yes"
        ) { dialog: DialogInterface, _: Int ->
            // If user click no then dialog box is canceled.
            dialog.cancel()
            deleteVoterIdImage()
            mBinding.voterIdImage.visibility = View.INVISIBLE
        }


        builder.setNegativeButton("No"
        ) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteVoterIdImage() {
        try {

            val f = File(imagePath, "voterId.jpg")
            f.delete()
            Toast.makeText(this, "VoterId Deleted",Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.open_doc_menu,menu)
        return true
    }
}