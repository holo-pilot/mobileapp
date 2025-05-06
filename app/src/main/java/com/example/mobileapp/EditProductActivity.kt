package com.example.mobileapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditProductActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var nameField: EditText
    private lateinit var descField: EditText
    private lateinit var quantField: EditText
    private lateinit var saveBtn: Button

    private lateinit var barcode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        nameField = findViewById(R.id.nameField)
        descField = findViewById(R.id.descField)
        quantField = findViewById(R.id.quantField)
        saveBtn = findViewById(R.id.saveBtn)

        barcode = intent.getStringExtra("barcode") ?: ""

        loadProduct()

        saveBtn.setOnClickListener {
            saveProduct()
        }
    }

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
                Toast.makeText(this, "Failed to load product", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProduct() {
        val name = nameField.text.toString().trim()
        val desc = descField.text.toString().trim()
        val quantity = quantField.text.toString().toDoubleOrNull()

        if (name.isBlank() || quantity == null) {
            Toast.makeText(this, "Please enter valid name and price", Toast.LENGTH_SHORT).show()
            return
        }

        val data = mapOf(
            "Name" to name,
            "Description" to desc,
            "Quantity" to quantity
        )

        db.collection("products").document(barcode)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
    }
}
