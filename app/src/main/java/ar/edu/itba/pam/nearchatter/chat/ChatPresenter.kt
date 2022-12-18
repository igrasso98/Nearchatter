package ar.edu.itba.pam.nearchatter.chat

import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

class ChatPresenter(
    view: ChatView,
    private val schedulerProvider: SchedulerProvider,
    private val nearbyService: INearbyService,
    private val hwid: String,
) {
}