package com.dev.amintopup.fragments.contactus

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragContactUsBinding
import com.dev.amintopup.fragments.contactus.adapter.CategoryAdapter
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi

class ContactUsFragment : BaseFragment() {
    var category: ArrayList<String> = ArrayList()
    private var _binding: FragContactUsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragContactUsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setSubjectField()
        category.add("General");
        category.add("Refund Request");
        category.add("Transaction Enquiry");
        category.add("Sales");
        category.add("Feature Request");
        category.add("Report a Technical Problem");
        val firstCategory = category.first()
        setCategoryField(firstCategory)
        binding.rlCategory.etField.isEnabled = true
        binding.rlCategory.etField.isFocusable = false;
        binding.rlCategory.etField.isClickable = true
        binding.rlCategory.etField.setOnClickListener {
            binding.rlCategory.rlField.performClick()
        }
        binding.rlCategory.rlField.setOnClickListener {
            val toolTip =
                mActivityItem.showToolTip(it, Gravity.BOTTOM, R.layout.layout_category, "Teom")
            val rv = toolTip.findViewById<RecyclerView>(R.id.rlCountryList)
            rv.layoutManager = LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
            rv.adapter = CategoryAdapter(mActivityItem, category) { selectedCategory ->
                binding.rlCategory.etField.setText(selectedCategory)
                toolTip.dismiss()
            }
        }

        binding.topBar.tvTitle.text = "Contact us"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        mActivityDashboard?.hideBtmBar(true)

        binding.btnLogin.setOnClickListener {
            if (checkValidation()) {
                val subject = binding.rlSubject.etField.text.toString().trim()
                val category = binding.rlCategory.etField.text.toString().trim()
                val description = binding.rlDescription.etField.text.toString().trim()
                callContactUs(subject, category, description)
            }
        }

    }

    private fun callContactUs(subject: String, category: String, description: String) {
        showLoader("")
        NetworkClass.callApi(URLApi().contactUS(subject, category, description),
            object : ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    hideLoader()
                    showToast(message)
                    findNavController().navigateUp()
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    hideLoader()
                    showToast(error ?: "")
                }

            })
    }

    private fun checkValidation(): Boolean {
        if (binding.rlSubject.etField.text!!.isEmpty()) {
            showToast("Please write subject")
            return false
        } else if (binding.rlDescription.etField.text!!.isEmpty()) {
            showToast("Please write description")
            return false
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setSubjectField() {
        binding.rlSubject.tvInfoField.text = "Subject"
        binding.rlSubject.etField.inputType = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
        binding.rlSubject.etField.hint = "Type Here.."
        binding.rlSubject.ivFields.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setCategoryField(category: String) {
        binding.rlCategory.tvInfoField.text = "Category"
        binding.rlCategory.etField.setText(category)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}