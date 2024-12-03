package com.iqbalgbv.myfirebaseexample

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ListAdapter(private val activity: Activity, private val context: Context, private var user: ArrayList<User>): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private lateinit var dialog: Dialog

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val brand: TextView
        val tipe: TextView
        val edit: Button
        val hapus: Button

        init {
            brand = itemView.findViewById(R.id.brand)
            tipe = itemView.findViewById(R.id.tipe)
            edit = itemView.findViewById(R.id.edit)
            hapus = itemView.findViewById(R.id.hapus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = user[position]
        holder.brand.text = currentItem.brand
        holder.tipe.text = currentItem.tipe
        holder.edit.setOnClickListener {
            dialog = Dialog(context)
            dialog.setContentView(R.layout.update_dialog)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val batal = dialog.findViewById<Button>(R.id.batal)
            val update = dialog.findViewById<Button>(R.id.update)
            val tipe = dialog.findViewById<EditText>(R.id.tipe)
            val brand = dialog.findViewById<EditText>(R.id.brand)
            tipe.setText(holder.tipe.text)
            brand.setText(holder.tipe.text)
            dialog.show()

            update.setOnClickListener {
                val ref = FirebaseDatabase.getInstance("https://tesfirebase-13d31-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("user")
                if (tipe.text.toString().isEmpty()) {
                    tipe.error = "Masukkan tipe"
                    tipe.requestFocus()
                }

                if (brand.text.toString().isEmpty()) {
                    brand.error = "Masukkan nama brand"
                    brand.requestFocus()
                }

                val changeUser = User(currentItem.id,tipe.text.toString(),brand.text.toString())
                ref.child(currentItem.id!!).setValue(changeUser)
                Toast.makeText(context, "Data Berhasil di Update", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            batal.setOnClickListener {
                dialog.dismiss()
            }
        }
        holder.hapus.setOnClickListener {
            val ref = FirebaseDatabase.getInstance("https://tesfirebase-13d31-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("user")
            ref.child(currentItem.id!!).removeValue()
            Toast.makeText(context, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show()
        }
    }
}