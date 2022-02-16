package ar.edu.itba.pam.nearchatter.peers.ui

import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener
import ar.edu.itba.pam.nearchatter.peers.OnPeerSelectedListener
import ar.edu.itba.pam.nearchatter.peers.PeersAdapter

interface PeersListView {
    fun bind(peersAdapter: PeersAdapter)
}