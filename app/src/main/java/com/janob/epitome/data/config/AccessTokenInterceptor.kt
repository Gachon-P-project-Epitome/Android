package com.janob.epitome.data.config

import android.util.Log
import com.janob.epitome.app.App.Companion.sharedPreferences
import com.janob.epitome.presentation.utils.Constants.X_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

//class AccessTokenInterceptor() : Interceptor {
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val builder: Request.Builder = chain.request().newBuilder()
//        val jwt: String? = sharedPreferences.getString(X_ACCESS_TOKEN, null)
//        Log.d("accessToken",jwt.toString())
//        jwt?.let {
//            builder.addHeader("Authorization", jwt)
//        } ?: run {
//        }
//        return chain.proceed(builder.build())
//    }
//}