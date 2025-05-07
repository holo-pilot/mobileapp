package com.example.mobileapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Activity for adding a new product after scanning a barcode
class barcodeadd : AppCompatActivity() {

    // Reference to Firestore database
    private val db = Firebase.firestore

    // UI components for user input
    private lateinit var barcodeField: EditText
    private lateinit var nameField: EditText
    private lateinit var descField: EditText
    private lateinit var quantField: EditText
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge display layout
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_product)

        // Applies proper padding to account for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind layout views to variables
        barcodeField = findViewById(R.id.barcodeField)
        nameField = findViewById(R.id.nameField)
        descField = findViewById(R.id.descField)
        quantField = findViewById(R.id.quantField)
        saveBtn = findViewById(R.id.saveBtn)

        // Pre-fill barcode field with value passed via intent
        val defaultName = intent.getStringExtra("defaultName")
        barcodeField.setText(defaultName)

        // Set listener to save product when button is clicked
        saveBtn.setOnClickListener {
            saveProduct()
        }
    }

    // Saves product information to Firestore
    private fun saveProduct() {
        // Get values from input fields
        val barcode = barcodeField.text.toString().trim()
        val bacode = barcodeField.text.toString().trim()  // Duplicate of `barcode` to fix barcode field not creating in Firestore
        val name = nameField.text.toString().trim()
        val desc = descField.text.toString().trim()
        val quantity = quantField.text.toString().toDoubleOrNull()

        // Validate required fields
        if (name.isBlank() || quantity == null) {
            Toast.makeText(this, "Please enter valid name and price", Toast.LENGTH_SHORT).show()
            return
        }

        // Reference to the Firestore document using the barcode
        val newDoc = db.collection("products").document("$barcode")

        // Create product data map
        val product = mapOf(
            "Barcode" to bacode,
            "Name" to name,
            "Description" to desc,
            "Quantity" to quantity
        )

        // Save the product to Firestore
        newDoc.set(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product created", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                // Show error if creation fails
                Toast.makeText(this, "Creation failed", Toast.LENGTH_SHORT).show()
            }
    }
}
