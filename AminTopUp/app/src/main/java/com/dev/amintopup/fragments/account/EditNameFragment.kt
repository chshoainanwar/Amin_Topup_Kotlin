package com.dev.amintopup.fragments.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragmentEditNameBinding
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class EditNameFragment : BaseFragment() {
    private var _binding: FragmentEditNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditNameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivityDashboard?.hideBtmBar(true)
        setTopBar()
        setNameField()
        setSurNameField()

        binding.btnSave.setOnClickListener {
            if (checkValidation()){
                callUpdateNameApi()
            }
        }

    }

    private fun callUpdateNameApi(){
        showLoader("")
        val userID = LocalPreference.shared.user!!.id
        val name = binding.rlEditName.etField.text.toString().trim()
        val surName = binding.rlEditSurName.etField.text.toString().trim()
        NetworkClass.callApi(URLApi().updateProfile(userID.toString(),"","","$name $surName","","",""), object : ResponseNetwork{
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                showToast("Name updated successfully")
//                val json = JSONObject(response ?: "")
//                val userString = json.optJSONObject("data")
//                val user =
//                    Gson().fromJson(userString?.toString() ?: "", UserItem::class.java)
//                LocalPreference.shared.user = user
//                findNavController().navigateUp()
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast("error $error")
            }

        })
    }

    private fun checkValidation(): Boolean {
        if (binding.rlEditName.etField.text!!.isEmpty()) {
            showToast("Please write name")
            return false
        } else if (binding.rlEditSurName.etField.text!!.isEmpty()) {
            showToast("Please write sur name")
            return false
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setTopBar() {
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.tvTitle.text = "Enter your name"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setNameField() {
        binding.rlEditName.tvLabel.text = "Name"
        binding.rlEditName.etField.hint = "Muhammad"
    }

    @SuppressLint("SetTextI18n")
    private fun setSurNameField() {
        binding.rlEditSurName.tvLabel.text = "Family Name"
        binding.rlEditSurName.etField.hint = "Ali"
    }
}