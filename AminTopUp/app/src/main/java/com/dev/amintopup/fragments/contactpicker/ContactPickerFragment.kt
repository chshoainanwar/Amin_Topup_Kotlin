package com.dev.amintopup.fragments.contactpicker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragContactListPickBinding
import com.dev.amintopup.fragments.home.homeModel.HomeModel
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.example.user.contactlist.view.ContactListFragment
import com.google.gson.Gson
import org.json.JSONObject

class ContactPickerFragment : BaseFragment() {

    private var _binding: FragContactListPickBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragContactListPickBinding.inflate(inflater, container, false)
        return binding.root

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
                val action = BottomGraphDirections.actionHFToDetailFragment(user,number)
                findNavController().navigate(action)
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?: "")
            }

        })

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, ContactListFragment.newInstance { itemContact ->
//                showToast(itemContact.phoneNumber)
//                val action = BottomGraphDirections.actionHFToDetailFragment()
//                action.number = itemContact.phoneNumber
//                action.operator
//                findNavController().navigate(action)
                callCheckTopUpDataApi(itemContact.phoneNumber)
            })
            .disallowAddToBackStack()
//            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            .commitAllowingStateLoss()
        binding.topBar.tvTitle.text = "Contacts"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        mActivityDashboard?.hideBtmBar(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}