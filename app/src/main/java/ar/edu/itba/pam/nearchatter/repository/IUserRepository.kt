package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Flowable
import io.reactivex.Single

interface IUserRepository {
    fun addUser(user: User): Single<Long>

    fun updateUsername(userId: Long, username: String): Single<Unit>

    fun getUserConversations(): Flowable<List<Conversation>>
}