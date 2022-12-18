package ar.edu.itba.pam.nearchatter.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.itba.pam.nearchatter.databinding.ActivityChatBinding
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User

class ChatActivity : AppCompatActivity(), ChatView {
    private lateinit var binding: ActivityChatBinding
    private lateinit var receiverUser: User
    private lateinit var chatMessages: List<Message>
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        loadReceiverDetails()
        init()
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
//        receiverUser = intent.getSerializableExtra("KEY_USER") as User
        binding.textName.text = "Delfi Varas"
    }

    override fun bind(conversations: List<Message>) {
        TODO("Not yet implemented")
    }


}