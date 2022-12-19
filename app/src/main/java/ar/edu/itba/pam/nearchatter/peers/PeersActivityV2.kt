package ar.edu.itba.pam.nearchatter.peers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.chat.ChatActivity
import ar.edu.itba.pam.nearchatter.databinding.ActivityPeersV2Binding
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import java.time.LocalDateTime

class PeersActivityV2 : AppCompatActivity(), PeersView, OnPeerSelectedListener {
    private lateinit var binding: ActivityPeersV2Binding

    private lateinit var peersAdapterV2: PeersAdapterV2
    private var presenter: PeersPresenter? = null

    private val REQUEST_CODE_REQUIRED_PERMISSIONS = 1


    private val REQUIRED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeersV2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        createPresenter()
        setupView()
        loadUserDetails()
        setListeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached()
        if (!hasPermissions(this, *REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun setupView() {
        peersAdapterV2 = PeersAdapterV2()
        peersAdapterV2.setOnPeerSelectedListener(this)
        binding.peersRecyclerView.adapter = peersAdapterV2
        // MOVE THIS FROM HERE
        binding.peersRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as PeersPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator().locateComponent(this)
            presenter = container.getPeersPresenter(this)
        }
    }


    private fun setListeners() {
        binding.imageSignOut.setOnClickListener { signOut() }
    }

    private fun loadUserDetails() {
        binding.textName.text = intent.getStringExtra("username")
    }

    private fun signOut() {
        showToast("Signing out...")
        presenter!!.deactivateSession()
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSelected(userId: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("KEY_USER", userId)
        startActivity(intent)
    }

    override fun bind(conversations: List<Conversation>) {
        peersAdapterV2.setDataset(conversations)
    }


}
