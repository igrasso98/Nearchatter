package ar.edu.itba.pam.nearchatter.di

import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import ar.edu.itba.pam.nearchatter.utils.schedulers.TestSchedulerProvider

class TestNearchatterModule() {
    fun provideSchedulerProvider(): SchedulerProvider {
        return TestSchedulerProvider()
    }
}
