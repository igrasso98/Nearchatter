package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import ar.edu.itba.pam.nearchatter.domain.User

class UserMapper {
    fun toEntity(user: User): UserEntity {
        return UserEntity(user.getUserId(), user.getUsername())
    }

    fun fromEntity(userEntity: UserEntity): User {
        return User(userEntity.userId!!, userEntity.username!!)
    }
}