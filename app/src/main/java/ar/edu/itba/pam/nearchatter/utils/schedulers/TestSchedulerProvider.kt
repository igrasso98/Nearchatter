package ar.edu.itba.pam.nearchatter.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


class TestSchedulerProvider : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}
