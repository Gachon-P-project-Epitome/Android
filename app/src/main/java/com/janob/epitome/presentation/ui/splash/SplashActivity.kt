package com.janob.epitome.presentation.ui.splash

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // GIF 로드
        Glide.with(this)
            .asGif()
            .load(R.raw.epitome)
            .into(binding.gifLogo)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 4000)
    }
}