package com.dev.amintopup.fragments.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragmentHistoryBinding
import com.dev.amintopup.fragments.history.adapter.HistoryAdapter
import com.dev.amintopup.fragments.history.model.AllTopup
import com.dev.amintopup.fragments.history.model.AllTopupModel
import com.dev.amintopup.fragments.home.BtmSelectedPosition
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class HistoryFragment : BaseFragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var mItem = ArrayList<AllTopup>()
    private var mAdapter: HistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivityDashboard?.hideBtmBar(false)
        mActivityDashboard?.setupBtmViews(BtmSelectedPosition.HISTORY, true)
        setRecyclerView()
        callTopUpHistoryApi()

    }

    private fun setRecyclerView() {
        mAdapter = HistoryAdapter(
            requireActivity() as BaseActivity,
            mItem
        ) { tranId, pos ->
            val action =
                BottomGraphDirections.actionHistoryFragmentToConfirmationStatusFragment(
                    tranId.toString()
                )
            findNavController().navigate(action)
        }
        binding.rvTopUp.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvTopUp.adapter = mAdapter
    }

    private fun callTopUpHistoryApi() {
        showLoader("")
        NetworkClass.callApi(URLApi().allTopUpList(), object : ResponseNetwork {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                showToast("Success")
                binding.main.visibility = View.VISIBLE
                binding.llReport.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                val json = JSONObject(response ?: "")
                val data =
                    Gson().fromJson(json.toString(), AllTopupModel::class.java)
                mItem.clear()
                mItem.addAll(data.allTopups ?: ArrayList())
                mAdapter?.notifyDataSetChanged()
                binding.tvAFNAmount.text = data.totalTopupAmount.toString()

            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                binding.main.visibility = View.GONE
                binding.llReport.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }

        })
    }

}