package ar.edu.itba.pam.nearchatter.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.itba.pam.nearchatter.databinding.ActivityChatBinding
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter

class ChatActivity : AppCompatActivity(), ChatView {
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

    private fun init() {
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter(
            chatMessages,
            "senderId",
        )
        binding.chatRecyclerView.adapter = chatAdapter

    }

    private fun sendMessage() {
        //SEND MESSAGE LOGIC
        binding.inputMessage.text = null
    }


    private fun setListeners() {
        binding.imageBack.setOnClickListener { onBackPressed() }
        binding.layoutSend.setOnClickListener { sendMessage() }
    }

    private fun loadReceiverDetails() {
        otherUserId = intent.getSerializableExtra("KEY_USER") as String
    }

    override fun bind(username: String, messages: List<Message>) {
        TODO("Not yet implemented")
    }


}