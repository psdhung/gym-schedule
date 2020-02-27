package dave.gymschedule.settings.repository

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable

class GymLocationRepository(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val TAG = GymLocationRepository::class.java.simpleName

        private const val SAVED_GYM_LOCATION_ID_KEY = "saved-gym-location-id-key"

        const val NO_SAVED_GYM_LOCATION_ID = -1
    }

    val savedGymLocationIdObservable: Observable<Int> = Observable.create { emitter ->
        val sharedPreferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == SAVED_GYM_LOCATION_ID_KEY) {
                val gymId = sharedPreferences.getInt(SAVED_GYM_LOCATION_ID_KEY, NO_SAVED_GYM_LOCATION_ID)
                Log.d(TAG, "listener, saved id = $gymId")
                emitter.onNext(gymId)
            }
        }
        emitter.setCancellable { sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesChangeListener) }
        val gymId = sharedPreferences.getInt(SAVED_GYM_LOCATION_ID_KEY, NO_SAVED_GYM_LOCATION_ID)
        Log.d(TAG, "initial gym id=$gymId")
        emitter.onNext(gymId)
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesChangeListener)
    }

    fun setSavedGymLocationId(locationId: Int): Completable {
        Log.d(TAG, "saving gym location id $locationId")
        return Completable.create { emitter ->
            sharedPreferences.edit()
                    .putInt(SAVED_GYM_LOCATION_ID_KEY, locationId)
                    .commit()
            emitter.onComplete()
        }
    }
}