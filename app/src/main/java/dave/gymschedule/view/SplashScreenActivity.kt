package dave.gymschedule.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import dave.gymschedule.R
import dave.gymschedule.repository.EventTypeStateRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class SplashScreenActivity : DaggerAppCompatActivity() {

    companion object {
        private val TAG = SplashScreenActivity::class.java.simpleName
    }

    @Inject
    lateinit var eventTypeStateRepository: EventTypeStateRepository

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        disposables.add(eventTypeStateRepository.initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "initialization finished, starting gym schedule activity")
                    startActivity(Intent(this, GymScheduleActivity::class.java))
                    finish()
                }, {
                    Log.d(TAG, "initialization failed", it)
                }))
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

}
