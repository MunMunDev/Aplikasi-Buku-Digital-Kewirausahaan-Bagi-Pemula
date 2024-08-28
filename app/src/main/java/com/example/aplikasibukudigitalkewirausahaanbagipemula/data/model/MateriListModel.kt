package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MateriListModel (
    @SerializedName("id_list_materi")
    var idListMateri: String? = null,

    @SerializedName("judul")
    var judul: String? = null,

    @SerializedName("gambar")
    var gambar: String? = null,

    @SerializedName("waktu")
    var waktu: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idListMateri)
        parcel.writeString(judul)
        parcel.writeString(gambar)
        parcel.writeString(waktu)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MateriListModel> {
        override fun createFromParcel(parcel: Parcel): MateriListModel {
            return MateriListModel(parcel)
        }

        override fun newArray(size: Int): Array<MateriListModel?> {
            return arrayOfNulls(size)
        }
    }
}