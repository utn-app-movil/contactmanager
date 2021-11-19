package cr.ac.utn.appmovil.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import org.jetbrains.annotations.Contract

class ContactDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertContact(contact: Contact): Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.ContactEntry.COLUMN_ID, contact.Id)
        values.put(DBContract.ContactEntry.COLUMN_NAME, contact.Name)
        values.put(DBContract.ContactEntry.COLUMN_LASTNAME, contact.LastName)
        values.put(DBContract.ContactEntry.COLUMN_ADDRESS, contact.Address)
        values.put(DBContract.ContactEntry.COLUMN_EMAIL, contact.Email)
        values.put(DBContract.ContactEntry.COLUMN_PHONE, contact.Phone)
        values.put(DBContract.ContactEntry.COLUMN_PHOTO, contact.Photo)

        val newRowId = db.insert(DBContract.ContactEntry.TABLE_NAME, null, values)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun updateContact(contact: Contact): Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.ContactEntry.COLUMN_NAME, contact.Name)
        values.put(DBContract.ContactEntry.COLUMN_LASTNAME, contact.LastName)
        values.put(DBContract.ContactEntry.COLUMN_ADDRESS, contact.Address)
        values.put(DBContract.ContactEntry.COLUMN_EMAIL, contact.Email)
        values.put(DBContract.ContactEntry.COLUMN_PHONE, contact.Phone)
        values.put(DBContract.ContactEntry.COLUMN_PHOTO, contact.Photo)

        val selection = "${DBContract.ContactEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(contact.Id)
        db.update(DBContract.ContactEntry.TABLE_NAME, values, selection, selectionArgs)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteContact(id: String): Boolean{
        val db = writableDatabase
        val selection = "${DBContract.ContactEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id)
        db.delete(DBContract.ContactEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }

    fun readContact(id: String): Contact{
        val contact = Contact()
        val db = writableDatabase
        var cursor: Cursor? = null
        try{
            val str = "SELECT * FROM ${DBContract.ContactEntry.TABLE_NAME} WHERE ${DBContract.ContactEntry.COLUMN_ID} = '$id'"
            cursor = db.rawQuery(str, null)
        }catch (e: SQLiteException){
            db.execSQL(SQL_CREATE_ENTRIES)
            return contact
        }

        var _id: Int
        var _name: Int
        var _lastName: Int
        var _phone: Int = 0
        var _email: Int
        var _address: Int
        var _photo: Int

        if (cursor!!.moveToFirst()){
            while(cursor.isAfterLast == false){
                _id = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ID) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ID) else 0
                _name = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_NAME) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_NAME) else 0
                _lastName = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_LASTNAME) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_LASTNAME) else 0
                _phone = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHONE) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHONE) else 0
                _email = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_EMAIL) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_EMAIL) else 0
                _address = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ADDRESS) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ADDRESS) else 0
                _photo = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHOTO) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHOTO) else 0

                contact.Id = cursor.getString(_id)
                contact.Name = cursor.getString(_name)
                contact.LastName = cursor.getString(_lastName)
                contact.Phone = cursor.getInt(_phone)
                contact.Email = cursor.getString(_email)
                contact.Address = cursor.getString(_address)
                contact.Photo = cursor.getBlob(_photo)

                cursor.moveToNext()
            }
        }
        return contact
    }

    fun readAllContact(): List<Contact>{
        val contacts = ArrayList<Contact>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery("SELECT * FROM ${DBContract.ContactEntry.TABLE_NAME}", null)
        }catch (e: SQLiteException){
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var _id: Int
        var _name: Int
        var _lastName: Int
        var _phone: Int = 0
        var _email: Int
        var _address: Int
        var _photo: Int

        if (cursor!!.moveToFirst()){
            while(cursor.isAfterLast == false){
                _id = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ID) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ID) else 0
                _name = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_NAME) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_NAME) else 0
                _lastName = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_LASTNAME) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_LASTNAME) else 0
                _phone = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHONE) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHONE) else 0
                _email = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_EMAIL) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_EMAIL) else 0
                _address = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ADDRESS) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_ADDRESS) else 0
                _photo = if (cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHOTO) > 0) cursor.getColumnIndex(DBContract.ContactEntry.COLUMN_PHOTO) else 0

                val photo: ByteArray = cursor.getBlob(_photo)

                val contact = Contact(cursor.getString(_id), cursor.getString(_name), cursor.getString(_lastName), cursor.getInt(_phone)
                    , cursor.getString(_email), cursor.getString(_address),  photo)
                contacts.add(contact)
                cursor.moveToNext()
            }
        }
        return contacts.toList()
    }

    companion object{
        val DATABASE_VERSION = 3
        val DATABASE_NAME = "contactManager.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${DBContract.ContactEntry.TABLE_NAME} (" +
                    "${DBContract.ContactEntry.COLUMN_ID} TEXT PRIMARY KEY," +
                    "${DBContract.ContactEntry.COLUMN_NAME} TEXT," +
                    "${DBContract.ContactEntry.COLUMN_LASTNAME} TEXT," +
                    "${DBContract.ContactEntry.COLUMN_EMAIL} TEXT," +
                    "${DBContract.ContactEntry.COLUMN_ADDRESS} TEXT," +
                    "${DBContract.ContactEntry.COLUMN_PHONE} INTEGER," +
                    "${DBContract.ContactEntry.COLUMN_PHOTO} BLOB)"

        private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${DBContract.ContactEntry.TABLE_NAME}"
    }
}