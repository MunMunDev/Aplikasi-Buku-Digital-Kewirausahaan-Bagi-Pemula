package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.read_pdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityReadPdfBinding

class ReadPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadPdfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}