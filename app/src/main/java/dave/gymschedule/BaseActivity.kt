package dave.gymschedule

import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    protected val ourApplication: GymScheduleApplication
        get() = application as GymScheduleApplication
}
