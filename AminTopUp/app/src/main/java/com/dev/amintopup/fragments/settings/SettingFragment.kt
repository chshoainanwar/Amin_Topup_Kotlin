package com.dev.amintopup.fragments.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragSettingsBinding
import com.dev.amintopup.fragments.adapters.LanguagePickerAdapter
import java.util.*

class SettingFragment : BaseFragment() {

    private var _binding: FragSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var languages: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeIO.run {
            languages.clear()
            languages.addAll(getLanguages())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.tvTitle.text = "Setting"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        mActivityDashboard?.hideBtmBar(true)
        binding.llForCountry.setOnClickListener {
            val toolTip = mActivityItem.showToolTip(it, Gravity.BOTTOM, R.layout.popup, "Teom")
            val rv = toolTip.findViewById<RecyclerView>(R.id.rlCountryList)
            rv.layoutManager = LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
            rv.adapter = LanguagePickerAdapter(languages, mActivityItem) { selectedCountry ->
                binding.tvLanguageValue.text = selectedCountry
//                binding.rlPhone.ivFields.setImageResource(selectedCountry.flagResource)
                toolTip.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun getLanguages(): ArrayList<String> {
//    val locales: Array<String> = Resources.getSystem().assets.locales
    val list: ArrayList<String> = ArrayList()
    for (locale in Locale.getAvailableLocales()) {
        if (locale.language.length == 2) {
            if (!isLanguageInList(list, locale)) {
                list.add(locale.displayLanguage)
            }
        }
    }
    list.sort()
    return list
}

private fun isLanguageInList(list: List<String>?, locale: Locale): Boolean {
    if (list == null) {
        return false
    }
    for (item in list) {
        if (item.equals(locale.displayLanguage, ignoreCase = true)) {
            return true
        }
    }
    return false
}