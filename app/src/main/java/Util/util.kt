package Util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

const val EXTRA_MESSAGE_CONTACT_ID = "cr.ac.utn.contactmanager.contactId"

class util {
    companion object{
        fun openActivity(context: Context, objClass: Class<*>,
                        keyName: String="", value: String?=""){
            val intent = Intent(context, objClass).apply { putExtra(keyName, value) }
            startActivity(context, intent, null)
        }
    }
}