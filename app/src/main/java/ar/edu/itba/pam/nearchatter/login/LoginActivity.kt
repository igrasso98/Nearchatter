package ar.edu.itba.pam.nearchatter.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.login.ui.LoginFormView
import ar.edu.itba.pam.nearchatter.peers.PeersActivityV2


class LoginActivity : AppCompatActivity(), LoginView, OnUsernameConfirmListener {
    private var presenter: LoginPresenter? = null
    private var canLogIn: Boolean = false
    private var hasRequestedPermissions: Boolean = false
    private lateinit var loginFormView: LoginFormView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createPresenter()
        checkForPermission()
        setupView()
    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as LoginPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator.locateComponent(this)
            presenter = container.getLoginPresenter(this)
        }
    }

    private fun setupView() {
        loginFormView = findViewById(R.id.login_form)
        loginFormView.setOnUsernameConfirmListener(this)
        loginFormView.bind()
    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            presenter?.onPermissionGranted(1)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 2)
        } else {
            presenter?.onPermissionGranted(2)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), 3)
        } else {
            presenter?.onPermissionGranted(3)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 4)
            } else {
                presenter?.onPermissionGranted(4)
            }
        } else {
            presenter?.onPermissionGranted(4)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE), 5)
            } else {
                presenter?.onPermissionGranted(5)
            }
        } else {
            presenter?.onPermissionGranted(5)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 6)
            } else {
                presenter?.onPermissionGranted(6)
            }
        } else {
            presenter?.onPermissionGranted(6)
        }

        hasRequestedPermissions = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkForPermission()
        }
        hasRequestedPermissions = true
    }


    override fun bind() {
        loginFormView.bind()
    }

    override fun setCanLogIn(canLogIn: Boolean) {
        this.canLogIn = canLogIn
    }

    override fun onConfirm(username: String) {
        checkForPermission()

        if (canLogIn) {
            presenter?.onUsernameConfirm(username)
            val intent = Intent(this, PeersActivityV2::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        } else if (hasRequestedPermissions) {
            Toast.makeText(applicationContext, "Please grant all permissions", Toast.LENGTH_LONG).show()
        }
    }
}
