package com.dev.amintopup.fragments.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragmentEditCountryBinding
import com.dev.amintopup.fragments.adapters.CountryPickerAdapter
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class EditCountryFragment : BaseFragment() {
    var country: ArrayList<Country> = ArrayList()
    private var _binding: FragmentEditCountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentEditCountryBinding.inflate(inflater, container, false)
        country.addAll(World.getAllCountries())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()

        mActivityDashboard?.hideBtmBar(true)
        val firstCountry = country.first()
        setCountryField(firstCountry)

        binding.rlCountry.rlField.setOnClickListener {
            val toolTip = mActivityItem.showToolTip(it, Gravity.BOTTOM, R.layout.popup, "Teom")
            val rv = toolTip.findViewById<RecyclerView>(R.id.rlCountryList)
            rv.layoutManager = LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
            rv.adapter = CountryPickerAdapter(country, mActivityItem) { selectedCountry ->
                binding.rlCountry.etField.setText(selectedCountry.name)
                toolTip.dismiss()
            }
        }

        binding.btnSave.setOnClickListener {
            val country = binding.rlCountry.etField.text.toString().trim()
            callUpdateCountryApi(country)
        }
    }

    private fun callUpdateCountryApi(country : String){
        showLoader("")
        val userID = LocalPreference.shared.user!!.id
        NetworkClass.callApi(URLApi().updateProfile(userID.toString(),"","","","","",country), object :
            ResponseNetwork {
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                showToast("Country updated successfully")
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
        binding.topBar.tvTitle.text = "Country"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setCountryField(firstCountry: Country){
        binding.rlCountry.tvInfoField.visibility = View.GONE
        binding.rlCountry.rlField.setBackgroundResource(R.drawable.bg_edit_field)
        context?.let { binding.rlCountry.etField.setTextColor(it.getColor(R.color.white)) }
        binding.rlCountry.etField.setText(firstCountry.name)
    }

}