package ar.edu.itba.pam.nearchatter.peers.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener
import ar.edu.itba.pam.nearchatter.peers.OnPeerSelectedListener
import ar.edu.itba.pam.nearchatter.peers.PeersAdapter

class PeersListViewImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs), PeersListView {

    override fun bind(peersAdapter: PeersAdapter) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = peersAdapter

    }
}