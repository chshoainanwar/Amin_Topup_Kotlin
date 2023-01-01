package com.dev.amintopup.fragments.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragmentEditBirthdayBinding
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject
import sh.tyy.wheelpicker.DatePickerView

class EditBirthdayFragment : BaseFragment() {
    private var _binding: FragmentEditBirthdayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBirthdayBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()
        setBirthdayField()
        mActivityDashboard?.hideBtmBar(true)

    }

    @SuppressLint("SetTextI18n")
    private fun setTopBar() {
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.tvTitle.text = "Enter your Birthday"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnConfirm.setOnClickListener {
            if (checkValidation()){
                callUpdateBirthApi()
            }
        }
    }

    private fun callUpdateBirthApi(){
        showLoader("")
        val userID = LocalPreference.shared.user!!.id
        val birth = binding.rlAddBirthday.etField.text.toString().trim()
        NetworkClass.callApi(URLApi().updateProfile(userID.toString(),"","","",birth,"",""), object : ResponseNetwork{
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                showToast("Birthday updated successfully")
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

    private fun checkValidation(): Boolean {
        if (binding.rlAddBirthday.etField.text!!.isEmpty()) {
            showToast("Please select birth date")
            return false
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setBirthdayField() {
        binding.rlAddBirthday.tvLabel.visibility = View.GONE
        binding.rlAddBirthday.etField.setText("17 / 07 / 1983")
        binding.dayTimePickerView.setWheelListener(object : DatePickerView.Listener {
            override fun didSelectData(year: Int, month: Int, day: Int) {
                binding.rlAddBirthday.etField.setText("$day / $month / $year")
            }
        })
    }

}