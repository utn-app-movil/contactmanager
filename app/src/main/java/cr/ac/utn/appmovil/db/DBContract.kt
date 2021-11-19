package cr.ac.utn.appmovil.db

import android.graphics.Bitmap
import android.provider.BaseColumns

object DBContract {
    class ContactEntry: BaseColumns{
        companion object{
            val TABLE_NAME="contacts"
            val COLUMN_ID="id"
            val COLUMN_NAME ="name"
            val COLUMN_LASTNAME="lastName"
            val COLUMN_PHONE="phone"
            val COLUMN_EMAIL="email"
            val COLUMN_ADDRESS="address"
            val COLUMN_PHOTO="photo"
        }
    }
}