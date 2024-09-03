package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MateriModel (
    @SerializedName("id_materi")
    var idMateri: String? = null,

    @SerializedName("id_list_materi")
    var idListMateri: String? = null,

    @SerializedName("judul")
    var judul: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null,

    @SerializedName("materi_gambar")
    var materi_gambar: ArrayList<MateriGambarModel>? = null,
)