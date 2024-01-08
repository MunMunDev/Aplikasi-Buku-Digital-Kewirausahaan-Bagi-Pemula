package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model

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
)