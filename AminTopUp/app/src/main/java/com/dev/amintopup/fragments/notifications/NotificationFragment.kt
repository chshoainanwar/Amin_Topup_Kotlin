package com.dev.amintopup.fragments.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.generateList
import com.dev.amintopup.databinding.FragNotificationBinding
import com.dev.amintopup.fragments.notifications.adapter.NotificationAdapter
import com.dev.amintopup.fragments.notifications.model.Data
import com.dev.amintopup.fragments.notifications.model.NotificationModel
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class NotificationFragment : BaseFragment() {

    private var _binding: FragNotificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var list = ArrayList<Data>()
    private var mAdapter : NotificationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragNotificationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.srl.setOnRefreshListener {
            binding.srl.isRefreshing = false
        }

        setRecyclerView()
        callNotificationApi()

        binding.topBar.tvTitle.text = "Notification"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        mActivityDashboard?.hideBtmBar(true)
    }

    private fun setRecyclerView(){
        binding.rvNotification.layoutManager =
            LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
        binding.rvNotification.adapter =
            NotificationAdapter(list, mActivityItem) {

            }
    }

    private fun callNotificationApi(){
        showLoader("")
        NetworkClass.callApi(URLApi().notifications(),object : ResponseNetwork{
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                binding.btmView.visibility = View.VISIBLE
                val json = JSONArray(response?:"data")
                val allBannersList =
                    generateList(json?.toString() ?: "", Array<Data>::class.java)
                list.clear()
                list.addAll(allBannersList)
                mAdapter?.notifyDataSetChanged()
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error?:"")
                binding.btmView.visibility = View.GONE
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}