package com.dev.amintopup.fragments.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.viewInVisible
import com.dev.amintopup.base.viewVisible
import com.dev.amintopup.databinding.FragmentEditPhoneBinding
import com.dev.amintopup.databinding.FragmentOrderSummaryBinding
import com.dev.amintopup.fragments.home.homeModel.Detail
import com.dev.amintopup.fragments.home.homeModel.HomeModel
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class EditPhoneFragment : BaseFragment() {
    private var _binding: FragmentEditPhoneBinding? = null
    private val binding get() = _binding!!
    var strPhone: String? = null
    var strFullPhone: String? = null
    private var from: String = ""
    private var getName: String = ""
    private var getEmail: String = ""
    private var getPayment: String = ""
    var list: Detail = Detail()
    var getNetworkData: HomeModel = HomeModel()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditPhoneBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        from = arguments?.getString("From", "") ?: ""
        getName = arguments?.getString("name", "") ?: ""
        getEmail = arguments?.getString("email", "") ?: ""
        getPayment = arguments?.getString("amount","") ?: ""
        list = arguments?.getSerializable("list") as Detail
        getNetworkData = arguments?.getSerializable("operator") as HomeModel

        setTopBar()
        setPhoneField()

    }

    private fun callCheckTopUpDataApi(number: String) {

        showLoader("")
        NetworkClass.callApi(URLApi().checkTopUpData(number), object : ResponseNetwork {
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                val json = JSONObject(response ?: "data")
                val operatorData = json.optJSONObject("network") ?: ""
                val user =
                    Gson().fromJson(operatorData.toString(), HomeModel::class.java)
                val action = BottomGraphDirections.actionHFToDetailFragment(user, number)
                findNavController().navigate(action)

                when (from) {
                    "HomeAmount" -> {
                        val bundle = Bundle()
                        bundle.putString("number", strPhone)
                        bundle.putSerializable("operator", user)
                        bundle.putSerializable("name", getName)
                        bundle.putSerializable("email", getName)
                        findNavController().navigate(
                            R.id.action_editPhoneFragment_to_homeAmountFragment2,
                            bundle
                        )
                    }

                    "OrderSummary" -> {
                        val bundle = Bundle()
                        bundle.putSerializable("operator", getNetworkData)
                        bundle.putSerializable("list", list)
                        bundle.putString("number", number)
                        bundle.putString("amount", getPayment)
                        bundle.putString("email", getEmail)
                        bundle.putString("name", getName)
                        findNavController().navigate(
                            R.id.action_editPhoneFragment_to_orderSummaryFragment2,
                            bundle
                        )
                    }
                }
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?: "")
            }

        })

    }

    private fun setPhoneField() {
        binding.rlPhone.tvInfoField.visibility = View.GONE
        binding.rlPhone.tvError.visibility = View.GONE
        binding.rlPhone.rlRightArrow.visibility = View.GONE
        binding.rlPhone.etField.hint = ""
        binding.rlPhone.ccp.contentColor = mActivityItem.getColor(R.color.white)
        context?.let { it1 -> binding.rlPhone.etField.setTextColor(it1.getColor(R.color.white)) }
        binding.rlPhone.rlRightArrow.setBackgroundResource(R.drawable.circle_shape1)
        context?.let { it1 -> binding.rlPhone.ivRightArrow.setColorFilter(it1.getColor(R.color.white)) }
        context?.let { binding.rlPhone.ivDropDown.setColorFilter(it.getColor(R.color.white)) }
        context?.let { binding.rlPhone.view.setBackgroundColor(it.getColor(R.color.white)) }
        binding.rlPhone.rlField.setBackgroundResource(R.drawable.bg_home_phone2)
        binding.rlPhone.ccp.registerCarrierNumberEditText(binding.rlPhone.etField);

        binding.rlPhone.ccp.setPhoneNumberValidityChangeListener {
            if (it) {
                binding.btnSave.setOnClickListener {
                    strPhone = binding.rlPhone.etField.text.toString()
                    val strCode: String = binding.rlPhone.ccp.defaultCountryCode
                    strFullPhone = strPhone + strCode
                    if (strPhone == null) {
                        showToast("Please write correct phone number")
                    } else {
                        callCheckTopUpDataApi(strPhone!!)
                    }
                }
                // success
            } else {
                // false
            }
        }
    }

    private fun setTopBar() {
        binding.topBar.tvTitle.visibility = View.INVISIBLE
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}