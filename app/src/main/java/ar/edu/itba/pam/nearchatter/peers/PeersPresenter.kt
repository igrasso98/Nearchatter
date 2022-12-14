package ar.edu.itba.pam.nearchatter.peers

import android.annotation.SuppressLint
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class PeersPresenter(
    view: PeersView,
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider,
    private val nearbyService: INearbyService,
    private val hwid: String,
) {

    private var view: WeakReference<PeersView> = WeakReference<PeersView>(view)
    private var conversationsDisposable: Disposable? = null

    fun onPeerSelected(peer: User) {

    }

    @SuppressLint("CheckResult")
    fun onViewAttached() {
        userRepository.getUsernameById(hwid).subscribeOn(Schedulers.computation())
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe(this::onUsernameLoaded, this::onFailure)
    }

    fun onViewDetached() {
        conversationsDisposable?.dispose()
    }

    fun onUsernameLoaded(username: String) {
        nearbyService.openConnections(username)
        conversationsDisposable =
            userRepository.getUserConversations().subscribeOn(Schedulers.computation())
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onConversationsLoaded, this::onFailure)
    }

    fun onConversationsLoaded(conversations: List<Conversation>) {
        if (view.get() != null) {
            view.get()!!.bind(conversations)
        }
    }

    fun onFailure(throwable: Throwable) {

    }

    fun connectToPeer(deviceId: String) {}

    fun discoverPeers() {}
}
