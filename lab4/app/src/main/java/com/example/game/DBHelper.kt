package com.example.game

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList
import java.util.HashMap


class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    private val hp: HashMap<*, *>? = null
    override fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub
        db.execSQL(
            "create table contacts " +
                    "(id integer primary key, name text,phone text,email text, street text,place text)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts")
        onCreate(db)
    }

    fun insertContact(
        name: String?,
        phone: String?,
        email: String?,
        street: String?,
        place: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("phone", phone)
        contentValues.put("email", email)
        contentValues.put("street", street)
        contentValues.put("place", place)
        db.insert("contacts", null, contentValues)
        return true
    }

    fun getData(id: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("select * from contacts where id=$id", null)
    }

    fun numberOfRows(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME).toInt()
    }

    fun updateContact(
        id: Int?,
        name: String?,
        phone: String?,
        email: String?,
        street: String?,
        place: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("phone", phone)
        contentValues.put("email", email)
        contentValues.put("street", street)
        contentValues.put("place", place)
        db.update(
            "contacts", contentValues, "id = ? ", arrayOf(
                (id!!).toString()
            )
        )
        return true
    }

    fun deleteContact(id: Int?): Int {
        val db = this.writableDatabase
        return db.delete(
            "contacts",
            "id = ? ", arrayOf((id!!).toString())
        )
    }

    //hp = new HashMap();
    val allCotacts: ArrayList<String>
        get() {
            val array_list = ArrayList<String>()

            //hp = new HashMap();
            val db = this.readableDatabase
            val res = db.rawQuery("select * from contacts", null)
            res.moveToFirst()
            while (!res.isAfterLast) {
                array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)))
                res.moveToNext()
            }
            return array_list
        }

    companion object {
        const val DATABASE_NAME = "MyDBName.db"
        const val CONTACTS_TABLE_NAME = "contacts"
        const val CONTACTS_COLUMN_ID = "id"
        const val CONTACTS_COLUMN_NAME = "name"
        const val CONTACTS_COLUMN_EMAIL = "email"
        const val CONTACTS_COLUMN_STREET = "street"
        const val CONTACTS_COLUMN_CITY = "place"
        const val CONTACTS_COLUMN_PHONE = "phone"
    }
}