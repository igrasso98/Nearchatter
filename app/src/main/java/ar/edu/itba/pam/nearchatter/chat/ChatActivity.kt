package ar.edu.itba.pam.nearchatter.chat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.pam.nearchatter.databinding.ActivityChatBinding
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Message

class ChatActivity : AppCompatActivity(), ChatView, OnMessageSentListener {
    private lateinit var binding: ActivityChatBinding
    private lateinit var otherUserId: String
    private lateinit var chatMessages: List<Message>
    private lateinit var chatAdapter: ChatAdapter
    private var isOnline: Boolean = false

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
                NearchatterContainerLocator.locateComponent(this)
            presenter = container.getChatPresenter(otherUserId)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached(this)

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

    override fun setOnline(online: Boolean) {
        isOnline = online
        if (!online){
            binding.textName.visibility = View.GONE
            binding.connectingProgressBar.visibility = View.VISIBLE
            binding.layoutSend.isEnabled = false
        } else {
            binding.textName.visibility = View.VISIBLE
            binding.connectingProgressBar.visibility = View.GONE
            binding.layoutSend.isEnabled = true
        }
    }

    override fun onMessageSent(payload: String) {
        if (payload.isNotEmpty() && payload.isNotBlank() && isOnline) {
            presenter!!.sendMessage(payload)
            binding.inputMessage.text = null
        }
    }


}
