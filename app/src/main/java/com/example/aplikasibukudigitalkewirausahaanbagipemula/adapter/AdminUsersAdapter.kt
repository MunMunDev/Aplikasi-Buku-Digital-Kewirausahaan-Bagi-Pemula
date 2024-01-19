package com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListAdminUsersBinding

class AdminUsersAdapter(var arrayUser: ArrayList<UsersModel>, val listener: ClickItemListener): RecyclerView.Adapter<AdminUsersAdapter.AdminUsersViewHolder>() {

    class AdminUsersViewHolder(val binding: ListAdminUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsersViewHolder {
        val binding = ListAdminUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(arrayUser.size > 0){
            arrayUser.size
        } else{
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdminUsersViewHolder, position: Int) {
        val data = arrayUser[position]
        holder.binding.apply {
            tvNama.text = data.nama
            tvNomorHp.text = data.nomorHp

            clBody.setOnClickListener {
                listener.onClickItem(data, it, position)
            }
        }
    }

    interface ClickItemListener{
        fun onClickItem(user: UsersModel, it:View, position: Int)
    }
}