package com.example.mobileapp


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class inventory : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var inputSearch: EditText
    private lateinit var txtDetails: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private var currentBarcode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        db = Firebase.firestore

        inputSearch = findViewById(R.id.txtSearch)
        txtDetails = findViewById(R.id.searchTV)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        findViewById<Button>(R.id.btnSearch).setOnClickListener {
            val queryText = inputSearch.text.toString().trim()
            searchProduct(queryText)
        }

        btnUpdate.setOnClickListener {
            currentBarcode?.let {
                val intent = Intent(this, EditProductActivity::class.java)
                intent.putExtra("barcode", it)
                startActivity(intent)
            }
        }

        btnDelete.setOnClickListener {
            currentBarcode?.let {
                confirmDelete(it)
            }
        }
    }

    private fun searchProduct(query: String) {
        if (query.isBlank()) return

        db.collection("products")
            .whereGreaterThanOrEqualTo("Barcode", query)
            .whereLessThanOrEqualTo("Barcode", query + '\uf8ff')
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val doc = result.documents[0]
                    val bacode = doc.getString("Barcode")
                    val name = doc.getString("Name")
                    val desc = doc.getString("Description")
                    val quantity = doc.getDouble("Quantity")

                    txtDetails.text = "Name: $name\nBarcode: $bacode\nDescription: $desc\nQuantity: $quantity"
                    currentBarcode = doc.id
                    btnUpdate.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                } else {
                    txtDetails.text = "No product found"
                    btnUpdate.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE

                    AlertDialog.Builder(this)
                        .setTitle("Product not found")
                        .setMessage("Would you like to create it?")
                        .setPositiveButton("Yes") { _, _ ->
                            val intent = Intent(this, CreateProductActivity::class.java)
                            intent.putExtra("defaultName", query)
                            startActivity(intent)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
            .addOnFailureListener {
                txtDetails.text = "Error searching product"
            }
    }

    private fun confirmDelete(barcode: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                db.collection("products").document(barcode)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                        txtDetails.text = ""
                        btnUpdate.visibility = View.GONE
                        btnDelete.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
