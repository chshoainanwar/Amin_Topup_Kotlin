package com.dev.amintopup.fragments.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.BuildConfig
import com.dev.amintopup.R
import com.dev.amintopup.base.*
import com.dev.amintopup.databinding.FragLoginBinding
import com.dev.amintopup.fragments.home.HomeActivity
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : BaseFragment() {

    private var _binding: FragLoginBinding? = null
    private val txtEmail: AppCompatEditText
        get() {
            return binding.rlEmail.etField
        }
    private val txtPassword: AppCompatEditText
        get() {
            return binding.rlPassword.etField
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEmail()
        setupPassword()
        checkValidation()
        HideUtil.init(mActivityItem)
        binding.btnForgot.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToForgot()
            findNavController().navigate(action)
        }
        binding.btnLogin.setOnClickListener {
            val strEmail = binding.rlEmail.etField.text.toString().trim()
            val strPassword = binding.rlPassword.etField.text.toString().trim()
            loginApi(strEmail, strPassword)
        }
        binding.tvToSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToSignup()
            findNavController().navigate(action)
        }
        if (BuildConfig.DEBUG) {
//            binding.rlEmail.etField.setText("ab@cc.com")
//            binding.rlPassword.etField.setText("ab@cc.comxasxasx")
            checkValidation()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginApi(email: String, password: String) {
        mActivityItem.showLoader("")
        NetworkClass.callApi(URLApi().login(email, password), object : ResponseNetwork {
            override fun onSuccessResponse(response: String?, message: String) {
                mActivityItem.hideLoader()
                val json = JSONObject(response ?: "")
                val userString = json.optJSONObject("data") ?: json.optJSONObject("user")
                val user =
                    Gson().fromJson(userString.toString(), UserItem::class.java)
                LocalPreference.shared.user = user
                LocalPreference.shared.token = user.token
                startActivity(Intent(mActivityItem, HomeActivity::class.java))
                mActivityItem.finish()
            }

            override fun onErrorResponse(error: String?, response: String?) {
                mActivityItem.hideLoader()
                mActivityItem.showToast(error ?: "Error")
            }

        })
    }

    fun checkValidation() {
        if (txtEmail.text.toString().trim().isValidEmail() && txtPassword.text.toString().trim()
                .isValidPassword()
        ) {
            binding.btnLogin.setBackgroundResource(R.drawable.btn_bg_enabled)
            binding.btnLogin.isEnabled = true
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.white))
        } else {

            binding.btnLogin.isEnabled = false
            binding.btnLogin.setBackgroundResource(R.drawable.bg_btn)
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.darkStrokeAndTextClr))
        }
    }

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    private fun setupPassword() {
        txtPassword.hint = "Enter your password"
//        txtPassword.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        binding.rlPassword.tvInfoField.text = "Password"
        binding.rlPassword.tvError.text = "Password should be at least 8 Characters!"
        binding.rlPassword.ivFields.setImageResource(R.drawable.ic_pass_visible)
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim()
                if (txt.isValidPassword()) {
                    binding.rlPassword.tvError.viewInVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPassword.tvError.viewVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_error_field)
                }
                checkValidation()
            }
        })
        binding.rlPassword.ivFields.setOnClickListener {
            if (txtPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.rlPassword.ivFields.setImageResource(R.drawable.ic_pass_visible)
            } else {
                txtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.rlPassword.ivFields.setImageResource(R.drawable.ic_pass_invisible)
            }
            txtPassword.setSelection(txtPassword.length())
        }
        txtPassword.setOnFocusChangeListener { _, hasFocus ->
            val txt = txtPassword.text.toString().trim()
            if (hasFocus) {
                if (txt.isValidPassword()) {
                    binding.rlPassword.tvError.viewInVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPassword.tvError.viewVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_focus_field)
                }
            } else {
                if (txt.isValidPassword()) {
                    binding.rlPassword.tvError.viewInVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPassword.tvError.viewVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_simple_field)
                }
            }
        }
    }

}