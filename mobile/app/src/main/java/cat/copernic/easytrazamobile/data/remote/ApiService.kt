package cat.copernic.easytrazamobile.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/health")
    suspend fun testConnection(): Response<String>
}