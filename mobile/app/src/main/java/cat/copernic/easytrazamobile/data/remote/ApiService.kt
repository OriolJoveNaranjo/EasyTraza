package cat.copernic.easytrazamobile.data.remote

import retrofit2.Response
import retrofit2.http.GET
import cat.copernic.easytrazamobile.data.model.UsuariDto

interface ApiService {

    @GET("api/health")
    suspend fun testConnection(): Response<String>

    @GET("api/usuaris")
    suspend fun getUsuaris(): Response<List<UsuariDto>>
}