package ar.edu.itba.pam.nearchatter.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.login.ui.LoginFormView
import ar.edu.itba.pam.nearchatter.peers.PeersActivity


class LoginActivity : AppCompatActivity(), LoginView, OnUsernameConfirmListener {
    private var presenter: LoginPresenter? = null
    private lateinit var loginFormView: LoginFormView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkForPermission()
        createPresenter()
        setupView()
    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as LoginPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator().locateComponent(this)
            presenter = container.getLoginPresenter(this)
        }
    }

    private fun setupView() {
        loginFormView = findViewById(R.id.login_form)
        loginFormView.setOnUsernameConfirmListener(this)
        loginFormView.bind()
    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT ), 1
            )
        }
    }


    override fun bind() {
        loginFormView.bind()
    }

    override fun onConfirm(username: String) {
        presenter?.onUsernameConfirm(username)
        val intent = Intent(this, PeersActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }
}