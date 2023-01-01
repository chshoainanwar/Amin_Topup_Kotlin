package com.dev.amintopup.fragments.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.R
import com.dev.amintopup.base.*
import com.dev.amintopup.databinding.FragNewPasswordBinding
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NewPasswordFragment : BaseFragment() {

    private var _binding: FragNewPasswordBinding? = null
    var getEmail: String? = null
    private val txtPassword: AppCompatEditText
        get() {
            return binding.rlPassword.etField
        }

    private val txtPasswordConfirm: AppCompatEditText
        get() {
            return binding.rlPasswordConfirm.etField
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragNewPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPassword()
        setupPasswordConfirm()
        checkValidation()
        HideUtil.init(mActivityItem)

        getEmail = arguments?.getString("email")

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLogin.setOnClickListener {
            callResetPasswordApi()
        }
    }

    private fun callResetPasswordApi(){
        val password = binding.rlPassword.etField.text.toString()
        showLoader("")
        NetworkClass.callApi(
            URLApi().resetPassword(getEmail!!, password),
            object : ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    hideLoader()
                    val frag = SuccessPasswordDialogue()
                    frag.onCallBackForRedirection = {
                        findNavController().navigate(
                            R.id.actionToLogin, null,
                            NavOptions.Builder().setPopUpTo(R.id.LoginFragment, true)
                                .build()
                        )
                    }
                    frag.show(childFragmentManager, frag.toString())
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    hideLoader()
                    showToast(error ?: "")
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkValidation() {
        if (txtPassword.text.toString().trim().isValidPassword() &&
            txtPasswordConfirm.text.toString().trim().isValidPassword() &&
            txtPassword.text.toString().trim() == txtPassword.text.toString().trim()
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

    private fun setupPassword() {
        txtPassword.hint = "Enter your new password"
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
            txtPassword.setSelection(txtPassword.length());
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

    @SuppressLint("SetTextI18n")
    private fun setupPasswordConfirm() {
        txtPasswordConfirm.hint = "Enter your password again"
        binding.rlPasswordConfirm.tvInfoField.text = "Confirm Password"
        binding.rlPasswordConfirm.tvError.text = "Password should be at least 8 Characters!"
        binding.rlPasswordConfirm.ivFields.setImageResource(R.drawable.ic_pass_visible)
        txtPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim()
                if (txt.isValidPassword()) {
                    binding.rlPasswordConfirm.tvError.viewInVisible()
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_success_field)
                    if (txt != txtPassword.text.toString().trim()) {
                        binding.rlPasswordConfirm.tvError.text =
                            "Password and Confirm password must be matched"
                        binding.rlPasswordConfirm.tvError.viewVisible()
                        binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_error_field)
                    } else {
                        binding.rlPasswordConfirm.tvError.text =
                            "Password should be at least 8 Characters!"
                        binding.rlPasswordConfirm.tvError.viewInVisible()
                        binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_success_field)
                    }
                } else if (txt.length > 1) {
                    binding.rlPasswordConfirm.tvError.text =
                        "Password should be at least 8 Characters!"
                    binding.rlPasswordConfirm.tvError.viewVisible()
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_error_field)
                }
                checkValidation()
            }
        })
        binding.rlPasswordConfirm.ivFields.setOnClickListener {
            if (txtPasswordConfirm.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                txtPasswordConfirm.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.rlPasswordConfirm.ivFields.setImageResource(R.drawable.ic_pass_visible)
            } else {
                txtPasswordConfirm.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.rlPasswordConfirm.ivFields.setImageResource(R.drawable.ic_pass_invisible)
            }
            txtPasswordConfirm.setSelection(txtPasswordConfirm.length());
        }
        txtPasswordConfirm.setOnFocusChangeListener { _, hasFocus ->
            val txt = txtPasswordConfirm.text.toString().trim()
            if (hasFocus) {
                if (txt.isValidPassword()) {
                    binding.rlPasswordConfirm.tvError.viewInVisible()
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_success_field)
                    if (txt != txtPassword.text.toString().trim()) {
                        binding.rlPasswordConfirm.tvError.text =
                            "Password and Confirm password must be matched"
                        binding.rlPasswordConfirm.tvError.viewVisible()
                        binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_error_field)
                    } else {
                        binding.rlPasswordConfirm.tvError.text =
                            "Password should be at least 8 Characters!"
                        binding.rlPasswordConfirm.tvError.viewInVisible()
                        binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_success_field)
                    }
                } else if (txt.length > 1) {
                    binding.rlPasswordConfirm.tvError.viewVisible()
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_focus_field)
                }
            } else {
                if (txt.isValidPassword()) {
                    binding.rlPasswordConfirm.tvError.viewInVisible()
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPasswordConfirm.tvError.viewVisible()
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlPasswordConfirm.rlField.setBackgroundResource(R.drawable.bg_simple_field)
                }
            }
        }
    }

}