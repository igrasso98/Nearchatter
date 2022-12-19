package ar.edu.itba.pam.nearchatter.chat

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.repository.UserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.time.LocalDate

class ChatPresenter(
    view: ChatView,
    private val userId: String,
    private val userRepository: IUserRepository,
    private val messageRepository: IMessageRepository,
    private val schedulerProvider: SchedulerProvider,
    private val nearbyService: INearbyService,
    private val hwid: String,

    ) {
    private var messages: LiveData<List<Message>>? = null
    private var view: WeakReference<ChatView> = WeakReference<ChatView>(view)
    private var username: String? = null
    private val observer: Observer<List<Message>> =
        Observer<List<Message>> { data -> onMessagesLoaded(data) }


    @SuppressLint("CheckResult")
    fun onViewAttached() {
        userRepository.getUsernameById(userId).subscribeOn(Schedulers.computation())
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe(this::onOtherUserLoaded, this::onFailure)

    }

    fun onViewDetached() {
        messages!!.removeObserver(observer)
    }

    fun sendMessage(payload: String) {
        nearbyService.sendMessage(payload, userId)
    }

    private fun onOtherUserLoaded(username: String) {
        messages = messageRepository.getMessagesById(userId).asLiveData()
        messages!!.observeForever(observer)
    }

    private fun onMessagesLoaded(messages: List<Message>) {
        if (view.get() != null && username != null) {
            view.get()!!.bind(username!!, messages)
        }
    }

    private fun onFailure(throwable: Throwable) {

    }
}
