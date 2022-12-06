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
import com.tushar.goscanner.databinding.ActivityPassportBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


class Passport : AppCompatActivity() {

    private lateinit var mBinding:ActivityPassportBinding
    private lateinit var contract:ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.activity_passport)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_passport)

        mBinding.passportDoc="Passport"
        contract=registerContractToOpenImage()

        lifecycleScope.launch(Dispatchers.IO)
        {
            loadImageFromStorage(VoterId.imagePath)
        }

    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "passport.jpg")
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
        mBinding.passportImage.visibility=View.VISIBLE
        return directory.absolutePath
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home-> finish()

            R.id.delete ->{
                openAlertDialog()
            }

            R.id.open ->{
                contract.launch("image/*")
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private suspend fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "passport.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))

            withContext(Dispatchers.Main)
            {
                mBinding.passportImage.setImageBitmap(b)
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun registerContractToOpenImage(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()){
            if(it==null)
            {
                Toast.makeText(this,"Please Select an Image", Toast.LENGTH_LONG).show()
            }
            else
            {
                mBinding.passportImage.setImageURI(it)
                lifecycleScope.launch(Dispatchers.IO)
                {

                    saveToInternalStorage(mBinding.passportImage.drawToBitmap())
                }
            }
        }
    }

    private fun openAlertDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@Passport)
        builder.setMessage("Do you want to Delete Passport ?")

        builder.setTitle("Delete Passport!")

        builder.setCancelable(false)

        builder.setPositiveButton("Yes"
        ) { dialog: DialogInterface, _: Int ->

            dialog.cancel()
            deletePassportImage()
            mBinding.passportImage.visibility = View.INVISIBLE
        }

        builder.setNegativeButton("No"
        ) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun deletePassportImage() {

        try {
            val f = File(VoterId.imagePath, "passport.jpg")
            f.delete()
            Toast.makeText(this, "Passport Deleted", Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.open_doc_menu,menu)
        return true
    }
}

