package ar.edu.itba.pam.nearchatter.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ar.edu.itba.pam.nearchatter.databinding.ActivityChatBinding
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter

class ChatActivity : AppCompatActivity(), ChatView, OnMessageSentListener {
    private lateinit var binding: ActivityChatBinding
    private lateinit var otherUserId: String
    private lateinit var chatMessages: List<Message>
    private lateinit var chatAdapter: ChatAdapter

    private var presenter: ChatPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadReceiverDetails()

        createPresenter()
        setListeners()
        init()
    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as ChatPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator().locateComponent(this)
            presenter = container.getChatPresenter(this, otherUserId)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached()

    }

    override fun onStop() {
        super.onStop()
        presenter?.onViewDetached()
    }


    private fun init() {
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter(otherUserId)
        binding.chatRecyclerView.adapter = chatAdapter
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener { onBackPressed() }
        binding.layoutSend.setOnClickListener { onMessageSent(binding.inputMessage.text.toString()) }
    }

    private fun loadReceiverDetails() {
        otherUserId = intent.getSerializableExtra("KEY_USER") as String
    }

    override fun bind(username: String, messages: List<Message>) {
        binding.chatRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.textName.text = username
        chatAdapter.setDataset(messages)
    }

    override fun onMessageSent(payload: String) {
        if (payload.isNotEmpty() && payload.isNotBlank()) {
            presenter!!.sendMessage(payload)
            binding.inputMessage.text = null
        }
    }


}