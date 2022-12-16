package ar.edu.itba.pam.nearchatter.peers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.domain.Conversation

class PeerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var onPeerSelectedListener: OnPeerSelectedListener? = null

    fun bind(model: Conversation) {
        val username = itemView.findViewById<TextView>(R.id.conversation_username)
        username.text = model.getUsername()
        val lastMessage = itemView.findViewById<TextView>(R.id.conversation_last_message)
        lastMessage.text = model.getLastMessagePayload()
        val sendAt = itemView.findViewById<TextView>(R.id.conversation_send_at)
        sendAt.text = model.getLastMessageSendAt()
//        val addProductButton = itemView.findViewById<ImageView>(R.id.shop_product)
//        addProductButton.setOnClickListener { v: View? ->
//            if (listener != null) {
//                bindMarketDialog(itemView.context, model.getId(), model.getMarketId(), markets)
//            }
//        }
    }

    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener?) {
        this.onPeerSelectedListener = listener
    }
}