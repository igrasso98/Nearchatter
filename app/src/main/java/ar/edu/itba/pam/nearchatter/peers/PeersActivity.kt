package ar.edu.itba.pam.nearchatter.peers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.peers.ui.PeersListView


class PeersActivity : AppCompatActivity(), PeersView, OnPeerSelectedListener {
    private var presenter: PeersPresenter? = null
    private lateinit var adapter: PeersAdapter
    private lateinit var peersListView: PeersListView
    private val REQUEST_CODE_REQUIRED_PERMISSIONS = 1


    private val REQUIRED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peers)

        createPresenter()
        setupView()
    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as PeersPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator().locateComponent(this)
            presenter = container.getPeersPresenter(this)
        }
    }

    private fun setupView() {
        peersListView = findViewById(R.id.peers_list)
        adapter = PeersAdapter()
        adapter.setOnPeerSelectedListener(this)
        peersListView.bind(adapter)
    }

    override fun onStop() {
        super.onStop()
        presenter?.onViewDetached()
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


    override fun onSelected(deviceId: String) {
        TODO("Not yet implemented")
        println(deviceId)
    }

    override fun bind(conversations: List<Conversation>) {
        var visibleUsername = findViewById<TextView>(R.id.visible_username)
        visibleUsername.text = "Logged as: " + intent.getStringExtra("username")
        val mockedConversations = listOf<Conversation>(
            Conversation(
                User("ksjdfnksjdnf", "Delfi Varas"),
                Message("skjdfnksjdfn", "kdjsfnskdjfn", "Hola, como andas?", "Now", true)
            ),
            Conversation(
                User("ksjdfnksjdnf", "Nico Britos"),
                Message("skjdfnksjdfn", "kdjsfnskdjfn", "llegamos con PAM?", "Yesterday", true)
            ),
            Conversation(
                User("ksjdfnksjdnf", "Santi Grasso"),
                Message("skjdfnksjdfn", "kdjsfnskdjfn", "chau", "10/04/2020", true)
            ),
        )
        adapter.setDataset(mockedConversations)
    }

}