package cat.copernic.easytrazamobile.data.remote

import cat.copernic.easytrazamobile.data.model.AlbaraMobileDto
import cat.copernic.easytrazamobile.data.model.GuardarAlbaraRequest
import cat.copernic.easytrazamobile.data.model.LotOberturaDto
import cat.copernic.easytrazamobile.data.model.MateriaPrimeraMobileDto
import cat.copernic.easytrazamobile.data.model.ProveidorMobileDto
import cat.copernic.easytrazamobile.data.model.UsuariDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit definition of the backend endpoints used by the mobile application.
 */
interface ApiService {

    /** Checks whether the backend is reachable. */
    @GET("api/health")
    suspend fun testConnection(): Response<String>

    /** Loads users available for mobile selection. */
    @GET("api/usuaris")
    suspend fun getUsuaris(): Response<List<UsuariDto>>

    /** Loads active suppliers for the delivery-note form. */
    @GET("api/mobile/albarans-proveidor/proveidors")
    suspend fun getProveidorsMobile(): Response<List<ProveidorMobileDto>>

    /** Loads one supplier delivery note by identifier. */
    @GET("api/mobile/albarans-proveidor/{id}")
    suspend fun getAlbaraMobile(@Path("id") id: Long): Response<AlbaraMobileDto>

    /** Loads raw material options. */
    @GET("api/materies-primeres")
    suspend fun getMateriesPrimeresMobile(): Response<List<MateriaPrimeraMobileDto>>

    /** Creates a supplier delivery note and its lots from mobile. */
    @POST("api/mobile/albarans-proveidor/guardar")
    suspend fun guardarAlbaraMobile(@Body request: GuardarAlbaraRequest): Response<String>

    /** Loads lots that are in stock and can be opened. */
    @GET("api/mobile/lots-proveidor/en-estoc")
    suspend fun getLotsEnEstoc(): Response<List<LotOberturaDto>>

    /** Opens a lot, optionally closing the previously opened lot for the same material. */
    @POST("api/mobile/lots-proveidor/{id}/obrir")
    suspend fun obrirLotMobile(
        @Path("id") id: Long,
        @Query("usuariId") usuariId: Long,
        @Query("confirmar") confirmar: Boolean = false
    ): Response<String>

    /** Loads lots that are currently open and can be closed. */
    @GET("api/mobile/lots-proveidor/oberts")
    suspend fun getLotsOberts(): Response<List<LotOberturaDto>>

    /** Closes an open lot. */
    @POST("api/mobile/lots-proveidor/{id}/tancar")
    suspend fun tancarLotMobile(@Path("id") id: Long): Response<String>
}
