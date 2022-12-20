package ar.edu.itba.pam.nearchatter.peers

import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerUserBinding
import ar.edu.itba.pam.nearchatter.domain.Conversation
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PeerViewHolderV2(itemView: ItemContainerUserBinding) :
    RecyclerView.ViewHolder(itemView.root) {
    private var binding: ItemContainerUserBinding = itemView
    private var onPeerSelectedListener: OnPeerSelectedListener? = null


    fun setPeerData(conversation: Conversation, connected: Boolean) {
        binding.textName.text = conversation.getUsername()
        binding.textRecentMessage.text = conversation.getLastMessagePayload()
        binding.textSendAt.text = conversation.getLastMessageSendAt()?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))

        if (connected) {
            binding.progressBar.visibility = android.view.View.GONE
            binding.imageProfile.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.imageProfile.visibility = android.view.View.GONE
        }

        binding.root.setOnClickListener { _ -> onPeerSelectedListener?.onSelected(conversation.getUserId()) }
    }

    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener?) {
        this.onPeerSelectedListener = listener
    }
}
