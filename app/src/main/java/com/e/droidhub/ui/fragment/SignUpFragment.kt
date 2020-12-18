package com.e.droidhub.ui.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.e.droidhub.R
import com.e.droidhub.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {5
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            signUpuser()
        }
    }

    private fun signUpuser () {
        var name = binding.name.text.toString()
        var email = binding.email.text.toString()
        var password = binding.password.text.toString().trim()

        if(name.isEmpty()){
            binding.name.error = "Please enter Full Name"
            binding.name.requestFocus()
            return
        }

        if(email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.error = "Please enter Valid Email"
            binding.email.requestFocus()
            return
        }

        if(password.isEmpty() && password.length < 6){
            binding.password.error = "6 character password required"
            binding.password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "Email sent.")
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success")
                                    findNavController().navigate(R.id.loginFragment)
                                }
                            }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed. Try again after some time",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }



}