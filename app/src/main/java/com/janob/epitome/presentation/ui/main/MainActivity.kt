package com.janob.epitome.presentation.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.janob.epitome.presentation.base.BaseActivity
import com.janob.epitome.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var mediaRecorder: MediaRecorder
    private var isRecording = false
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventObserve()

        // 파일 경로 설정
        filePath = "${externalCacheDir?.absolutePath}/recorded_audio.mp3"
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is MainEvent.GoToGetMusic -> {
                        // music 입력받기
                        if (checkPermissions()) {
                            startRecording()
                        } else {
                            requestPermissions()
                        }
                    }

                    is MainEvent.StopGetMusic -> {
                        // 녹음 중지
                        stopRecording()
                    }

                    MainEvent.DismissLoading -> dismissLoading()
                    MainEvent.ShowLoading -> showLoading(this@MainActivity)
                    is MainEvent.ShowToastMessage -> showToastMessage(event.msg)
                }
            }
        }
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(filePath)
            prepare()
            start()
        }
        isRecording = true
        showToastMessage("노래를 들려주세요")
    }

    private fun stopRecording() {
        if (isRecording) {
            mediaRecorder.apply {
                stop()
                release()
            }
            isRecording = false
            showToastMessage("녹음 중지")

            // 비동기 처리
            lifecycleScope.launch {
                showLoading(this@MainActivity)
                viewModel.setInputMusic(filePath) // 녹음이 완료된 후 파일 경로를 _inputMusic에 저장
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording()
            } else {
                showToastMessage("권한 설정이 필요합니다")
            }
        }
    }
}