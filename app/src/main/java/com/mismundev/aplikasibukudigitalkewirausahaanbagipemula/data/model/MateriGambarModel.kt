package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MateriGambarModel (
    @SerializedName("id_materi_gambar")
    var id_materi_gambar: String? = null,

    @SerializedName("id_materi")
    var id_materi: String? = null,

    @SerializedName("gambar")
    var gambar: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null,
)