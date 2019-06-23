package com.psky.fake_sms.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import com.psky.fake_sms.service.SingletonService
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * Create by AnhPQ in 04/05/2019
 */
class Utils {
    companion object {
        val shared = SingletonService.get(Utils::class)
    }

    fun formatDuration(time: Long): String {
        val time: Long = Math.round((time / 1000L).toDouble())
        if (time > 99 || time < 0) {
            return "00:00:00"
        }
        if (time >= 60) {
            return "00:01:${time - 60}"
        }
        if (time < 10) {
            return "00:00:0$time"
        }
        return "00:00:$time"
    }

    /**
     *
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDateToString(timeLong: Long): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(Date(timeLong))
    }

    /**
     *
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDateToStringShowMessages(timeLong: Long): String {
        val date = SimpleDateFormat("MM/dd/yy").format(Date(timeLong))
        val time = SimpleDateFormat("HH:mm").format(Date(timeLong))

        val current = SimpleDateFormat("MM/dd/yy").format(Date(System.currentTimeMillis()))

        if (date.equals(current)) {
            return "Today $time"
        }
        return date
    }

    /**
     *
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDateToStringShowMessenger(timeLong: Long): String {
        val date = SimpleDateFormat("MM/dd/yy").format(Date(timeLong))
        val time = SimpleDateFormat("HH:mm").format(Date(timeLong))

        val current = SimpleDateFormat("MM/dd/yy").format(Date(System.currentTimeMillis()))

        if (date.equals(current)) {
            val hour = time.substring(0, time.indexOf(":")).toInt
            if (hour > 12) {
                return "$time PM"
            } else {
                return "$time AM"
            }
        }
        return date
    }

    /**
     * Used to check the time between two messages
     * @return true when time2 is 5 minutes bigger than time1 else return false
     */
    fun checkTimeAddHeader(time1: String, time2: String): Boolean {
        return true
    }

    @SuppressLint("ObsoleteSdkInt")
    fun getPathFromUri(context: Context, uri: Uri): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return RealPathUtils.shared.getRealPathFromURI_BelowAPI11(context, uri)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return RealPathUtils.shared.getRealPathFromURI_API11to18(context, uri)
        } else {
            return RealPathUtils.shared.getRealPathFromURI_API19(context, uri)
        }
    }

    /**
     *  return true if folder fakeSMS
     */
    fun existsFakeSMS(context: Context): Boolean {
        try {
            val fakeSMSFolder = File(context.externalCacheDir.toString() + File.separator + "fake_sms")
            val nomedia = File(fakeSMSFolder.absolutePath + "/.nomedia")
            if (!fakeSMSFolder.exists()) {
                return fakeSMSFolder.mkdir() && nomedia.createNewFile()
            } else {
                if (!nomedia.exists()) {
                    return nomedia.createNewFile()
                }
            }
        } catch (e: Exception) {
            Log.e("TAGS", e.toString())
        }
        return true
    }

    /**
     *
     */
    @Throws(IOException::class)
    fun createFileImage(context: Context): File {
        existsFakeSMS(context)
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filePath =
            context.externalCacheDir.toString() + File.separator + "fake_sms" + File.separator + timeStamp + Define.Fields.FORMAT_IMAGE
        val file = File(filePath)
        file.createNewFile()
        return file
    }

    /**
     * Decrease Size Image and save local
     */
    fun handlingImage(srcFile: File, dstFile: File) {
        val fileSizeInBytes = srcFile.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        if (fileSizeInKB >= 300) {
            reduceSizeImageFile(srcFile, dstFile)
        } else {
            copyImage(srcFile, dstFile)
        }
    }

    /**
     * Used to copy image
     */
    private fun copyImage(srcFile: File, dstFile: File) {
        CopyImageAsync().execute(srcFile, dstFile)
    }

    /**
     * Used to resize bitmap images
     */
    private fun reduceSizeImageFile(srcFile: File, dstFile: File) {
        val rotate90 = 90.0f
        val rotate180 = 180.0f
        val rotate270 = 270.0f
        try {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6

            var inputStream = FileInputStream(srcFile)
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size want to scale to
            val REQUIRED_SIZE = 70

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            // rotate
            val matrix = Matrix()
            val exifInterface = ExifInterface(srcFile.absolutePath)
            when (exifInterface.getAttributeInt(
                "Orientation",
                0
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(rotate90)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(rotate180)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(rotate270)
            }
            inputStream = FileInputStream(srcFile)

            var selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            selectedBitmap = Bitmap.createBitmap(
                selectedBitmap!!, 0, 0, selectedBitmap.width,
                selectedBitmap.height, matrix, true
            )
            inputStream.close()

            dstFile.createNewFile()
            val outputStream = FileOutputStream(dstFile)

            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            Log.e("TAGs", "reduceSizeImageFile: ", e)
        }
    }

    private class CopyImageAsync : AsyncTask<File, Unit, Unit>() {
        override fun doInBackground(vararg params: File?): Unit? {
            val src = params[0]
            val dst = params[1]
            try {
                val inStream = FileInputStream(src)
                val out = FileOutputStream(dst)
                val buf = ByteArray(1024)
                var len = inStream.read(buf)
                while (len > 0) {
                    out.write(buf, 0, len)
                    len = inStream.read(buf)
                }
                out.close()
                inStream.close()
            } catch (e: IOException) {
                Log.e("TAGs", e.toString())
            }

            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
        }
    }


}