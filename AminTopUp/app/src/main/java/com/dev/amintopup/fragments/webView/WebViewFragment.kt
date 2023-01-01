package com.dev.amintopup.fragments.webView

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragmentWebViewBinding


class WebViewFragment : BaseFragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val url: String
        get() {
            return arguments?.getString("URL", "") ?: ""
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d("Link", "Link ${request?.url?.toString()}")
                if (request?.url?.toString()
                        ?.contains("http://kodextech.net/amin-topup/public/sucess") == true
                ) {
                    mActivityDashboard?.isWebViewSuccess = true
                    findNavController().navigateUp()
                    return false
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                Log.d("Link", "Link ${request?.url?.toString()}")
                if (request?.url?.toString()
                        ?.contains("http://kodextech.net/amin-topup/public/sucess") == true
                ) {


                }
                return super.shouldInterceptRequest(view, request)
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.loadUrl(url)

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                binding.progress.progress = newProgress
                if (newProgress >= 100) {
                    binding.progress.visibility = View.GONE
                } else {
                    binding.progress.visibility = View.VISIBLE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)

            }

        }

//        binding.webView.webViewClient = object : WebViewClient() {
//            @Deprecated("Deprecated in Java")
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                if (url == "http://kodextech.net/amin-topup/public/sucess") {
//                    mActivityItem.runOnUiThread {
//                        mActivityItem.setResult(AppCompatActivity.RESULT_OK)
//                        mActivityItem.showToast("Success")
//
//                    }
//                }
//                return true
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTopBar() {
        binding.topBar.tvTitle.text = "Payment"
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}

//    private class MyBrowser(var mContext: AppCompatActivity) : WebViewClient() {
//
//        override fun shouldOverrideUrlLoading(
//            view: WebView?,
//            request: WebResourceRequest?
//        ): Boolean {
//            if (request?.url?.toString() == "http://kodextech.net/amin-topup/public/sucess") {
//                mContext.runOnUiThread {
//                    mContext.setResult(AppCompatActivity.RESULT_OK)
//                    mContext.showToast("Success0")
////                    mContext.startActivity(Intent(mContext,DashboardActivity::class.java))
////                    mContext.finish()
//                }
//            }
//            return true
//        }
//
//        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            view.loadUrl(url)
//
//            if (url == "http://kodextech.net/amin-topup/public/sucess") {
//                mContext.runOnUiThread {
//                    mContext.setResult(AppCompatActivity.RESULT_OK)
//                    mContext.showToast("Success1")
////                    mContext.startActivity(Intent(mContext,DashboardActivity::class.java))
//                }
//            }
//            return true
//        }
//
//        override fun onPageFinished(view: WebView?, url: String?) {
//            super.onPageFinished(view, url)
//            if (url!!.contains("success-url")) {
//                mContext.runOnUiThread {
//                    mContext.setResult(AppCompatActivity.RESULT_OK)
//                    mContext.showToast("Success2")
////                    mContext.startActivity(Intent(mContext,DashboardActivity::class.java))
//
//                }
//            }
//        }
//    }
