package com.dev.amintopup.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.HideUtil
import com.dev.amintopup.databinding.FragVerifyPinBinding
import com.dev.amintopup.fragments.auth.VerifyPinModel.VerifyPinModel
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import com.poovam.pinedittextfield.PinField
import com.poovam.pinedittextfield.SquarePinField
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class VerifyPinFragment : BaseFragment() {

    private var _binding: FragVerifyPinBinding? = null
    var getEmail: String? = null
    var getOTP: String? = null
    var enteredOTP: String? = null
    private val txtPin: SquarePinField
        get() {
            return binding.squareField
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragVerifyPinBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getEmail = arguments?.getString("email")
        getOTP = arguments?.getString("otp")

        setupPin()
        HideUtil.init(mActivityItem)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLogin.isEnabled = false
        binding.btnLogin.setOnClickListener {
            verifyOTPApi()
        }
        binding.btnResend.setOnClickListener {
            sendOTPApi()
        }
    }

    private fun verifyOTPApi() {
        showLoader("")
        NetworkClass.callApi(
            URLApi().verifyOTP(getEmail!!, enteredOTP!!),
            object : ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    hideLoader()
                    val json = JSONObject(response ?: "")
                    val userString = json.optJSONObject("data") ?: json.optJSONObject("user")
                    val user =
                        Gson().fromJson(userString.toString(), UserItem::class.java)
                    LocalPreference.shared.user = user
                    LocalPreference.shared.token = user.token
                    showToast( "OTP verified successfully")
                    val bundle = Bundle()
                    bundle.putString("email", getEmail)
                    findNavController().navigate(R.id.actionVerifyToNewPassword, bundle)
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    hideLoader()
                    showToast(error ?: "")
                }

            })
    }

    private fun sendOTPApi() {
        showLoader("")
        NetworkClass.callApi(URLApi().frogotPassword(getEmail!!), object : ResponseNetwork {
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                val otp = response.toString()
                showToast("OTP is $otp")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkValidation(pin: String): Boolean {
        if (pin.length == 4) {
            enteredOTP = pin
            binding.btnLogin.setBackgroundResource(R.drawable.btn_bg_enabled)
            binding.btnLogin.isEnabled = true
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.white))
            txtPin.highlightPaintColor = mActivityItem.getColor(R.color.greenClr)
            return true
        } else {
            binding.btnLogin.isEnabled = false
            binding.btnLogin.setBackgroundResource(R.drawable.bg_btn)
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.darkStrokeAndTextClr))
            txtPin.highlightPaintColor = mActivityItem.getColor(R.color.redClr)
            return false
        }
    }

    private fun setupPin() {
        txtPin.onTextCompleteListener = object : PinField.OnTextCompleteListener {
            override fun onTextComplete(enteredText: String): Boolean {
                return checkValidation(enteredText)
            }

        }

    }
}