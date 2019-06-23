package com.psky.fake_sms.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.CursorLoader
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.psky.fake_sms.service.SingletonService

class RealPathUtils {
    companion object {
        val shared = SingletonService.get(RealPathUtils::class)
    }

    /**
     * API > 19
     */
    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String {
        var filePath = ""
        val wholeID = DocumentsContract.getDocumentId(uri)
        val id = wholeID.split(":")[1]


        val column = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null
        )

        val columnIndex = cursor!!.getColumnIndex(column[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    /**
     * API >= 11  and API <= 18
     */
    @SuppressLint("NewApi")
    fun getRealPathFromURI_API11to18(context: Context, contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        val cursorLoader = CursorLoader(
            context,
            contentUri, proj, null, null, null
        )
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(column_index)
        }
        return result ?: ""
    }

    /**
     * API < 11
     */
    fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}