package com.iqbalgbv.myfirebaseexample

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.iqbalgbv.myfirebaseexample.R.id.tipe

class MainActivity : AppCompatActivity() {

    private lateinit var brand: EditText
    private lateinit var tipe: EditText
    private lateinit var tambah: Button
    private lateinit var ref: DatabaseReference
    private lateinit var userList: ArrayList<User>
    private lateinit var list_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        brand = findViewById(R.id.brand)
        tipe = findViewById(R.id.tipe)
        tambah = findViewById(R.id.tambah)
        list_view = findViewById(R.id.list_view)
        list_view.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        list_view.layoutManager = layoutManager

        val database = Firebase.database
        val myRef = database.getReference("data")

        myRef.setValue("Hello, World!")

        ref =
            FirebaseDatabase.getInstance("https://my-firebase-example-36638-default-rtdb.firebaseio.com/")
                .getReference("user")

        userList = arrayListOf<User>()

        getData()
        tambah.setOnClickListener {
            userList.clear()
            tambahData()
            brand.text.clear()
            tipe.text.clear()
        }
    }

    private fun tambahData() {
        val sbrand = brand.text.toString().trim()
        val stipe = tipe.text.toString().trim()

        if (sbrand.isEmpty()) {
            brand.error = "Masukkan nama breed"
        }

        if (stipe.isEmpty()) {
            tipe.error = "Masukkan harga"
        }

        val ref =
            FirebaseDatabase.getInstance("https://my-firebase-example-36638-default-rtdb.firebaseio.com/")
                .getReference("user")

        val userid = ref.push().key

        val user = User(userid, sbrand, stipe)

        if (userid != null) {
            ref.child(userid).setValue(user).addOnCompleteListener {
                Log.d("sbrand", "Data Berhasil Ditambah")
                Toast.makeText(this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getData() {
        val addValueEventListener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList = arrayListOf<User>()
                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        val user = h.getValue(User::class.java)
                        userList.add(user!!)
                    }
                    Toast.makeText(applicationContext, "Server Berhasil", Toast.LENGTH_SHORT).show()
                    val adapter = ListAdapter(this@MainActivity, this@MainActivity, userList)
                    list_view.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Server Gagal", Toast.LENGTH_SHORT).show()
            }


        })

    }
}