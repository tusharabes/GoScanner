package com.tushar.goscanner.viewmodel

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrivingLicenceVM(application: Application) : AndroidViewModel(application) {

    lateinit var dlImage:LiveData<ImageView>

    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}