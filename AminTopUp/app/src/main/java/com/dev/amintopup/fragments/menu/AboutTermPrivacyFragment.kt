package com.dev.amintopup.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.viewInVisible
import com.dev.amintopup.base.viewVisible
import com.dev.amintopup.databinding.FragAboutTermPrivacyBinding
import com.dev.amintopup.databinding.TopBarLayoutBinding

class AboutTermPrivacyFragment : BaseFragment() {

    private var _binding: FragAboutTermPrivacyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val title: String
        get() {
            return arguments?.getString("TITLE", "") ?: ""
        }

    private val link: String
        get() {
            return arguments?.getString("LINK", "") ?: ""
        }

    //    private val webView: WebView
//        get() {
//            return binding.webView
//        }
    private val topView: TopBarLayoutBinding
        get() {
            return binding.topBar
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragAboutTermPrivacyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        topView.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        topView.btnBack.viewVisible()
        topView.btnRight.viewInVisible()
        topView.tvTitle.text = title
//        webView.loadUrl(link)
        mActivityDashboard?.hideBtmBar(true)
    }

    private fun setData() {
        when (title) {
            "Privacy Policy" -> {
                binding.tvHead1.text = mActivityItem.getString(R.string.Privacy1)
                binding.tvHead2.text = mActivityItem.getString(R.string.Privacy3)
                binding.tvHead3.text = mActivityItem.getString(R.string.Privacy5)
                binding.tvHead4.text = mActivityItem.getString(R.string.Privacy7)
                binding.tvDesc1.text = mActivityItem.getString(R.string.Privacy2)
                binding.tvDesc2.text = mActivityItem.getString(R.string.Privacy4)
                binding.tvDesc3.text = mActivityItem.getString(R.string.Privacy6)
                binding.tvDesc4.text = mActivityItem.getString(R.string.Privacy8)
            }
            "Terms & Condition" -> {
                binding.tvHead1.text = mActivityItem.getString(R.string.Terms1)
                binding.tvHead2.text = mActivityItem.getString(R.string.Terms3)
                binding.tvHead3.text = mActivityItem.getString(R.string.Terms5)
                binding.tvHead4.text = mActivityItem.getString(R.string.Terms7)
                binding.tvDesc1.text = mActivityItem.getString(R.string.Terms2)
                binding.tvDesc2.text = mActivityItem.getString(R.string.Terms4)
                binding.tvDesc3.text = mActivityItem.getString(R.string.Terms6)
                binding.tvDesc4.text = mActivityItem.getString(R.string.Terms8)
            }
            "About AminTopup" -> {
                binding.tvHead1.visibility = View.GONE
                binding.tvHead2.visibility = View.GONE
                binding.tvHead3.visibility = View.GONE
                binding.tvHead4.visibility = View.GONE
                binding.tvDesc1.text = mActivityItem.getString(R.string.About1)
                binding.tvDesc2.text = mActivityItem.getString(R.string.About2)
                binding.tvDesc3.text = mActivityItem.getString(R.string.About3)
                binding.tvDesc4.text = mActivityItem.getString(R.string.About4)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}