package cr.ac.utn.appmovil.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream

const val EXTRA_MESSAGE_CONTACTID = "cr.ac.utn.appmovil.ContactId"

class util {
    companion object  {
        fun openActivity(context: Context, objclass: Class<*>, extraName: String, value: String?){
            val intent = Intent(context, objclass).apply { putExtra(extraName, value)}
            startActivity(context, intent, null)
        }

        fun convertToByteArray(bitmap: Bitmap):ByteArray?{
            val outputStrem = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStrem)
            return outputStrem.toByteArray()
        }
    }
}