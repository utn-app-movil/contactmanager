package Util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream

const val EXTRA_MESSAGE_CONTACT_ID = "cr.ac.utn.contactmanager.contactId"

class util {
    companion object {
        fun openActivity(
            context: Context, objClass: Class<*>,
            keyName: String = "", value: String? = ""
        ) {
            val intent = Intent(context, objClass).apply { putExtra(keyName, value) }
            startActivity(context, intent, null)
        }


        fun convertToByteArray(bitmap: Bitmap): ByteArray? {
            val outputStrem = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStrem)
            return outputStrem.toByteArray()
        }
    }
}

