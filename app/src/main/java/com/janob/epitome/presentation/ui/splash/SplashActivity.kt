package com.janob.epitome.presentation.ui.splash

import android.os.Bundle
import com.janob.epitome.presentation.base.BaseActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.janob.epitome.databinding.ActivitySplashBinding
import com.janob.epitome.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 2000)
    }
}