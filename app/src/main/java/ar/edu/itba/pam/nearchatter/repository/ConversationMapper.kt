package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.conversation.ConversationEntity
import ar.edu.itba.pam.nearchatter.domain.Conversation


class ConversationMapper {
    fun toEntity(conv: Conversation): ConversationEntity {
    }

    fun fromEntity(convEntity: ConversationEntity): Conversation {
        return Conversation()
    }
}