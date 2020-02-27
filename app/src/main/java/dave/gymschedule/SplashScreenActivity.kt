package dave.gymschedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.schedule.view.GymScheduleActivity
import dave.gymschedule.settings.GymLocationInteractor
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
    lateinit var gymLocationInteractor: GymLocationInteractor

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()

        disposables.add(gymLocationInteractor.savedGymLocationObservable
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe({ savedGymLocation ->
                    Log.d(TAG, "got saved gym id: ${savedGymLocation.locationId}")
                    if (savedGymLocation == GymLocation.NONE) {
                        val adapter = GymLocationAdapter(this, GymLocation.getValidLocations())
                        val pickLocationDialog = AlertDialog.Builder(this)
                                .setAdapter(adapter) { _, selectedPosition ->
                                    val selectedGymLocation = GymLocation.values()[selectedPosition]

                                    Log.d(TAG, "selected location index: $selectedPosition, locationId: $selectedGymLocation")
                                    disposables.add(gymLocationInteractor.setSavedGymLocation(selectedGymLocation)
                                            .subscribeOn(io())
                                            .observeOn(mainThread())
                                            .subscribe({
                                                startGymScheduleActivity()
                                            }, {
                                                AlertDialog.Builder(this)
                                                        .setTitle("ERROR")
                                                        .setMessage("Error while picking location")
                                                        .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
                                                        .create()
                                                        .show()
                                            })
                                    )
                                }
                                .setCancelable(false)
                                .setTitle(getString(R.string.gym_location_dialog_title))
                                .create()

                        pickLocationDialog.listView.apply {
                            divider = ColorDrawable(Color.GRAY)
                            dividerHeight = 1
                        }
                        pickLocationDialog.show()
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
