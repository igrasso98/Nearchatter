package ar.edu.itba.pam.nearchatter.peers

import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.NearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class PeersPresenter(
    view: PeersView,
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider
    private val nearbyService: NearbyService,
) {

    private var view: WeakReference<PeersView> = WeakReference<PeersView>(view)
    private var conversationsDisposable: Disposable? = null

    fun onPeerSelected(peer: User) {

    }

    fun onViewAttached() {
        nearbyService.openConnections("", {})
        var userId: Long = sharedPreferencesStorage.getUserId()
        conversationsDisposable =
            userRepository.getUserConversations().subscribeOn(Schedulers.computation())
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onUserAdded, this::onUserAddedFailed)
    }

    fun onViewDetached() {
        conversationsDisposable?.dispose()
    }

    fun connectToPeer(deviceId: String) {}

    fun discoverPeers() {}
}
