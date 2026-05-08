package cat.copernic.easytrazamobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userSessionDataStore by preferencesDataStore(name = "user_session")

class UserSessionRepository(private val context: Context) {

    private val userIdKey = longPreferencesKey("user_id")

    val userId: Flow<Long?> = context.userSessionDataStore.data.map { preferences ->
        preferences[userIdKey]
    }

    suspend fun saveUserId(id: Long) {
        context.userSessionDataStore.edit { preferences ->
            preferences[userIdKey] = id
        }
    }

    suspend fun clearSession() {
        context.userSessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}