package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListAdminUsersBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem

class AdminUsersAdapter(
    var arrayUser: ArrayList<UsersModel>,
    val listener: OnClickItem.ClickAkun
): RecyclerView.Adapter<AdminUsersAdapter.AdminUsersViewHolder>() {

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
                listener.clickItemAkun(data, it)
            }
        }
    }

}