package com.tushar.goscanner.repository

import androidx.lifecycle.LiveData
import java.io.File

class ImageRepository {

     fun loadImages(path : File): Array<File>? {

        return path.listFiles()
    }
}