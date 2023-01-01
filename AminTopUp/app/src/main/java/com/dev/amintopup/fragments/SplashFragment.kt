package com.dev.amintopup.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragSplashBinding
import com.dev.amintopup.network.LocalPreference

class SplashFragment : BaseFragment() {

    private var _binding: FragSplashBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragSplashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getToken = LocalPreference.shared.token ?: ""
        Handler(Looper.getMainLooper()).postDelayed({
            if (getToken.isEmpty()) {
                val action = SplashFragmentDirections.actionSplashToIntro()
                findNavController().navigate(action)
            } else {
                val action = SplashFragmentDirections.actionToHome()
                findNavController().navigate(action)
            }
        }, 4500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}