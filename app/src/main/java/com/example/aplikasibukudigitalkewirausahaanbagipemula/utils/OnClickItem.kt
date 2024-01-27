package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

import android.view.View
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel

interface OnClickItem {
    interface ClickMateri{
        fun clickItemMateri(materi: MateriModel, it: View)
    }
    interface ClickVideo{
        fun clickItemVideo(video: VideoModel, it: View)
    }
    interface ClickAkun{
        fun clickItemAkun(akun: UsersModel, it: View)
    }
}