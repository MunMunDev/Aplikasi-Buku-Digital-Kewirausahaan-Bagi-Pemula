package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model

import com.google.gson.annotations.SerializedName

class VideoModel (
    @SerializedName("no_video")
    var noVideo: String? = null,

    @SerializedName("nama_video")
    var namaVideo: String? = null,

    @SerializedName("url_video")
    var urlVideo: String? = null,

    @SerializedName("jumlah_pelihat")
    var jumlahPelihat: String? = null,

    @SerializedName("tanggal_upload")
    var tanggalUpload: String? = null
)