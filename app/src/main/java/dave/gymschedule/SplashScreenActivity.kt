package dave.gymschedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.common.database.GymLocationRepository
import dave.gymschedule.common.database.GymLocationRepository.Companion.NO_SAVED_GYM_LOCATION_ID
import dave.gymschedule.schedule.view.GymScheduleActivity
import dave.gymschedule.settings.model.GymLocation
import dave.gymschedule.settings.view.GymLocationAdapter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class SplashScreenActivity : DaggerAppCompatActivity() {

    companion object {
        private val TAG = SplashScreenActivity::class.java.simpleName
    }

    @Inject
    lateinit var gymLocationRepository: GymLocationRepository

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()

        disposables.add(gymLocationRepository.savedGymLocationIdObservable
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ savedGymLocationId ->
                    Log.d(TAG, "got saved gym id: $savedGymLocationId")
                    if (savedGymLocationId == NO_SAVED_GYM_LOCATION_ID) {
                        val adapter = GymLocationAdapter(this, GymLocation.values().asList())
                        AlertDialog.Builder(this)
                                .setAdapter(adapter) { _, selectedPosition ->
                                    val selectedGymLocationId = GymLocation.values()[selectedPosition].locationId

                                    Log.d(TAG, "selected location index: $selectedPosition, locationId: $selectedGymLocationId")
                                    if (selectedGymLocationId != -1) {
                                        disposables.add(gymLocationRepository.setSavedGymLocationId(selectedGymLocationId)
                                                .subscribeOn(io())
                                                .observeOn(mainThread())
                                                .subscribe({
                                                    startGymScheduleActivity()
                                                }, {
                                                    AlertDialog.Builder(this)
                                                            .setTitle("ERROR")
                                                            .setMessage("Error while picking location")
                                                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                                                finish()
                                                            }
                                                            .create()
                                                            .show()
                                                })
                                        )
                                    }
                                }
                                .setCancelable(false)
                                .setTitle(getString(R.string.gym_location_dialog_title))
                                .create()
                                .show()
                    } else {
                        startGymScheduleActivity()
                    }
                }, { error ->

                }))
    }

    private fun startGymScheduleActivity() {
        val gymScheduleActivityIntent = Intent(this, GymScheduleActivity::class.java)
        startActivity(gymScheduleActivityIntent)
        finish()
    }
}
