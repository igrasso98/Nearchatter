package ar.edu.itba.pam.nearchatter.peers

import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.UserRepository
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class PeersPresenter(view: PeersView, private val userRepository: UserRepository) {
    private var view: WeakReference<PeersView> = WeakReference<PeersView>(view)
    private val userDisposable: Disposable? = null

    fun onPeerSelected(peer: User) {

    }

    fun ovViewAttached() {
        //userDisposable =
    }

    fun onViewDetached() {
        userDisposable?.dispose()
    }

    fun connectToPeer(deviceId: String) {}

    fun discoverPeers() {}
}
