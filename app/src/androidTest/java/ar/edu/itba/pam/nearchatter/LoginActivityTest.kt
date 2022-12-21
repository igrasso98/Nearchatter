package ar.edu.itba.pam.nearchatter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import ar.edu.itba.pam.nearchatter.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkActivityVisibility() {
        onView(withId(R.id.layout_LoginActivity)).check(matches(isDisplayed()))

        onView(withId(R.id.login_form)).check(matches(isDisplayed()))
        onView(withId(R.id.login_form_username_input)).check(matches(isDisplayed()))
        onView(withId(R.id.login_form_confirm_button)).check(matches(isDisplayed()))
        onView(withId(R.id.login_form_confirm_button)).check(matches(isNotEnabled()))
    }

    @Test
    fun checkButtonEnabledAfterSettingUsername() {
        onView(withId(R.id.login_form_confirm_button)).check(matches(isNotEnabled()))
        onView(withId(R.id.login_form_username_input)).perform(typeText("username"))
        onView(withId(R.id.login_form_confirm_button)).check(matches(isEnabled()))
    }
}