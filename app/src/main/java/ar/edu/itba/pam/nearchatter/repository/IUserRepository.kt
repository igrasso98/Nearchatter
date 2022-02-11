package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.User

interface IUserRepository {
    fun addUser(user: User)
}