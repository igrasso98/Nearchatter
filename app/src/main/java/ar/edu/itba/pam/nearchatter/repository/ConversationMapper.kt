package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.user.UserConversation
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User


class ConversationMapper {
    fun fromUserConversation(userConversation: UserConversation): Conversation {
        val userEnt: UserEntity = userConversation.user!!
        val lastMessEnt = userConversation.lastMessage
        val user = User(
            userEnt.userId,
            userEnt.username,
            false,
        )
        var lastMessage: Message? = null
        if (lastMessEnt != null) {
            lastMessage = Message(
                lastMessEnt.id,
                lastMessEnt.senderId,
                lastMessEnt.receiverId,
                lastMessEnt.payload,
                lastMessEnt.sendAt,
            )
        }
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
