package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Main activity class for the mobile app
class MainActivity : AppCompatActivity() {

    // Entry point when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables drawing edge-to-edge on the screen
        enableEdgeToEdge()

        // Sets the layout resource to use for this activity
        setContentView(R.layout.activity_main)

        // Applies proper padding to account for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets unmodified
        }

        // Finds the "Inventory" button in the layout
        val inventory_button = findViewById<Button>(R.id.inventory)

        // Sets a click listener to open the Inventory screen when clicked
        inventory_button.setOnClickListener {
            val inventory_intent = Intent(this, inventory::class.java)
            startActivity(inventory_intent) // Starts the Inventory activity
        }

        // Finds the "Scan" button in the layout
        val scan_button = findViewById<Button>(R.id.scan)

        // Sets a click listener to open the QR Scan screen when clicked
        scan_button.setOnClickListener {
            val scanbtn_intent = Intent(this, qrscan::class.java)
            startActivity(scanbtn_intent) // Starts the QR Scan activity
        }
    }
}
