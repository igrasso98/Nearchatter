package ar.edu.itba.pam.nearchatter.peers

import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.login.domain.User
import ar.edu.itba.pam.nearchatter.login.repository.UserRepository
import java.lang.ref.WeakReference

class PeersPresenter(view: PeersView) {
    private val view: WeakReference<PeersView> = WeakReference<PeersView>(view)

    fun connectToPeer(deviceId: String) {}

    fun discoverPeers() {}
}
