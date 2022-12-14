package ar.edu.itba.pam.nearchatter.peers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener

class PeersAdapter : RecyclerView.Adapter<PeerViewHolder>() {
    private var onPeerSelectedListener: OnPeerSelectedListener? = null
    private var peersList: MutableList<Conversation> = ArrayList()


    fun setDataset(newDataset: List<Conversation>?) {
        peersList.clear()
        if (newDataset != null) {
            peersList.addAll(newDataset)
        }
        notifyDataSetChanged()
    }


    fun setOnPeerSelectedListener(listener: OnPeerSelectedListener) {
        this.onPeerSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.peer_viewholder, parent, false)
        return PeerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        holder.bind(peersList[position])
        holder.setOnPeerSelectedListener(onPeerSelectedListener)
    }

    override fun getItemCount(): Int {
        return peersList.size
    }

}