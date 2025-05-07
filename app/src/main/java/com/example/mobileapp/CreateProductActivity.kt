package com.example.mobileapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Activity for creating a new product in the Firestore database
class CreateProductActivity : AppCompatActivity() {

    // Reference to Firestore database
    private val db = Firebase.firestore

    // UI components
    private lateinit var barcodeField: EditText
    private lateinit var nameField: EditText
    private lateinit var descField: EditText
    private lateinit var quantField: EditText
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        // Bind UI components to layout
        barcodeField = findViewById(R.id.barcodeField)
        nameField = findViewById(R.id.nameField)
        descField = findViewById(R.id.descField)
        quantField = findViewById(R.id.quantField)
        saveBtn = findViewById(R.id.saveBtn)

        // Pre-fill the barcode field if a barcode was passed in the intent
        val defaultName = intent.getStringExtra("defaultName")
        barcodeField.setText(defaultName)

        // Set click listener to run saveProduct() when the save button is clicked
        saveBtn.setOnClickListener {
            saveProduct()
        }
    }

    // Collects input, validates data, and saves new product to Firestore
    private fun saveProduct() {
        val barcode = barcodeField.text.toString().trim()
        val name = nameField.text.toString().trim()
        val desc = descField.text.toString().trim()
        val quantity = quantField.text.toString().toDoubleOrNull()

        // Ensure required fields are properly filled
        if (name.isBlank() || quantity == null) {
            Toast.makeText(this, "Please enter valid name and price", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Firestore document reference using the barcode
        val newDoc = db.collection("products").document("$barcode")

        // Prepare product data to store
        val product = mapOf(
            "Name" to name,
            "Barcode" to barcode,
            "Description" to desc,
            "Quantity" to quantity
        )

        // Save product to Firestore
        newDoc.set(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product created", Toast.LENGTH_SHORT).show()
                finish() // Close activity after successful creation
            }
            .addOnFailureListener {
                // Show error if creation fails
                Toast.makeText(this, "Creation failed", Toast.LENGTH_SHORT).show()
            }
    }
}
