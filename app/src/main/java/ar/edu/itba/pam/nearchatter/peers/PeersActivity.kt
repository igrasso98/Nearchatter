package ar.edu.itba.pam.nearchatter.peers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener
import ar.edu.itba.pam.nearchatter.peers.ui.PeersListView
import ar.edu.itba.pam.nearchatter.repository.UserRepository

class PeersActivity : AppCompatActivity(), PeersView, OnPeerSelectedListener {
    private var presenter: PeersPresenter? = null
    private lateinit var adapter: PeersAdapter
    private lateinit var peersListView: PeersListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peers_list)
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
        peersListView = findViewById(R.id.peers_list_view)
        adapter = PeersAdapter()
        adapter.setOnPeerSelectedListener(this)
        peersListView.bind(adapter)
    }

    override fun onStop() {
        super.onStop()
        presenter?.onViewDetached()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached()
    }

    override fun onSelected(deviceId: String) {
        TODO("Not yet implemented")
        println(deviceId)
    }

    override fun bind(conversations: List<Conversation>) {
        peersListView.bind(adapter)
    }

}