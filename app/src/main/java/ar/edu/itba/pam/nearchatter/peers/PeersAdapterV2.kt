package ar.edu.itba.pam.nearchatter.peers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.databinding.ItemContainerUserBinding
import ar.edu.itba.pam.nearchatter.domain.Conversation

class PeersAdapterV2() : RecyclerView.Adapter<PeerViewHolderV2>() {
    private var peers: MutableList<Conversation> = ArrayList()
    private var onPeerSelectedListener: OnPeerSelectedListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataset(newDataset: List<Conversation>?) {
        peers.clear()
        if (newDataset != null) {
            peers.addAll(newDataset)
        }
        notifyDataSetChanged()
    }

    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener) {
        this.onPeerSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolderV2 {
        val itemContainerUserBinding: ItemContainerUserBinding = ItemContainerUserBinding.inflate(
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