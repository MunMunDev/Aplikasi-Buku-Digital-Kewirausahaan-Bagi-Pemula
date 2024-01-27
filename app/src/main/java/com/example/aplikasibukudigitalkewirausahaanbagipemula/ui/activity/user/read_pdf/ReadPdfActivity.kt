package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.read_pdf

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityReadPdfBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.rajat.pdfviewer.PdfEngine
import com.rajat.pdfviewer.PdfQuality


class ReadPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadPdfBinding
    private lateinit var listMateri: MateriModel
    private lateinit var webView: WebView
    private var loading = LoadingAlertDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setUrlFromPreviousActivity()
        setPdfFile()
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setUrlFromPreviousActivity() {
        listMateri = MateriModel()
        try{
            val intent = intent.getParcelableExtra<MateriModel>("materi")
            listMateri = intent!!
            binding.titlePdf.text = listMateri.namaMateri
        } catch (ex: Exception){
            Toast.makeText(this@ReadPdfActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPdfFile() {
        binding.pdf.apply {
            initWithUrl(
                "${Constant.BASE_URL}${listMateri.lokasiFile}${listMateri.urlMateri}",
                pdfQuality = PdfQuality.FAST,
                engine = PdfEngine.GOOGLE
            )
        }
    }
}