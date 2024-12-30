package com.janob.epitome.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.arthenica.mobileffmpeg.FFmpeg
import com.janob.epitome.presentation.base.BaseActivity
import com.janob.epitome.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var audioRecord: AudioRecord
    private var isRecording = false

    // 샘플링 주파수와 채널 형식, 인코딩 형식을 올바르게 설정
    private val bufferSize = AudioRecord.getMinBufferSize(
        44100, // 샘플링 주파수
        AudioFormat.CHANNEL_IN_MONO, // 채널 형식
        AudioFormat.ENCODING_PCM_16BIT // PCM 인코딩 형식
    )

    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventObserve()

        // 파일 경로 설정
        filePath = "${externalCacheDir?.absolutePath}/recorded_audio.pcm"
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is MainEvent.GoToGetMusic -> {
                        startRecording() // 권한 체크는 startRecording 내에서 진행
                    }

                    is MainEvent.StopGetMusic -> {
                        stopRecording()
                    }

                    MainEvent.DismissLoading -> dismissLoading()
                    MainEvent.ShowLoading -> showLoading(this@MainActivity)
                    is MainEvent.ShowToastMessage -> showToastMessage(event.msg)
                    else -> {}
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startRecording() {
        if (!checkPermissions()) {
            requestPermissions() // 권한 요청
            return
        }

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        try {
            audioRecord.startRecording()
            isRecording = true
            viewModel.controlAnimation(isRecording) // UI 스레드에서 애니메이션 처리

            // 녹음 스레드 시작
            Thread {
                Log.d("startRecording Thread", "start recording")
                val pcmData = ByteArray(bufferSize)
                val outputStream = FileOutputStream(filePath)

                while (isRecording) {
                    val readSize = audioRecord.read(pcmData, 0, bufferSize)
                    if (readSize > 0) {
                        outputStream.write(pcmData, 0, readSize)
                    }
                }

                outputStream.close()
            }.start()
        } catch (e: Exception) {
            Log.e("AudioRecord", "녹음 시작 실패: ${e.message}")
            showToastMessage("녹음 시작 실패")
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            audioRecord.stop()
            audioRecord.release()
        }
        isRecording = false
        viewModel.controlAnimation(isRecording)

        // MP3 파일 경로 설정
        val mp3FilePath = "${externalCacheDir?.absolutePath}/converted_audio.mp3"
        // PCM 파일을 MP3로 변환
        convertPcmToMp3(filePath, mp3FilePath)
    }

    private fun convertPcmToMp3(pcmFilePath: String, mp3FilePath: String) {
        // FFmpeg 명령어 생성
        val command = "-y -f s16le -ar 44100 -ac 1 -i $pcmFilePath -codec:a libmp3lame -b:a 256k $mp3FilePath"

        // 변환 실행
        val rc = FFmpeg.execute(command)

        if (rc == 0) {
            Log.d("convertPcmToMp3", "변환 성공: $mp3FilePath")
            lifecycleScope.launch {
                showLoading(this@MainActivity)
                viewModel.setInputMusic(mp3FilePath) // 변환된 MP3 파일 경로를 ViewModel에 전달
            }
        } else {
            Log.e("convertPcmToMp3", "변환 실패: 코드 $rc")
        }
    }


    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        ActivityCompat.requestPermissions(this,
            permissions.toTypedArray(), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startRecording() // 권한이 허용되면 녹음 시작
            } else {
                showToastMessage("권한 설정이 필요합니다")
            }
        }
    }
}
