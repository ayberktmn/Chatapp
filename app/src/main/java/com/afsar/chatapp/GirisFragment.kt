package com.afsar.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afsar.chatapp.databinding.FragmentGirisBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class GirisFragment : Fragment() {

    private var _binding : FragmentGirisBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val currentUser = auth.currentUser



        if(currentUser !=null){
            val action = GirisFragmentDirections.actionGirisFragmentToChatFragment()
            findNavController().navigate(action)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGirisBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.girisBtn.setOnClickListener{

            if(binding.EmailText.getText().toString().trim().equals("") || binding.SifreText.getText().toString().trim().equals("") ){
                Toast.makeText(requireContext(),"Email veya Şifre Boş Geçilemez",Toast.LENGTH_LONG).show()
            }else{

            auth.signInWithEmailAndPassword(binding.EmailText.text.toString(),binding.SifreText.text.toString()).addOnSuccessListener {

                val action = GirisFragmentDirections.actionGirisFragmentToChatFragment()
                findNavController().navigate(action)

            }.addOnFailureListener { exception->
                Toast.makeText(requireContext(),"Bu Kullanıcı Kayıtlı Değil",Toast.LENGTH_LONG).show()

              }
             }


        }

        binding.kayitBtn.setOnClickListener {

            if(binding.EmailText.getText().toString().trim().equals("") || binding.SifreText.getText().toString().trim().equals("") ) {
                Toast.makeText(
                    requireContext(),
                    "Email veya Şifre Boş Geçilemez",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                auth.createUserWithEmailAndPassword(
                    binding.EmailText.text.toString(),
                    binding.SifreText.text.toString()
                ).addOnSuccessListener {

                    val action = GirisFragmentDirections.actionGirisFragmentToChatFragment()
                    findNavController().navigate(action)

                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Kullanıcı Kaydı oluşturuldu", Toast.LENGTH_LONG)
                        .show()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
