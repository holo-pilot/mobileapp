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



class barcodeadd : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var barcodeField: EditText
    private lateinit var nameField: EditText
    private lateinit var descField: EditText
    private lateinit var quantField: EditText
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        barcodeField = findViewById(R.id.barcodeField)
        nameField = findViewById(R.id.nameField)
        descField = findViewById(R.id.descField)
        quantField = findViewById(R.id.quantField)
        saveBtn = findViewById(R.id.saveBtn)

        val defaultName = intent.getStringExtra("defaultName")
        barcodeField.setText(defaultName)

        saveBtn.setOnClickListener {
            saveProduct()
        }
    }

    private fun saveProduct() {
        val barcode = barcodeField.text.toString().trim()
        val bacode = barcodeField.text.toString().trim()
        val name = nameField.text.toString().trim()
        val desc = descField.text.toString().trim()
        val quantity = quantField.text.toString().toDoubleOrNull()

        if (name.isBlank() || quantity == null) {
            Toast.makeText(this, "Please enter valid name and price", Toast.LENGTH_SHORT).show()
            return
        }

        val newDoc = db.collection("products").document("$barcode")
        val product = mapOf(
            "Barcode" to bacode,
            "Name" to name,
            "Description" to desc,
            "Quantity" to quantity
        )

        newDoc.set(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product created", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Creation failed", Toast.LENGTH_SHORT).show()
            }
    }


}