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
        val productName = itemView.findViewById<TextView>(R.id.conversation_username)
        productName.text = model.getUsername()
        val productDescription = itemView.findViewById<TextView>(R.id.conversation_payload)
        productDescription.text = model.getLastMessagePayload()
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