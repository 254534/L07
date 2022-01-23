package com.example.l07

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

import android.widget.AdapterView.OnItemClickListener





class RecordVideoActivity : AppCompatActivity() {
    val REQUEST_VIDEO_CAPTURE = 1
    val REQUEST_READ_STORAGE = 2

    lateinit var arrayAdapter : ArrayAdapter<String>
    lateinit var listTitles: MutableList<String>
    lateinit var listIds: MutableList<String>
    lateinit var listView: ListView

    fun getAll() {
        listTitles = mutableListOf<String>()
        listIds = mutableListOf<String>()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val contentResolver = contentResolver
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        if (cursor == null) {
            // handle error code
        } else if (!cursor.moveToFirst()) {
            // handle no media case
        } else {
            val idColumn: Int = cursor.getColumnIndex(MediaStore.Video.Media._ID)
            val titleColumn: Int = cursor.getColumnIndex(MediaStore.Video.Media.TITLE)
            do {
                val itemId: Long = cursor.getLong(idColumn)
                val itemTitle: String = cursor.getString(titleColumn)
                listIds.add("$itemId")
                listTitles.add("$itemTitle")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_video)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            listView = findViewById<ListView>(R.id.listView)
            getAll()

            arrayAdapter =
                ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listTitles)
            listView.adapter = arrayAdapter

            listView.setOnItemClickListener(OnItemClickListener { arg0, arg1, position, arg3 ->
                val intent = Intent(this, PlayVideoActivity::class.java)
                var bundle = Bundle()
                bundle.putString("title", listIds[position])
                intent.putExtras(bundle)
                startActivity(intent)
            })

        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORAGE)
        }


        findViewById<Button>(R.id.buttonRecordVideo).setOnClickListener {
            var intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            intent.resolveActivity(packageManager)
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)

//            getAll()
//            arrayAdapter =
//                ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listStr)
//            listView.adapter = arrayAdapter
        }
    }
}