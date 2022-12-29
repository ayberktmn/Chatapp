package com.afsar.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsar.chatapp.databinding.FragmentChatBinding
import com.afsar.chatapp.databinding.FragmentGirisBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatFragment : Fragment() {

    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private lateinit var adapter: ChatRecycclerAdapter
    private var chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = Firebase.firestore
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatRecycclerAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.gonderBtn.setOnClickListener {

            auth.currentUser?.let {
                val chatText = binding.yorumText.text.toString()
                val date = FieldValue.serverTimestamp()
                val user = it.email

                val dataMap = HashMap<String, Any>()
                dataMap.put("text",chatText)
                dataMap.put("user",user!!)
                dataMap.put("date",date)

                firestore.collection("Chats").add(dataMap).addOnSuccessListener {
                    binding.yorumText.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                    binding.yorumText.setText("")
                }
            }

        }
        firestore.collection("Chats").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener{value , error ->

            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
             }else{
                 if (value != null){
                     if (value.isEmpty){
                         Toast.makeText(requireContext(),"Mesaj Yok",Toast.LENGTH_LONG).show()
                     }else{

                         val documents = value.documents
                         chats.clear()
                         for(document in documents){
                             val text = document.get("text")as String
                             val user = document.get("user")as String
                             val chat = Chat(user,text)
                             chats.add(chat)
                             adapter.chats = chats
                         }
                     }
                         adapter.notifyDataSetChanged()
                 }
            }
        }

    }


}