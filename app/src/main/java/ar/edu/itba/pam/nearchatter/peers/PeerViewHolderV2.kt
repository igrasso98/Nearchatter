package ar.edu.itba.pam.nearchatter.peers

import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerReceivedMessageBinding
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerUserBinding
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.User

class PeerViewHolderV2(itemView: ItemContainerUserBinding) :
    RecyclerView.ViewHolder(itemView.root) {
    private var binding: ItemContainerUserBinding = itemView
    private var onPeerSelectedListener: OnPeerSelectedListener? = null


    fun setPeerData(conversation: Conversation) {
        binding.textName.text = conversation.getUsername()
        binding.textRecentMessage.text = conversation.getLastMessagePayload()
        binding.textSendAt.text = conversation.getLastMessageSendAt()
        binding.root.setOnClickListener { _ -> onPeerSelectedListener?.onSelected(conversation.getUsername()) }
    }

    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener?) {
        this.onPeerSelectedListener = listener
    }
}