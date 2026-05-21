package cat.copernic.easytrazamobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userSessionDataStore by preferencesDataStore(name = "user_session")

/**
 * Stores the selected mobile user session.
 *
 * The mobile app does not authenticate with username/password; it keeps only the selected
 * backend user identifier so actions can be attributed to the operator.
 */
class UserSessionRepository(private val context: Context) {

    private val userIdKey = longPreferencesKey("user_id")

    /** Selected user identifier, or `null` when no user has been chosen. */
    val userId: Flow<Long?> = context.userSessionDataStore.data.map { preferences ->
        preferences[userIdKey]
    }

    /** Saves the selected operator identifier. */
    suspend fun saveUserId(id: Long) {
        context.userSessionDataStore.edit { preferences ->
            preferences[userIdKey] = id
        }
    }

    /** Clears the current mobile session. */
    suspend fun clearSession() {
        context.userSessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
