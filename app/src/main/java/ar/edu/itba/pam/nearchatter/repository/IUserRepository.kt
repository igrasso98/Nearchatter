package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Flowable
import io.reactivex.Single

interface IUserRepository {
    fun addUser(user: User): Single<Unit>

    fun getUsernameById(id: String): Single<String>

    fun updateUsername(userId: String, username: String): Single<Unit>

    fun getUserConversations(): Flowable<List<Conversation>>
}