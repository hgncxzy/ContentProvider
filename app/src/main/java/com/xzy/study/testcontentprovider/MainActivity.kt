package com.xzy.study.testcontentprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues
import android.widget.Button
import com.xzy.study.testcontentprovider.Provider.Companion.CMD_CONTROL_LANGUAGE


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_change_language).setOnClickListener {
            val values = ContentValues()
            values.put("username", "dicky")
            values.put("password", "123456")
            contentResolver.insert(Provider.CONTENT_URI, values)
        }
    }
}