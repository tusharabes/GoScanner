package com.tushar.goscanner.Ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tushar.goscanner.R
import com.tushar.goscanner.databinding.ActivityPassportBinding

class Passport : AppCompatActivity() {

    private lateinit var mBinding:ActivityPassportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.activity_passport)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_passport)

        mBinding.passportDoc="Passport"



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.open_doc_menu,menu)
        return true
    }
}