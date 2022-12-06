package com.tushar.goscanner.repository

import java.io.File

class ImageRepository {

     fun loadImages(path : File): Array<File>? {

        return path.listFiles()
    }
}