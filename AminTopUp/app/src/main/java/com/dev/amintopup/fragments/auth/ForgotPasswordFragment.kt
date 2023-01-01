package com.dev.amintopup.fragments.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.R
import com.dev.amintopup.base.*
import com.dev.amintopup.databinding.FragFrogotPasswordBinding
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ForgotPasswordFragment : BaseFragment() {

    private var _binding: FragFrogotPasswordBinding? = null
    private val txtEmail: AppCompatEditText
        get() {
            return binding.rlEmail.etField
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragFrogotPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEmail()
        checkValidation()
        HideUtil.init(mActivityItem)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLogin.setOnClickListener {
            sendOTPApi()
        }
    }

    private fun sendOTPApi() {
        showLoader("")
        val email = binding.rlEmail.etField.text.toString()
        NetworkClass.callApi(URLApi().frogotPassword(email), object : ResponseNetwork {
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                val otp = response.toString()
                showToast("OTP is $otp")
                val bundle = Bundle()
                bundle.putString("email", email)
                bundle.putString("otp", otp)
                findNavController().navigate(R.id.actionForgotToVerify, bundle)
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?:" ")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkValidation() {
        if (txtEmail.text.toString().trim().isValidEmail()) {
            binding.btnLogin.setBackgroundResource(R.drawable.btn_bg_enabled)
            binding.btnLogin.isEnabled = true
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.white))
        } else {
            binding.btnLogin.isEnabled = false
            binding.btnLogin.setBackgroundResource(R.drawable.bg_btn)
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.darkStrokeAndTextClr))
        }
    }

    private fun setupEmail() {
        txtEmail.hint = "Enter your e-Mail"
        txtEmail.inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.rlEmail.tvInfoField.text = "E-Mail"
        binding.rlEmail.tvError.text = "Enter valid E-Mail"
        binding.rlEmail.ivFields.setImageResource(R.drawable.ic_mail)
        txtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim()
                if (txt.isValidEmail()) {
                    binding.rlEmail.tvError.viewInVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlEmail.tvError.viewVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_error_field)
                }
                checkValidation()
            }
        })

        txtEmail.setOnFocusChangeListener { _, hasFocus ->
            val txt = txtEmail.text.toString().trim()
            if (hasFocus) {
                if (txt.isValidEmail()) {
                    binding.rlEmail.tvError.viewInVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlEmail.tvError.viewVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_focus_field)
                }
            } else {
                if (txt.isValidEmail()) {
                    binding.rlEmail.tvError.viewInVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlEmail.tvError.viewVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_simple_field)
                }
            }
        }
    }
}