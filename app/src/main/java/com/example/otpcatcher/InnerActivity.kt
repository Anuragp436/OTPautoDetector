package com.example.otpcatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)
    }
}