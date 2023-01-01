package com.dev.amintopup.fragments.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.isValidEmail
import com.dev.amintopup.databinding.FragmentEditEmailBinding
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class EditEmailFragment : BaseFragment() {
    private var _binding: FragmentEditEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentEditEmailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()
        setEmailField()
        mActivityDashboard?.hideBtmBar(true)
        binding.btnSave.setOnClickListener {
            val email = binding.rlEditEmail.etField.text.toString().trim()
            if (email.isValidEmail()){
                callUpdateEmailApi(email)
            }else{
                showToast("Please write correct email")
            }
        }
    }

    private fun callUpdateEmailApi(email : String){
        showLoader("")
        val userID = LocalPreference.shared.user!!.id
        NetworkClass.callApi(URLApi().updateProfile(userID.toString(),"email","","","","",""), object :
            ResponseNetwork {
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                showToast("Email updated successfully")
                val json = JSONObject(response ?: "")
                val userString = json.optJSONObject("data")
                val user =
                    Gson().fromJson(userString?.toString() ?: "", UserItem::class.java)
                LocalPreference.shared.user = user
                findNavController().navigateUp()
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error?:"")
            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun setTopBar() {
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.tvTitle.text = "Enter your Name"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setEmailField(){
        binding.rlEditEmail.tvLabel.text = "E mail"
        binding.rlEditEmail.etField.setText("amintoup@gmail.com")
    }



}