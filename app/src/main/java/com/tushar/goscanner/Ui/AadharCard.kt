package com.tushar.goscanner.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tushar.goscanner.R
import com.tushar.goscanner.databinding.ActivityAadharCardBinding

class AadharCard : AppCompatActivity() {

    private lateinit var mBinding:ActivityAadharCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GoScanner)
        setContentView(R.layout.activity_aadhar_card)

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_aadhar_card)

        mBinding.bvAadhar="Aadhar Card"
    }
}