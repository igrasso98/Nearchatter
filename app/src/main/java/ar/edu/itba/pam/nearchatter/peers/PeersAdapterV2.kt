package ar.edu.itba.pam.nearchatter.peers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerUserBinding
import ar.edu.itba.pam.nearchatter.domain.Conversation

class PeersAdapterV2(peers: List<Conversation>) : RecyclerView.Adapter<PeerViewHolderV2>() {
    private var peers: List<Conversation> = peers
    private var onPeerSelectedListener: OnPeerSelectedListener? = null

    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener) {
        this.onPeerSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolderV2 {
        var itemContainerUserBinding: ItemContainerUserBinding = ItemContainerUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PeerViewHolderV2(itemContainerUserBinding)
    }

    override fun onBindViewHolder(holder: PeerViewHolderV2, position: Int) {
        holder.setPeerData(peers[position])
        holder.setOnPeerSelectedListener(onPeerSelectedListener)

    }

    override fun getItemCount(): Int {
        return peers.size
    }
}