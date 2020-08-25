package com.example.kotlinbitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.InputStream

class GetImageFromURL(var image: ImageView): AsyncTask<String, Void, Bitmap>() {
    lateinit var bitmap: Bitmap

    override fun doInBackground(vararg usl: String?): Bitmap {
        val urldispley = usl[0]
        try {
            val srt: InputStream = java.net.URL(urldispley).openStream()
            bitmap = BitmapFactory.decodeStream(srt)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return bitmap
    }

    override fun(url: String){

    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        image.setImageBitmap(result)
    }
}