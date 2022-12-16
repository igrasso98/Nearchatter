package ar.edu.itba.pam.nearchatter.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.databinding.ActivityChatBinding
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.peers.PeersAdapter

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var receiverUser: User
    private lateinit var chatMessages: List<Message>
    private lateinit var chatAdapter: ChatAdapter
//    private lateinit var preferenceManager: PreferenceManager


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


}