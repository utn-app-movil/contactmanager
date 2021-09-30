package cr.ac.utn.appmovil.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

class util {
    companion object  {
        fun openActivity(context: Context, objclass: Class<*>){
            val intent = Intent(context, objclass)
            startActivity(context, intent, null)
        }
    }
}