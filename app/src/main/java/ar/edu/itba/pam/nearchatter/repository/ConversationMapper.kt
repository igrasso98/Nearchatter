package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.conversation.ConversationEntity
import ar.edu.itba.pam.nearchatter.db.room.conversation.UserConversation
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User


class ConversationMapper {
    private fun fromUserConversation(userConversation: UserConversation): Conversation {
        val userEnt: UserEntity? = userConversation.user
        val lastMessEnt = userConversation.lastMessage
        val user = User(
            userEnt?.userId!!,
            userEnt?.username!!
        )
        val lastMessage = Message(
            lastMessEnt?.senderId!!,
            lastMessEnt?.receiverId!!,
            lastMessEnt?.payload!!,
            lastMessEnt?.sendAt!!,
            lastMessEnt?.read!!
        )
        return Conversation(user, lastMessage)
    }

    fun fromUserConversations(userConversations: List<UserConversation>): List<Conversation> {
        val conversations: MutableList<Conversation> = ArrayList()
        for (uc in userConversations) {
            conversations.add(fromUserConversation(uc))
        }
        return conversations
    }
}