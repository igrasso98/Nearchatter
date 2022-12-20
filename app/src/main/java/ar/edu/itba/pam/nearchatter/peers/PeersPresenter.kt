package ar.edu.itba.pam.nearchatter.peers

import android.annotation.SuppressLint
import android.util.Log
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
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider,
    private val nearbyService: INearbyService,
    private val hwid: String,
) {

    private val tag = "PeersPresenter"
    private var view: WeakReference<PeersView>? = null
    private var conversations: LiveData<List<Conversation>>? = null
    private val observer: Observer<List<Conversation>> =
        Observer<List<Conversation>> { data -> onConversationsLoaded(data) }

    @SuppressLint("CheckResult")
    fun onViewAttached(peersView: PeersView) {
        view = WeakReference<PeersView>(peersView)
        userRepository.getUsernameById(hwid).subscribeOn(Schedulers.computation())
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe(this::onUsernameLoaded, this::onFailure)
    }

    fun onViewDetached() {
        conversations!!.removeObserver(observer)
    }

    fun deactivateSession() {
        sharedPreferencesStorage.deactivate()
        nearbyService.closeConnections()
    }

    private fun onUsernameLoaded(username: String?) {
        setNearbyServiceCallbacks()
        nearbyService.openConnections(username!!)
        conversations = userRepository.getUserConversations().asLiveData()
        conversations!!.observeForever(observer)
    }

    private fun onConversationsLoaded(conversations: List<Conversation>) {
        val mutableList = conversations.toMutableList()
        mutableList.removeAll { it.getUserId() == hwid }
        mutableList.sortedBy { it.getLastMessageSendAt() }
        if (view?.get() != null) {
            view?.get()!!.bind(mutableList)
        }
    }

    private fun onFailure(throwable: Throwable) {
        Log.e(tag, "Error loading username", throwable)
    }

    private fun setNearbyServiceCallbacks() {
        nearbyService.setOnConnectCallback { user ->
            run {
                view?.get()!!.setOnline(user.getUserId(), true)
            }
        }
        nearbyService.setOnDisconnectCallback { user ->
            run {
                view?.get()!!.setOnline(user.getUserId(), false)
            }
        }
    }
}
