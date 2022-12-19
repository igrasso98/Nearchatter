package ar.edu.itba.pam.nearchatter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerReceivedMessageBinding
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerSentMessageBinding
import ar.edu.itba.pam.nearchatter.domain.Message

class ChatAdapter(private var chatMessages: List<Message>, private var otherId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_SENT = 1
    val VIEW_TYPE_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENT) {
            return SentMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        } else {
            return ReceivedMessageViewHolder(
                ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).bind(chatMessages[position])
        } else {
            (holder as ReceivedMessageViewHolder).bind(chatMessages[position])
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatMessages[position].getSenderId() != otherId) {
            return VIEW_TYPE_SENT
        } else {
            return VIEW_TYPE_RECEIVED
        }

    }

}