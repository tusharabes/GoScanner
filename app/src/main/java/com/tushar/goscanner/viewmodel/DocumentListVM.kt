package com.tushar.goscanner.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tushar.goscanner.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class DocumentListVM(application: Application) : AndroidViewModel(application) {

    var documentList= MutableLiveData<List<File>?>()
    var documentListImmutable  = documentList as LiveData<List<File>?>

    private var repo:ImageRepository = ImageRepository()

    private var context=application

    init {

        viewModelScope.launch(Dispatchers.IO) {

            val x=repo.loadImages(application.filesDir)
            Log.d("Tag","${x?.size}")
            withContext(Dispatchers.Main)
            {
                documentList.value=x?.toList()
            }

        }
    }

    fun deleteFiles(dFile:String) =viewModelScope.launch(Dispatchers.IO) {
        context.deleteFile(dFile)

        val x=repo.loadImages(context.filesDir)

        withContext(Dispatchers.Main)
        {
            documentList.value=x?.toList()
            Log.d("Tag","Deleted")
        }
    }
}