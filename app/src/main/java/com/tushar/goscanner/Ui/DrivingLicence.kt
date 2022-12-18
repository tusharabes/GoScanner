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
import com.tushar.goscanner.databinding.DrivingLicenceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*


class DrivingLicence : AppCompatActivity() {

    private lateinit var mBinding:DrivingLicenceBinding
    private lateinit var openImage:ActivityResultLauncher<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.driving_licence)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding=DataBindingUtil.setContentView(this,R.layout.driving_licence)
        mBinding.bvDocument="Driving Licence"

        loadImageFromStorage("/data/user/0/com.tushar.goscanner/app_imageDir")
        openImage=registerContractToOpenImage()

        mBinding.rotate.setOnClickListener{
            mBinding.drivingLicenceImage.rotation= mBinding.drivingLicenceImage.rotation + 90.0f
        }

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
            R.id.delete ->{
                openAlertDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun openAlertDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DrivingLicence)
        builder.setMessage("Do you want to Delete DL ?")

        builder.setTitle("Delete Driving Licence!")

        builder.setCancelable(false)


        builder.setPositiveButton("Yes"
        ) { dialog: DialogInterface, _: Int ->
            // If user click no then dialog box is canceled.
            dialog.cancel()
            deleteDrivingLicence()
            mBinding.drivingLicenceImage.visibility = View.INVISIBLE
        }


        builder.setNegativeButton("No"
        ) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteDrivingLicence() {
        try {
            val f = File(VoterId.imagePath, "dl.jpg")
            f.delete()
            Toast.makeText(this, "Driving Licence Deleted",Toast.LENGTH_SHORT).show()
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
                mBinding.drivingLicenceImage.setImageURI(it)
                mBinding.drivingLicenceImage.visibility=View.VISIBLE
                lifecycleScope.launch(Dispatchers.IO)
                {
                    saveToInternalStorage(mBinding.drivingLicenceImage.drawToBitmap())
                }
            }

        }
    }
}