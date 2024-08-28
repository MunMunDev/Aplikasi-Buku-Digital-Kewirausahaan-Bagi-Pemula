package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

import android.view.View
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel

interface OnClickItem {
    interface ClickMateri{
        fun clickItemMateri(materi: MateriListModel, it: View)
    }
    interface ClickVideo{
        fun clickItemVideo(video: VideoModel, it: View)
    }

    interface ClickAdminMateri{
        fun clickItemHalaman(halaman: String, it: View)
        fun clickItemJudul(judul: String, it: View)
        fun clickItemDeskripsi(deskripsi: String, it: View)
        fun clickItemSetting(tentangStroke: MateriModel, it: View)
    }
    interface ClickAkun{
        fun clickItemAkun(akun: UsersModel, it: View)
    }
}