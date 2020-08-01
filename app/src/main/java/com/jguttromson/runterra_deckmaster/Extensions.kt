package com.jguttromson.runterra_deckmaster

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream


fun loadData(inFile: String, resources: Resources): String {
    var tContents: String = ""
    try {
        val stream: InputStream = resources.assets.open(inFile)
        val size: Int = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        tContents = String(buffer)
    } catch (e: IOException) {
        // Handle exceptions here
    }

    return tContents
}

fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
    val assetManager: AssetManager = context.assets
    val istr: InputStream
    var bitmap: Bitmap? = null
    try {
        istr = assetManager.open(filePath)
        bitmap = BitmapFactory.decodeStream(istr)
    } catch (e: IOException) {
        // handle exception
    }
    return bitmap
}