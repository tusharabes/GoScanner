package com.tushar.goscanner.Ui


import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import com.tushar.goscanner.R
import com.tushar.goscanner.databinding.ActivityDrivingLicenceBinding
import java.io.*


class DrivingLicence : AppCompatActivity() {

    private lateinit var mBinding:ActivityDrivingLicenceBinding
    private lateinit var openImage:ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.activity_driving_licence)



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_driving_licence)



        loadImageFromStorage("/data/user/0/com.tushar.goscanner/app_imageDir")

        openImage=registerContractToOpenImage()




    }
    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "dl.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))

            mBinding.drivingLicenceImage.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "dl.jpg")
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
        Log.d("tag","${directory.absolutePath}")
        return directory.absolutePath
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.open_doc_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> finish()

            R.id.open -> {
                openImage.launch("image/*")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun registerContractToOpenImage(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()){
            if(it==null)
            {
                Toast.makeText(this,"Please Select an Image", Toast.LENGTH_LONG).show()
            }
            else
            {
                mBinding.drivingLicenceImage.setImageURI(it)
                saveToInternalStorage(mBinding.drivingLicenceImage.drawToBitmap())
            }

        }
    }
}