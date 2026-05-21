package cat.copernic.easytrazamobile.data.remote

import cat.copernic.easytrazamobile.data.model.AlbaraMobileDto
import cat.copernic.easytrazamobile.data.model.MateriaPrimeraMobileDto
import cat.copernic.easytrazamobile.data.model.ProveidorMobileDto
import retrofit2.Response
import retrofit2.http.GET
import cat.copernic.easytrazamobile.data.model.UsuariDto
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST
import cat.copernic.easytrazamobile.data.model.GuardarAlbaraRequest

interface ApiService {

    @GET("api/health")
    suspend fun testConnection(): Response<String>

    @GET("api/usuaris")
    suspend fun getUsuaris(): Response<List<UsuariDto>>
    @GET("api/mobile/albarans-proveidor/proveidors")
    suspend fun getProveidorsMobile(): Response<List<ProveidorMobileDto>>

    @GET("api/mobile/albarans-proveidor/{id}")
    suspend fun getAlbaraMobile(
        @Path("id") id: Long
    ): Response<AlbaraMobileDto>

    @GET("api/materies-primeres")
    suspend fun getMateriesPrimeresMobile(): Response<List<MateriaPrimeraMobileDto>>

    @POST("api/mobile/albarans-proveidor/guardar")
    suspend fun guardarAlbaraMobile(
        @Body request: GuardarAlbaraRequest
    ): Response<String>

}