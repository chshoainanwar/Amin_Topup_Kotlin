package com.dev.amintopup.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.generateList
import com.dev.amintopup.databinding.FragmentHomeBinding
import com.dev.amintopup.fragments.home.adapter.ViewAllAdapter
import com.dev.amintopup.fragments.home.homeModel.HomeModel
import com.dev.amintopup.fragments.home.homeModel.ViewAllModel
import com.dev.amintopup.fragments.home.homeTopupModel.HomeTopupModel
import com.dev.amintopup.fragments.home.homeTopupModel.RecentTopup
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var list = ArrayList<RecentTopup>()
    private var mAdapter: ViewAllAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()
        setPhoneField()
        setRecyclerView()
        callTopUpListApi()

        mActivityDashboard?.setupBtmViews(BtmSelectedPosition.HOME, true)
        mActivityDashboard?.hideBtmBar(false)
        binding.tvSelectRecipient.setOnClickListener {
            val action = BottomGraphDirections.actionToPickContact()
            findNavController().navigate(action)
        }
        binding.rlHistory.setOnClickListener {
            mActivityDashboard?.setupBtmViews(BtmSelectedPosition.HISTORY)
        }
    }

    private fun setRecyclerView() {
        mAdapter = ViewAllAdapter(requireActivity() as BaseActivity, list) { tranId, pos ->
            val action =
                BottomGraphDirections.actionHistoryFragmentToConfirmationStatusFragment(tranId.toString())
            findNavController().navigate(action)
            showToast(tranId.toString())
        }
        binding.rvTopUp.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvTopUp.adapter = mAdapter
    }

    private fun callTopUpListApi() {
        showLoader("")
        NetworkClass.callApi(URLApi().topUpHistory(), object : ResponseNetwork {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                showToast("Success")
                binding.llList.visibility = View.VISIBLE
                binding.llNoTopUp.visibility = View.GONE
                val json = JSONObject(response ?: "data")
                val data =
                    Gson().fromJson(json.toString(), HomeTopupModel::class.java)
                list.clear()
                list.addAll(data.recentTopups ?: ArrayList())
                mAdapter?.notifyDataSetChanged()
                binding.tvYourTopUpAmount.text = data.WeeklyTopups.toString()
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                binding.llList.visibility = View.GONE
                binding.llNoTopUp.visibility = View.VISIBLE
            }

        })
    }

    private fun setTopBar() {
        binding.topBar.btnBack.setImageResource(R.drawable.ic_iv_menu)
        binding.topBar.btnRight.setImageResource(R.drawable.ic_iv_notification)
        binding.topBar.btnRight.visibility = View.VISIBLE
        binding.topBar.tvTitle.visibility = View.GONE

        binding.topBar.btnBack.setOnClickListener {
            mActivityDashboard?.openSideMenu()
        }
        binding.topBar.btnRight.setOnClickListener {
            mActivityDashboard?.openNotification()
        }

    }

    private fun setPhoneField() {
        binding.rlPhone.tvInfoField.visibility = View.GONE
        binding.rlPhone.tvError.visibility = View.GONE
        binding.rlPhone.rlRightArrow.visibility = View.VISIBLE
        binding.rlPhone.etField.hint = ""
        context?.let { binding.rlPhone.etField.setTextColor(it.getColor(R.color.arrow)) }
        binding.rlPhone.rlField.setBackgroundResource(R.drawable.bg_home_phone)
        binding.rlPhone.rlRightArrow.setBackgroundResource(R.drawable.circle_shape1)
        context?.let { binding.rlPhone.ivDropDown.setColorFilter(it.getColor(R.color.arrow)) }
        context?.let { binding.rlPhone.view.setBackgroundColor(it.getColor(R.color.arrow)) }
        binding.rlPhone.ccp.contentColor = mActivityItem.getColor(R.color.white)
        binding.rlPhone.ccp.registerCarrierNumberEditText(binding.rlPhone.etField);
        binding.rlPhone.ccp.setPhoneNumberValidityChangeListener { it ->
            if (it) {
                context?.let { it1 -> binding.rlPhone.etField.setTextColor(it1.getColor(R.color.white)) }
                binding.rlPhone.rlRightArrow.setBackgroundResource(R.drawable.circle_shape)
                context?.let { it1 -> binding.rlPhone.ivRightArrow.setColorFilter(it1.getColor(R.color.white)) }
                context?.let { binding.rlPhone.ivDropDown.setColorFilter(it.getColor(R.color.white)) }
                context?.let { binding.rlPhone.view.setBackgroundColor(it.getColor(R.color.white)) }
                binding.rlPhone.rlField.setBackgroundResource(R.drawable.bg_home_phone2)
                binding.rlPhone.rlRightArrow.setOnClickListener {
                    val strPhone = binding.rlPhone.etField.text.toString()
                    val strCode: String = binding.rlPhone.ccp.selectedCountryCode
                    val strFullPhone = strCode + strPhone
                    callCheckTopUpDataApi(strFullPhone)
                }
                // success
            } else {
                context?.let { it1 -> binding.rlPhone.etField.setTextColor(it1.getColor(R.color.arrow)) }
                binding.rlPhone.rlRightArrow.setBackgroundResource(R.drawable.circle_shape1)
                context?.let { it1 -> binding.rlPhone.ivRightArrow.setColorFilter(it1.getColor(R.color.darkStrokeAndTextClr)) }
                context?.let { binding.rlPhone.ivDropDown.setColorFilter(it.getColor(R.color.arrow)) }
                context?.let { binding.rlPhone.view.setBackgroundColor(it.getColor(R.color.arrow)) }
                binding.rlPhone.rlField.setBackgroundResource(R.drawable.bg_home_phone)
            }
            // false
        }
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
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?: "")
            }

        })

    }
}