package cr.ac.utn.appmovil.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import android.graphics.Bitmap.CompressFormat

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


const val EXTRA_MESSAGE_CONTACTID = "cr.ac.utn.appmovil.ContactId"

class util {
    companion object  {
        fun openActivity(context: Context, objclass: Class<*>, extraName: String, value: String?){
            val intent = Intent(context, objclass).apply { putExtra(extraName, value)}
            startActivity(context, intent, null)
        }

        fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray? {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.PNG, 0, outputStream)
            return outputStream.toByteArray()
        }
    }
}