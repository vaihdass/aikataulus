package ru.vaihdass.aikataulus.data.remote

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vaihdass.aikataulus.BuildConfig
import ru.vaihdass.aikataulus.data.remote.api.AikataulusApi
import ru.vaihdass.aikataulus.data.remote.api.TasksApi
import ru.vaihdass.aikataulus.data.remote.interceptor.AuthorizationFailedInterceptor
import ru.vaihdass.aikataulus.data.remote.interceptor.AuthorizationInterceptor
import ru.vaihdass.aikataulus.data.remote.interceptor.JsonInterceptor
import ru.vaihdass.aikataulus.data.remote.serializer.DateDeserializer
import ru.vaihdass.aikataulus.data.remote.serializer.TaskStatusDeserializer
import ru.vaihdass.aikataulus.data.remote.util.TaskStatus
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Date
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {
    @Provides
    fun provideOkHttpClient(
        jsonInterceptor: JsonInterceptor,
        authorizationFailedInterceptor: AuthorizationFailedInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
    ): OkHttpClient {
        val clientBuilder = if (BuildConfig.DEBUG) {
            createUnsafeClient()
        } else {
            OkHttpClient.Builder()
        }
            .addInterceptor(jsonInterceptor)
            .addInterceptor(authorizationFailedInterceptor)
            .addInterceptor(authorizationInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideAikataulusApi(okHttpClient: OkHttpClient): AikataulusApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.AIKATAULUS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AikataulusApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTasksApi(okHttpClient: OkHttpClient): TasksApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(TaskStatus::class.java, TaskStatusDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_TASKS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TasksApi::class.java)
    }

    // Only for debug!
    private fun createUnsafeClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        return try {
            val trustAllCerts: Array<TrustManager> =
                arrayOf(@SuppressLint("CustomX509TrustManager") object : X509TrustManager {

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?, authType: String?
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?, authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory, trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            okHttpClient
        } catch (e: Exception) {
            okHttpClient
        }
    }
}