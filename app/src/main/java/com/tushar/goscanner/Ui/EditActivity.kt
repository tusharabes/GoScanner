package com.tushar.goscanner.Ui


import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tushar.goscanner.R
import com.tushar.goscanner.adapter.FilterAdapter
import com.tushar.goscanner.databinding.ActivityEditBinding


class EditActivity : AppCompatActivity() , FilterAdapter.ImageClickListener{

    private lateinit var _binding:ActivityEditBinding
    private lateinit var adapter: FilterAdapter
    private lateinit var filterImages: ArrayList<ImageView>
    private lateinit var image:ImageFilterView
    private lateinit var imageUri:Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.activity_edit)


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            window.statusBarColor=resources.getColor(R.color.skyBlue,null)
        }


        _binding=DataBindingUtil.setContentView(this,R.layout.activity_edit)
        image= ImageFilterView(this)
        image.setImageURI(Uri.parse(intent.getStringExtra("image")))
        imageUri=Uri.parse(intent.getStringExtra("image"))


        if(intent.getStringExtra("image")!=null)
        {
            _binding.editImage.setImageURI(Uri.parse(intent.getStringExtra("image")))
        }


//        Setting Brightness by using seek bar

        _binding.seekBrightness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                _binding.editImage.setBrightness(1+progress/5.0f)
                Log.d("tag","$progress")

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

//        Setting Contrast by using Seek bar

        _binding.seekContrast.setOnSeekBarChangeListener(object:OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

                _binding.editImage.alpha=1-progress/10.0f

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })


//        Creating the filtered list for edit image
        filterImages=ArrayList()

//        Original Image Filter

        val originalImage=ImageFilterView(this)
        originalImage.setImageURI(Uri.parse(intent.getStringExtra("image")))
        filterImages.add(originalImage)

//        Black and white Image
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)
        val image=ImageFilterView(this)
        image.setImageURI(Uri.parse(intent.getStringExtra("image")))
        image.colorFilter=filter
        filterImages.add(image)



//        Creating Red Image
        val redImage=ImageFilterView(this)
        redImage.setImageURI(Uri.parse(intent.getStringExtra("image")))
        redImage.warmth=2.0f
        filterImages.add(redImage)

//        Creating warmth image Colder
        val coldImage=ImageFilterView(this)
        coldImage.setImageURI(Uri.parse(intent.getStringExtra("image")))
        coldImage.warmth=0.5f
        filterImages.add(coldImage)

//        Setting up the recyclerView
        adapter=FilterAdapter(this,filterImages)
        _binding.recyclerView.layoutManager=LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        _binding.recyclerView.adapter=adapter

    }

    override fun onImageClick(position:Int) {
        when (position) {
            0 -> {
                _binding.editImage.setImageURI(imageUri)
                _binding.editImage.warmth=1.0f
                _binding.editImage.saturation=1.0f
            }
            1 -> {
                _binding.editImage.setImageURI(imageUri)
                _binding.editImage.warmth=1.0f
                _binding.editImage.saturation = 0.5f
                Log.d("Tag","Saturation${_binding.editImage.saturation}")

            }
            2 -> {
                _binding.editImage.setImageURI(imageUri)
                _binding.editImage.warmth = 2.0f
            }
            3 -> {
                _binding.editImage.setImageURI(imageUri)
                _binding.editImage.warmth = 0.5f
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=MenuInflater(this).inflate(R.menu.edit_menu,menu)
        return true
    }

}