package com.e.droidhub.ui.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.e.droidhub.R
import com.e.droidhub.databinding.FragmentSignupBinding


class SplashScreenFragment : Fragment() {
    private val SPLASH_TIME_OUT = 3000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed(
                {
                    findNavController().navigate(R.id.loginFragment)
                }, SPLASH_TIME_OUT)
    }

}