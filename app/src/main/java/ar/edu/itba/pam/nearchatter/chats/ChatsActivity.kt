package ar.edu.itba.pam.nearchatter.chats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.pam.nearchatter.R

class ChatsActivity  : AppCompatActivity(), ChatsView, OnChatSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun bind() {
        TODO("Not yet implemented")
    }

    override fun onSelected(userId: String) {
        TODO("Not yet implemented")
    }
}