package ar.edu.itba.pam.nearchatter.utils.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun ui(): Scheduler
}