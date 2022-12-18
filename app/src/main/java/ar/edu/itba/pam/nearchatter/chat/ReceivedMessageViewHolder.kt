package ar.edu.itba.pam.nearchatter.chat

import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerReceivedMessageBinding
import ar.edu.itba.pam.nearchatter.domain.Message
import java.time.format.DateTimeFormatter

class ReceivedMessageViewHolder(itemView: ItemContainerReceivedMessageBinding) :
    RecyclerView.ViewHolder(itemView.root) {
    private var binding: ItemContainerReceivedMessageBinding = itemView

    fun bind(chatMessage: Message) {
        binding.textMessage.text = chatMessage.getPayload()
        binding.textDateTime.text = chatMessage.getSendAt().format(DateTimeFormatter.BASIC_ISO_DATE)
        // isRead missing (not needed for MVP)
    }
}
