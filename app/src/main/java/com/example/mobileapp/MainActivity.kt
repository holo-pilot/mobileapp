package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inventory_button = findViewById<Button>(R.id.inventory)
        inventory_button.setOnClickListener {
            val inventory_intent = Intent(this,inventory::class.java)
            startActivity(inventory_intent)
        }

        val scan_button = findViewById<Button>(R.id.scan)
        scan_button.setOnClickListener {
            val scanbtn_intent = Intent(this,qrscan::class.java)
            startActivity(scanbtn_intent)
        }
    }
}