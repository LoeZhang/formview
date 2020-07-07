package com.loe.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        formView1.setOnClickListener()
        {
            formView1.name = null
            formView1.value = null
            Toast.makeText(this, "选择", Toast.LENGTH_SHORT).show()
            formView3.text = "${formView1.text} - ${formView1.value}"
        }
    }
}