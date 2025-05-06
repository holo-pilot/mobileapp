package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class add_product : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val productNameEditText = findViewById<EditText>(R.id.productNameEditText)
        val productDescriptionEditText = findViewById<EditText>(R.id.productDescriptionEditText)
        val productQuantityEditText = findViewById<EditText>(R.id.productQuantityEditText)
        val addProductButton = findViewById<Button>(R.id.addProductButton)

        val barcode = intent.getStringExtra("Barcode")

        addProductButton.setOnClickListener {
            val name = productNameEditText.text.toString()
            val description = productDescriptionEditText.text.toString()
            val quantity = productQuantityEditText.text.toString().toIntOrNull() ?: 0

            val resultIntent = Intent()
            resultIntent.putExtra("Barcode", barcode)
            resultIntent.putExtra("Name", name)
            resultIntent.putExtra("Description", description)
            resultIntent.putExtra("Quantity", quantity)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}