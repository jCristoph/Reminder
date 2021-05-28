package com.example.reminder.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.reminder.services.ReminderService
import com.example.reminder.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
        observeLiveData()
        startService()

    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, ReminderService::class.java))
        } else {
            startService(Intent(this, ReminderService::class.java))
        }
    }

    private fun setOnClickListeners() {
        binding.btnStart.setOnClickListener {
            viewModel.getData()
        }
    }

    private fun observeLiveData() {
        viewModel.data.observe(this, {
            binding.numberTv.text = String.format("%.3f", it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, ReminderService::class.java))
        _binding = null
    }
}