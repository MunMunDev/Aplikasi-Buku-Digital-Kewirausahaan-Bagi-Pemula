package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

import android.view.View
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriGambarModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel

interface OnClickItem {
    interface ClickMateri{
        fun clickItemMateri(materi: MateriListModel, it: View)
    }
    interface ClickMateriGambar{
        fun clickItemGambar(gambar: String, deskripsi: String, it: View)
    }
    interface ClickVideo{
        fun clickItemVideo(video: VideoModel, it: View)
    }

    interface ClickAdminMateri{
        fun clickItemHalaman(halaman: String, it: View)
        fun clickItemJudul(judul: String, it: View)
        fun clickItemDeskripsi(deskripsi: String, it: View)
        fun clickItemGambar(idMateri: String, judul: String, it: View)
        fun clickItemSetting(materi: MateriModel, it: View)
    }

    interface ClickAdminMateriGambar{
        fun clickItemGambar(gambar: String, keterangan: String, it: View)
        fun clickItemKeterangan(keterangan: String, it: View)
        fun clickItemSetting(materi: MateriGambarModel, it: View)
    }

    interface ClickAkun{
        fun clickItemAkun(akun: UsersModel, it: View)
    }
}