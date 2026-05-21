package cat.copernic.easytrazamobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "server_config")

/**
 * Persists the backend base URL selected by the operator.
 *
 * The URL is stored with DataStore so the mobile app can reconnect to the same backend
 * after being closed or restarted.
 */
class ServerConfigRepository(private val context: Context) {

    private val serverIpKey = stringPreferencesKey("server_ip")

    /** Backend base URL currently saved in local storage. */
    val serverIp: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[serverIpKey] ?: ""
    }

    /**
     * Saves the backend URL after trimming accidental spaces.
     *
     * @param ip Full backend URL, including protocol and port.
     */
    suspend fun saveServerIp(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[serverIpKey] = ip.trim()
        }
    }
}
