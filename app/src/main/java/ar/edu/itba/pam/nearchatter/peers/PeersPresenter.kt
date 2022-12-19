package ar.edu.itba.pam.nearchatter.peers

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
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
    private var conversations: LiveData<List<Conversation>>? = null
    private val observer: Observer<List<Conversation>> =
        Observer<List<Conversation>> { data -> onConversationsLoaded(data) }

    @SuppressLint("CheckResult")
    fun onViewAttached() {
        userRepository.getUsernameById(hwid).subscribeOn(Schedulers.computation())
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe(this::onUsernameLoaded, this::onFailure)
    }

    fun onViewDetached() {
        conversations!!.removeObserver(observer)
    }

    private fun onUsernameLoaded(username: String) {
        setNearbyServiceCallbacks()
        nearbyService.openConnections(username)
        conversations = userRepository.getUserConversations().asLiveData()
        conversations!!.observeForever(observer)
    }

    private fun onConversationsLoaded(conversations: List<Conversation>) {
        if (view.get() != null) {
            view.get()!!.bind(conversations)
        }
    }

    private fun onFailure(throwable: Throwable) {

    }

    private fun setNearbyServiceCallbacks() {
        nearbyService.setOnConnectCallback { user ->
            run {
                println(user)
            }
        }
        nearbyService.setOnDisconnectCallback { user ->
            run {
                println(user.getUsername())
            }
        }
    }
}
