package dave.gymschedule

import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    protected val ourApplication: GymEventApplication
        get() = application as GymEventApplication
}
