package com.example.game

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                SCORE_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addScore(name : String, score : String ){
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(SCORE_COL, score)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getScores(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getByName(name: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT $NAME_COl FROM $TABLE_NAME WHERE $NAME_COl = '$name'", null)
    }

    companion object{
        private val DATABASE_NAME = "UserScores.db"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "scores"
        val ID_COL = "id"
        val NAME_COl = "name"
        val SCORE_COL = "score"
    }
}
