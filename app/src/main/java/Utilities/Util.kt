package Utilities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream

const val EXTRA_MESSAGE_CONTACTID = "cr.ac.utn.appmovil.ContactId"

class util {
    companion object{
        fun openActivity(context: Context, objClass: Class<*>, keyExtra: String, value: String){
            val intent = Intent(context, objClass).apply { putExtra(keyExtra, value) }
            startActivity(context, intent, null)
        }

        fun convertToByteArray(bitmap: Bitmap):ByteArray?{
            val outputStrem = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStrem)
            return outputStrem.toByteArray()
        }
    }
}