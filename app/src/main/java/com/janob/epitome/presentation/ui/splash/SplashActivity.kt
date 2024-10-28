package com.janob.epitome.presentation.ui.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.janob.epitome.R
import com.janob.epitome.databinding.ActivitySplashBinding
import com.janob.epitome.presentation.base.BaseActivity
import com.janob.epitome.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private var animator: ObjectAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 회전 애니메이션 시작
        startRotation()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 4000)
    }

    override fun onPause() {
        super.onPause()
        stopRotation() // 애니메이션 중지
    }

    private fun startRotation() {
        animator = ObjectAnimator.ofFloat(binding.gifLogo, "rotation", 0f, 360f)
        animator?.duration = 8000 // 8초 동안 회전
        animator?.repeatCount = ObjectAnimator.INFINITE // 무한 반복
        animator?.start()
    }

    private fun stopRotation() {
        animator?.cancel() // 회전 애니메이션 중지
//        // 초기 위치로 리셋 (원하는 경우)
//        binding.gifLogo.rotation = 0f
    }
}