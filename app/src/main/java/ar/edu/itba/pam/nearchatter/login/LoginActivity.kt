package ar.edu.itba.pam.nearchatter.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.login.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.login.repository.UserRepository
import ar.edu.itba.pam.nearchatter.login.ui.LoginFormView


class LoginActivity : AppCompatActivity(), LoginView, OnUsernameConfirmListener {
    private var presenter: LoginPresenter? = null
    private lateinit var loginFormView: LoginFormView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createPresenter()

        setUpView()
    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as LoginPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator().locateComponent(this)
            presenter = container.getLoginPresenter(this)
        }
    }

    private fun setUpView() {
        loginFormView = findViewById(R.id.login_form)
        loginFormView.setOnUsernameConfirmListener(this)
        loginFormView.bind()
    }

    override fun bind() {
        loginFormView.bind()
    }

    override fun onConfirm(username: String) {
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra("username", username)
//        startActivity(intent)
        println(username)
    }
}