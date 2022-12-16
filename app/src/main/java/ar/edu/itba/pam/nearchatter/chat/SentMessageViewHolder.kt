package ar.edu.itba.pam.nearchatter.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerSentMessageBinding
import ar.edu.itba.pam.nearchatter.domain.Message

class SentMessageViewHolder(itemView: ItemContainerSentMessageBinding) :
    RecyclerView.ViewHolder(itemView.root) {
    private var binding: ItemContainerSentMessageBinding = itemView

    fun bind(chatMessage:Message) {
        binding.textMessage.text = chatMessage.getPayload()
        binding.textDateTime.text = chatMessage.getSendAt()
        // isRead missing (not needed for MVP)
    }

}