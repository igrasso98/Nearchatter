package ar.edu.itba.pam.nearchatter.peers.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.itba.pam.nearchatter.peers.PeersAdapter

class PeersListViewImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs), PeersListView {

    override fun bind(peersAdapter: PeersAdapter) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = peersAdapter
        addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
    }
}