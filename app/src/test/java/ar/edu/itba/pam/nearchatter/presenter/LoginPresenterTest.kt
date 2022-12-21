package ar.edu.itba.pam.nearchatter.presenter

import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.test_utils.MockitoHelper
import ar.edu.itba.pam.nearchatter.test_utils.TestingUtils
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LoginPresenterTest {
  @Before
  fun setUp() {
    TestingUtils.setNearchatterContainer()
  }

  @Test
  fun onUsernameConfirmShouldAddUserToRepository() {
    val loginView = mock(LoginView::class.java)
    val userRepository = mock(IUserRepository::class.java)
    `when`(userRepository.addUser(MockitoHelper.anyObject())).thenReturn(Single.just(Unit))
    val sharedPreferences = mock(ISharedPreferencesStorage::class.java)
    
    val presenter = LoginPresenter(
      loginView,
      userRepository,
      sharedPreferences,
      NearchatterContainerLocator.container!!.getSchedulerProvider(),
      "hwId"
    )

    presenter.onUsernameConfirm("test_username")

    verify(userRepository).addUser(User("hwId", "test_username"))
  }

  @Test
  fun onUsernameConfirmShouldAddUserToSharedPreferences() {
    val loginView = mock(LoginView::class.java)
    val userRepository = mock(IUserRepository::class.java)
    `when`(userRepository.addUser(MockitoHelper.anyObject())).thenReturn(Single.just(Unit))
    val sharedPreferences = mock(ISharedPreferencesStorage::class.java)

    val presenter = LoginPresenter(
      loginView,
      userRepository,
      sharedPreferences,
      NearchatterContainerLocator.container!!.getSchedulerProvider(),
      "hwId"
    )

    presenter.onUsernameConfirm("test_username")

    verify(sharedPreferences).setUsername("test_username")
  }

  @Test
  fun onUsernameConfirmShouldTrimUsername() {
    val loginView = mock(LoginView::class.java)
    val userRepository = mock(IUserRepository::class.java)
    `when`(userRepository.addUser(MockitoHelper.anyObject())).thenReturn(Single.just(Unit))
    val sharedPreferences = mock(ISharedPreferencesStorage::class.java)

    val presenter = LoginPresenter(
      loginView,
      userRepository,
      sharedPreferences,
      NearchatterContainerLocator.container!!.getSchedulerProvider(),
      "hwId"
    )

    presenter.onUsernameConfirm("  112 12   sadf   ")

    verify(userRepository).addUser(User("hwId", "112 12   sadf"))
  }

  @Test
  fun retrieveUsernameShouldBeNotEmptyIfGetUsernameFromSharedPreferencesIsPresent() {
    val loginView = mock(LoginView::class.java)
    val userRepository = mock(IUserRepository::class.java)
    val sharedPreferences = mock(ISharedPreferencesStorage::class.java)
    `when`(sharedPreferences.getUsername()).thenReturn("test_username")

    val presenter = LoginPresenter(
      loginView,
      userRepository,
      sharedPreferences,
      NearchatterContainerLocator.container!!.getSchedulerProvider(),
      "hwId"
    )

    presenter.retrieveUsername()

    verify(sharedPreferences).getUsername()
    verify(loginView).setUsername("test_username")
  }

  @Test
  fun retrieveUsernameShouldBeEmptyIfGetUsernameFromSharedPreferencesIsNotPresent() {
    val loginView = mock(LoginView::class.java)
    val userRepository = mock(IUserRepository::class.java)
    val sharedPreferences = mock(ISharedPreferencesStorage::class.java)
    `when`(sharedPreferences.getUsername()).thenReturn(null)

    val presenter = LoginPresenter(
      loginView,
      userRepository,
      sharedPreferences,
      NearchatterContainerLocator.container!!.getSchedulerProvider(),
      "hwId"
    )

    presenter.retrieveUsername()

    verify(sharedPreferences).getUsername()
    verify(loginView).setUsername("")
  }

  @Test
  fun onPermissionGrantedSetsCanLogIn() {
    val loginView = mock(LoginView::class.java)
    val userRepository = mock(IUserRepository::class.java)
    val sharedPreferences = mock(ISharedPreferencesStorage::class.java)
    val presenter = LoginPresenter(
      loginView,
      userRepository,
      sharedPreferences,
      NearchatterContainerLocator.container!!.getSchedulerProvider(),
      "hwId"
    )

    presenter.onPermissionGranted(1)
    verify(loginView, times(1)).setCanLogIn(false)
    reset(loginView)

    presenter.onPermissionGranted(2)
    verify(loginView, times(1)).setCanLogIn(false)
    reset(loginView)

    presenter.onPermissionGranted(3)
    verify(loginView, times(1)).setCanLogIn(false)
    reset(loginView)

    presenter.onPermissionGranted(4)
    verify(loginView, times(1)).setCanLogIn(false)
    reset(loginView)

    presenter.onPermissionGranted(5)
    verify(loginView, times(1)).setCanLogIn(false)
    reset(loginView)

    presenter.onPermissionGranted(6)
    verify(loginView, times(1)).setCanLogIn(true)
  }
}
