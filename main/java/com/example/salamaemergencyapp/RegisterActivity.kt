package com.example.salamaemergencyapp


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class Register : AppCompatActivity() {
    lateinit var b1: Button
    lateinit var b2: Button
    lateinit var b3: Button
    lateinit var e1: EditText
    lateinit var listView: ListView
    lateinit var s1: SQLiteOpenHelper
    lateinit var sqLitedb: SQLiteDatabase
    lateinit var myDB: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        e1 = findViewById(R.id.phone)
        b1 = findViewById(R.id.add)
        b2 = findViewById(R.id.delete)
        b3 = findViewById(R.id.view)
        myDB = DatabaseHandler(this)
        listView = findViewById(R.id.list)


        b1.setOnClickListener {
            val sr: String = getText().toString()
                addData(sr)
                Toast.makeText(this@Register, "Data Added", Toast.LENGTH_SHORT).show()
                e1.setText("")
            }

        b2.setOnClickListener{
                sqLitedb = myDB.getWritableDatabase()
                val x: String = e1.getText().toString()
                DeleteData(x)
                Toast.makeText(this@Register, "Data Deleted", Toast.LENGTH_SHORT).show()
            }

        b3.setOnClickListener{
                loadData()
            }

    }

    private fun getText(): CharSequence {
        TODO("Not yet implemented")
    }

    private fun loadData() {
        val theList: ArrayList<String> = ArrayList()
        val data: Cursor = myDB.getListContents()
        if (data.getCount() === 0) {
            Toast.makeText(this@Register, "There is no content", Toast.LENGTH_SHORT).show()
        } else {
            while (data.moveToNext()) {
                theList.add(data.getString(1))
                val listAdapter: ListAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, theList)
                listView.setAdapter(listAdapter)
            }
        }
    }

    private fun DeleteData(x: String): Boolean {
        return sqLitedb.delete(
            DatabaseHandler.TABLE_NAME,
            DatabaseHandler.COL2 + "=?",
            arrayOf<String>(x)
        ) > 0
    }

    private fun addData(newEntry: String) {
        val insertData: Boolean = myDB.addData(newEntry)
        if (insertData == true) {
            Toast.makeText(this@Register, "Data Added..", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@Register, "Unsuccessful", Toast.LENGTH_SHORT).show()
        }
    }
}