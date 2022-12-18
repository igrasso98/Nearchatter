package ar.edu.itba.pam.nearchatter.peers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.chat.ChatActivity
import ar.edu.itba.pam.nearchatter.databinding.ActivityPeersV2Binding
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import java.time.LocalDate

class PeersActivityV2 : AppCompatActivity(), PeersView, OnPeerSelectedListener {
    private lateinit var binding: ActivityPeersV2Binding

    //    private lateinit var peers: List<Conversation>
    private lateinit var peersAdapterV2: PeersAdapterV2
    private var presenter: PeersPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeersV2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        createPresenter()
        setupView()
        loadUserDetails()
        setListeners()
    }

    private fun setupView() {
        peersAdapterV2 = PeersAdapterV2()
        peersAdapterV2.setOnPeerSelectedListener(this)
        binding.peersRecyclerView.adapter = peersAdapterV2
        // MOVE THIS FROM HERE
        binding.peersRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun createPresenter() {
        presenter = lastNonConfigurationInstance as PeersPresenter?

        if (presenter == null) {
            val container: NearchatterContainer =
                NearchatterContainerLocator().locateComponent(this)
            presenter = container.getPeersPresenter(this)
        }
    }


    private fun setListeners() {
        binding.imageSignOut.setOnClickListener { signOut() }
    }

    private fun loadUserDetails() {
        binding.textName.text = intent.getStringExtra("username")
    }

    private fun signOut() {
        showToast("Signing out...")
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

//    val mockedConversations = listOf<Conversation>(
//        Conversation(
//            User("ksjdfnksjdnf", "Delfi Varas"),
//            Message("skjdfnksjdfn", "kdjsfnskdjfn", "Hola, como andas?", LocalDate.now())
//        ),
//        Conversation(
//            User("ksjdfnksjdnf", "Nico Britos"),
//            Message(
//                "skjdfnksjdfn",
//                "kdjsfnskdjfn",
//                "llegamos con PAM?",
//                LocalDate.now().minusDays(1)
//            )
//        ),
//        Conversation(
//            User("ksjdfnksjdnf", "Santi Grasso"),
//            Message(
//                "skjdfnksjdfn",
//                "kdjsfnskdjfn",
//                "Esto es una descripcion un poco mas larga para ver que pasa",
//                LocalDate.now().minusMonths(1),
//            )
//        ),
//    )

    override fun onSelected(deviceId: String) {
        val intent = Intent(this, ChatActivity::class.java)
//        intent.putExtra("deviceId", deviceId)
        startActivity(intent)
        println("entre aca")
    }

    override fun bind(conversations: List<Conversation>) {
        peersAdapterV2.setDataset(conversations)
    }


}
