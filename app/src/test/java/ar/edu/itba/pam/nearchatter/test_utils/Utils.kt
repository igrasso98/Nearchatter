package ar.edu.itba.pam.nearchatter.test_utils

import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import io.reactivex.Scheduler
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

// Fix for any() must not be null
object MockitoHelper {
  fun <T> anyObject(): T {
    Mockito.any<T>()
    return uninitialized()
  }
  @Suppress("UNCHECKED_CAST")
  fun <T> uninitialized(): T = null as T

  fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

  fun mockSchedulerProvider() : SchedulerProvider {
    val scheduler = mock(Scheduler::class.java)
    `when`(scheduler.scheduleDirect(anyObject())).thenAnswer { invocation ->
      (invocation.getArgument(0) as Runnable).run()
    }

    val schedulerProvider = mock(SchedulerProvider::class.java)
    `when`(schedulerProvider.io()).thenReturn(scheduler)
    `when`(schedulerProvider.computation()).thenReturn(scheduler)
    `when`(schedulerProvider.ui()).thenReturn(scheduler)
    return schedulerProvider
  }
}
