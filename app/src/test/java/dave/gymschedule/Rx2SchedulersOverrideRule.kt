package dave.gymschedule

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule that overrides all schedulers with the trampoline scheduler.
 * This means everything runs on the same thread, when using this rule
 */
class Rx2SchedulersOverrideRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                //Set Scheduler for AndroidSchedulers
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }

                //Set Schedulers to trampoline
                RxJavaPlugins.reset()
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

                base.evaluate()

                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }
}