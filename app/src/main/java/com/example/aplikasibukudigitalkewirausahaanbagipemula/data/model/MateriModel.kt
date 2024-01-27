package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MateriModel (
    @SerializedName("no_materi")
    var noMateri: String? = null,

    @SerializedName("id_materi")
    var idMateri: String? = null,

    @SerializedName("nama_materi")
    var namaMateri: String? = null,

    @SerializedName("nama_penulis")
    var namaPenulis: String? = null,

    @SerializedName("lokasi_file")
    var lokasiFile: String? = null,

    @SerializedName("url_image")
    var urlImage: String? = null,

    @SerializedName("url_materi")
    var urlMateri: String? = null,

    @SerializedName("jumlah_pelihat")
    var jumlahPelihat: String? = null,

    @SerializedName("tanggal_upload")
    var tanggalUpload: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(noMateri)
        parcel.writeString(idMateri)
        parcel.writeString(namaMateri)
        parcel.writeString(namaPenulis)
        parcel.writeString(lokasiFile)
        parcel.writeString(urlImage)
        parcel.writeString(urlMateri)
        parcel.writeString(jumlahPelihat)
        parcel.writeString(tanggalUpload)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MateriModel> {
        override fun createFromParcel(parcel: Parcel): MateriModel {
            return MateriModel(parcel)
        }

        override fun newArray(size: Int): Array<MateriModel?> {
            return arrayOfNulls(size)
        }
    }
}