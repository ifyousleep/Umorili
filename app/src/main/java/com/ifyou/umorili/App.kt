package com.ifyou.umorili

import android.app.Application
import android.content.Context
import com.ifyou.umorili.net.API
import com.ifyou.umorili.utils.TypefaceUtil
import com.ifyou.umorili.utils.setNightMode
import io.paperdb.Paper
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import kotlin.properties.Delegates
import java.util.concurrent.TimeUnit

var appContext: Context? = null

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        var client = OkHttpClient()
        val cacheSize = 10 * 1024 * 2048 // 20 MiB
        val cacheDirectory = File(cacheDir.absolutePath, "UmoriliAppHttpCache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())
        client = client
                .newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://www.umori.li/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(API::class.java)
        appContext = applicationContext
        Paper.init(this)
        setNightMode(appContext)
        TypefaceUtil.overrideFont(appContext, "SERIF", "fonts/Roboto.ttf")
    }

    companion object {
        private var service: API by Delegates.notNull()

        fun API(): API {
            return service
        }
    }
}
