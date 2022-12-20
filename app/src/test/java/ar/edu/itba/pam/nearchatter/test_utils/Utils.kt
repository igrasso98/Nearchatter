package ar.edu.itba.pam.nearchatter.test_utils

import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

// Fix for any() must not be null
object MockitoHelper {
  fun <T> anyObject(): T {
    Mockito.any<T>()
    return uninitialized()
  }
  @Suppress("UNCHECKED_CAST")
  fun <T> uninitialized(): T = null as T

  fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}

object TestingUtils {
  fun setNearchatterContainer(nearchatterContainer: NearchatterContainer = TestNearchatterContainer()) {
    NearchatterContainerLocator.setComponent(nearchatterContainer)
  }
}
