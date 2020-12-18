package com.e.droidhub.ui.fragment

import android.content.ContentValues.TAG
import android.content.Context
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
import com.e.droidhub.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
        Log.d("log", "navigation to the login screen")
    }

    /*
     * Handling click events and other user interface logic should be done here
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.signUpText.setOnClickListener{
            findNavController().navigate(R.id.signUpFragment)
        }

        binding.loginButton.setOnClickListener {
            doLogin()
        }


    }

    fun doLogin(){
        var email = binding.email.text.toString()
        var password = binding.password.text.toString().trim()

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

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        updateUI(null)
                    }
                }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null) {
            if(currentUser.isEmailVerified) {
                findNavController().navigate(R.id.filesFragment)
            }else{
                Toast.makeText(
                        context, "Please verify your email address.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }else{
            Toast.makeText(context , "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
        }
    }

}