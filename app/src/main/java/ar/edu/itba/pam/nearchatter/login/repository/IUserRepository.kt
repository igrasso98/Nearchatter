package ar.edu.itba.pam.nearchatter.login.repository

import ar.edu.itba.pam.nearchatter.login.domain.User

interface IUserRepository {
    fun addUser(user: User)
}