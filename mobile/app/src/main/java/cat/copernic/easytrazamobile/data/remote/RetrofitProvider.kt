package cat.copernic.easytrazamobile.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Factory that creates Retrofit API clients for the configured backend URL.
 */
object RetrofitProvider {

    /**
     * Creates an [ApiService] ensuring Retrofit receives a base URL ending with `/`.
     *
     * @param baseUrl Backend base URL saved in the app configuration.
     */
    fun createApi(baseUrl: String): ApiService {
        val fixedBaseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(fixedBaseUrl)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
