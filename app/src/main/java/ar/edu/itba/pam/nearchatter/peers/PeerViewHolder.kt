package ar.edu.itba.pam.nearchatter.peers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.domain.User

class PeerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var onPeerSelectedListener: OnPeerSelectedListener? = null

    fun bind(model: User) {

    }

    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener?) {
        this.onPeerSelectedListener = listener
    }
}