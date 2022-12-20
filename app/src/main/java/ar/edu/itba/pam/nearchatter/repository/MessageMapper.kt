package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.message.MessageEntity
import ar.edu.itba.pam.nearchatter.domain.Message


class MessageMapper {
    fun toEntity(message: Message): MessageEntity {
        return MessageEntity(message.getId(), message.getSenderId(),  message.getReceiverId(), message.getPayload(), message.getSendAt())
    }

    fun fromEntity(messageEntity: MessageEntity): Message {
        return Message(messageEntity.id, messageEntity.senderId,  messageEntity.receiverId, messageEntity.payload, messageEntity.sendAt)
    }
}
