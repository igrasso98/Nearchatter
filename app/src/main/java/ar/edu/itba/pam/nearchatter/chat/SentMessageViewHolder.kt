package ar.edu.itba.pam.nearchatter.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerSentMessageBinding
import ar.edu.itba.pam.nearchatter.domain.Message
import java.time.format.DateTimeFormatter

class SentMessageViewHolder(itemView: ItemContainerSentMessageBinding) :
    RecyclerView.ViewHolder(itemView.root) {
    private var binding: ItemContainerSentMessageBinding = itemView

    fun bind(chatMessage:Message) {
        binding.textMessage.text = chatMessage.getPayload()
        binding.textDateTime.text = chatMessage.getSendAt().format(DateTimeFormatter.BASIC_ISO_DATE)
        // isRead missing (not needed for MVP)
    }

}
