package com.example.mobileapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Activity for editing an existing product's details
class EditProductActivity : AppCompatActivity() {

    // Reference to Firestore database
    private val db = Firebase.firestore

    // UI elements
    private lateinit var nameField: EditText
    private lateinit var descField: EditText
    private lateinit var quantField: EditText
    private lateinit var saveBtn: Button

    // Holds the barcode of the product being edited
    private lateinit var barcode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        // Bind UI components to layout
        nameField = findViewById(R.id.nameField)
        descField = findViewById(R.id.descField)
        quantField = findViewById(R.id.quantField)
        saveBtn = findViewById(R.id.saveBtn)

        // Retrieve barcode passed from intent
        barcode = intent.getStringExtra("barcode") ?: ""

        // Load existing product data into fields
        loadProduct()

        // Set listener to save product updates
        saveBtn.setOnClickListener {
            saveProduct()
        }
    }

    // Fetches product details from Firestore and populates input fields
    private fun loadProduct() {
        db.collection("products").document(barcode)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    nameField.setText(doc.getString("Name"))
                    descField.setText(doc.getString("Description"))
                    quantField.setText(doc.getDouble("Quantity")?.toString() ?: "")
                }
            }
            .addOnFailureListener {
                // Show error message if loading fails
                Toast.makeText(this, "Failed to load product", Toast.LENGTH_SHORT).show()
            }
    }

    // Validates and updates product information in Firestore
    private fun saveProduct() {
        val name = nameField.text.toString().trim()
        val desc = descField.text.toString().trim()
        val quantity = quantField.text.toString().toDoubleOrNull()

        // Check for valid input
        if (name.isBlank() || quantity == null) {
            Toast.makeText(this, "Please enter valid name and price", Toast.LENGTH_SHORT).show()
            return
        }

        // Create data map for updating Firestore document
        val data = mapOf(
            "Name" to name,
            "Description" to desc,
            "Quantity" to quantity
        )

        // Update the document in Firestore
        db.collection("products").document(barcode)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show()
                finish() // Close the activity on success
            }
            .addOnFailureListener {
                // Show error message if update fails
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
    }
}
