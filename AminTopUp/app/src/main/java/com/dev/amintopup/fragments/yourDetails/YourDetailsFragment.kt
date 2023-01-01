package com.dev.amintopup.fragments.yourDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.isValidEmail
import com.dev.amintopup.base.viewInVisible
import com.dev.amintopup.base.viewVisible
import com.dev.amintopup.databinding.FragmentYourDetailsBinding
import com.dev.amintopup.fragments.home.HomeActivityDirections
import com.dev.amintopup.fragments.home.homeModel.HomeModel
import com.dev.amintopup.network.Placeholders
import com.dev.amintopup.network.loadImage

class YourDetailsFragment : BaseFragment() {
    private var _binding: FragmentYourDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentYourDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()
        setNameField()
        setEmailField()

        val getNetworkData: HomeModel =
            arguments?.getSerializable("operator") as? HomeModel ?: HomeModel()
        val getNumber = arguments?.getString("number", "") ?: ""
        binding.tvNumber.text = getNumber
        binding.tvOperatorName.text = getNetworkData.operator_name
        binding.ivNetwork.loadImage(getNetworkData.operator_image, Placeholders.USER)
        binding.btnContinue.setBackgroundResource(R.drawable.btn_bg_enabled)
        binding.btnContinue.isEnabled = true
        binding.btnContinue.setTextColor(mActivityItem.getColor(R.color.white))


        mActivityDashboard?.hideBtmBar(true)
        binding.btnContinue.setOnClickListener {
            val name = binding.rlName.etField.text.toString()
            val email = binding.rlEmail.etField.text.toString()
            val action = BottomGraphDirections.actionToHomeAmountFragment(getNetworkData)
            action.number = getNumber
            action.name = name
            action.email = email
            findNavController().navigate(action)
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            val action = YourDetailsFragmentDirections.actionToAboutTermPolicy()
            action.title = "Privacy Policy"
            action.link =
                "https://policymaker.io/privacy-policy/?gclid=CjwKCAjwu5yYBhAjEiwAKXk_eCgrKBoCH_MGFoEZWaE4_RWXuqe23RIPFijurzAXzmu45_OFgeIVAxoCyXIQAvD_BwE"
            findNavController().navigate(action)
        }
        binding.tvTerms.setOnClickListener {
            val action = HomeActivityDirections.actionToAboutTermPolicy()
            action.title = "Terms & Condition"
            action.link =
                "https://termly.io/resources/templates/terms-and-conditions-template/"
            findNavController().navigate(action)
        }
        binding.tvCondition.setOnClickListener {
            val action = HomeActivityDirections.actionToAboutTermPolicy()
            action.title = "Terms & Condition"
            action.link =
                "https://termly.io/resources/templates/terms-and-conditions-template/"
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setNameField() {
        binding.rlName.tvInfoField.text = "Receiver Name"
        binding.rlName.etField.hint = "Amin Top-up"
        binding.rlName.etField.inputType = InputType.TYPE_CLASS_TEXT
        binding.rlName.ivFields.setImageResource(R.drawable.ic_baseline_person_outline_24)
        binding.rlName.etField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setEmailField() {
        binding.rlEmail.tvInfoField.text = "Receiver Email"
        binding.rlEmail.etField.hint = "amintopup@gmail.com"
        binding.rlEmail.etField.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.rlEmail.etField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim()
                if (txt.isValidEmail()) {
                    binding.rlEmail.tvError.viewInVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlEmail.tvError.viewVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_error_field)
                }

            }
        })
    }

    fun checkValidation() {
        if (binding.rlName.etField.text.toString().trim()
                .isNotEmpty()
        ) {
            binding.btnContinue.setBackgroundResource(R.drawable.btn_bg_enabled)
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setTextColor(mActivityItem.getColor(R.color.white))
        } else {
            binding.btnContinue.isEnabled = false
            binding.btnContinue.setBackgroundResource(R.drawable.bg_btn)
            binding.btnContinue.setTextColor(mActivityItem.getColor(R.color.darkStrokeAndTextClr))
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