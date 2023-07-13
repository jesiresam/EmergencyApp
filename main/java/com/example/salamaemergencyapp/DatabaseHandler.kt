package com.example.salamaemergencyapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

class DatabaseHandler(@Nullable context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "ITEM1 TEXT)"
        db.execSQL(createTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val a = "DROP TABLE IF EXISTS " + TABLE_NAME
        db.execSQL(a)
        onCreate(db)
    }

    fun addData(item1: String?): Boolean {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val contentValues = ContentValues()
        contentValues.put(COL2, item1)
        val result: Long = db.insert(TABLE_NAME, null, contentValues)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    internal fun getListContents(): Cursor {
        TODO("Not yet implemented")
    }

    val listContents: Cursor
        get() {
            val db: SQLiteDatabase = this.getWritableDatabase()
            return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        }

    companion object {
        const val DATABASE_NAME = "mylist.db"
        const val TABLE_NAME = "mylist_data"
        const val COL1 = "ID"
        const val COL2 = "ITEM1"
    }}
