package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import ar.edu.itba.pam.nearchatter.domain.User

class UserMapper {
    fun toEntity(user: User): UserEntity {
        var userEntity = UserEntity()
        userEntity.username = user.getUsername()
        return userEntity
    }

    fun fromEntity(userEntity: UserEntity): User {
        return User(userEntity.userId!!, userEntity.username!!)
    }
}