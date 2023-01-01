package com.dev.amintopup.fragments.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.dev.amintopup.R
import com.dev.amintopup.base.*
import com.dev.amintopup.databinding.FragSignupBinding
import com.dev.amintopup.fragments.adapters.CountryPickerAdapter
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import org.json.JSONObject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignUpFragment : BaseFragment() {
    private var checkPhoneValidatio: String = "false"
    var country: ArrayList<Country> = ArrayList()
    private var _binding: FragSignupBinding? = null
    private val txtEmail: AppCompatEditText
        get() {
            return binding.rlEmail.etField
        }
    private val txtPassword: AppCompatEditText
        get() {
            return binding.rlPassword.etField
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragSignupBinding.inflate(inflater, container, false)
        country.addAll(World.getAllCountries())
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEmail()
        setupPassword()
        checkValidation()
        HideUtil.init(mActivityItem)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLogin.setOnClickListener {
            val strEmail: String = binding.rlEmail.etField.text.toString()
            val strPassword: String = binding.rlPassword.etField.text.toString()
            val strCountry: String = binding.rlCountry.etField.text.toString()
            val strPhone: String = binding.rlPhone.etField.text.toString()
            val strCode: String = binding.rlPhone.ccp.defaultCountryCode
            val strFullPhone = strPhone + strCode
            registerApi(strEmail, strPassword, strCountry, strFullPhone)
        }
        val firstCountry = country.first()
        setupField(firstCountry)
        binding.rlCountry.rlField.setOnClickListener {
            val toolTip = mActivityItem.showToolTip(it, Gravity.TOP, R.layout.popup, "Teom")
            val rv = toolTip.findViewById<RecyclerView>(R.id.rlCountryList)
            rv.layoutManager = LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
            rv.adapter = CountryPickerAdapter(country, mActivityItem) { selectedCountry ->
                binding.rlCountry.etField.setText(selectedCountry.name)
                toolTip.dismiss()
            }
        }
        binding.rlPhone.ccp.registerCarrierNumberEditText(binding.rlPhone.etField);
        binding.rlPhone.ccp.setPhoneNumberValidityChangeListener {
            if (it) {
                checkPhoneValidatio = "true"
                binding.rlPhone.tvError.viewInVisible()
                binding.rlPhone.rlField.setBackgroundResource(R.drawable.bg_success_field)
                checkValidation()
                // success
            } else {
                val txt = binding.rlPhone.etField.text.toString()
                if (txt.length > 1) {
                    checkPhoneValidatio = "false"
                    binding.rlPhone.tvError.text = "Enter valid phone number"
                    binding.rlPhone.tvError.viewVisible()
                    binding.rlPhone.rlField.setBackgroundResource(R.drawable.bg_error_field)
                    checkValidation()
                }
                // false
            }
        }
//        .setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
//            @Override
//            public void onValidityChanged(boolean isValidNumber) {
//                // your code
//            }
//        });
//        binding.rlPhone.llOther.setOnClickListener {
//            val toolTip = mActivityItem.showToolTip(it, Gravity.TOP, R.layout.popup, "Teom")
//            val rv = toolTip.findViewById<RecyclerView>(R.id.rlCountryList)
//            rv.layoutManager = LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
//            rv.adapter = CountryPickerAdapter(country, mActivityItem) { selectedCountry ->
//                binding.rlPhone.tvPhoneCode.text = selectedCountry.id.toString()
//                binding.rlPhone.ivFields.setImageResource(selectedCountry.flagResource)
//                toolTip.dismiss()
//            }
//        }
    }

    private fun registerApi(
        email: String,
        password: String,
        phone: String,
        country: String,
    ) {
        mActivityItem.showLoader("")

        NetworkClass.callApi(
            URLApi().register(email, password, phone, country),
            object : ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    mActivityItem.hideLoader()
                    val json = JSONObject(response ?: "")
                    val token = json.optString("token")
                    LocalPreference.shared.token = token
                    findNavController().navigateUp()
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    mActivityItem.hideLoader()
                    mActivityItem.showToast(error ?: "")
                }
            })
    }

    private fun setupField(firstCountry: Country) {
        binding.rlPhone.tvPhoneCode.text = firstCountry.id.toString()
        binding.rlPhone.ivFields.setImageResource(firstCountry.flagResource)
        binding.rlCountry.etField.setText(firstCountry.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkValidation() {
        if (txtEmail.text.toString().trim().isValidEmail() && txtPassword.text.toString().trim()
                .isValidPassword() && binding.rlCountry.etField.text.toString().isNotEmpty() &&
            checkPhoneValidatio.contains("true")
        ) {
            binding.btnLogin.setBackgroundResource(R.drawable.btn_bg_enabled)
            binding.btnLogin.isEnabled = true
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.white))
        } else {
            binding.btnLogin.isEnabled = false
            binding.btnLogin.setBackgroundResource(R.drawable.bg_btn)
            binding.btnLogin.setTextColor(mActivityItem.getColor(R.color.darkStrokeAndTextClr))
        }
    }

    private fun setupEmail() {

        txtEmail.hint = "Enter your e-Mail"
        txtEmail.inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.rlEmail.tvInfoField.text = "E-Mail"
        binding.rlEmail.tvError.text = "Enter valid E-Mail"
        binding.rlEmail.ivFields.setImageResource(R.drawable.ic_mail)
        txtEmail.addTextChangedListener(object : TextWatcher {
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
                checkValidation()
            }
        })

        txtEmail.setOnFocusChangeListener { _, hasFocus ->
            val txt = txtEmail.text?.toString()?.trim() ?: ""
            if (hasFocus) {
                if (txt.isValidEmail()) {
                    binding.rlEmail.tvError.viewInVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlEmail.tvError.viewVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_focus_field)
                }
            } else {
                if (txt.isValidEmail()) {
                    binding.rlEmail.tvError.viewInVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlEmail.tvError.viewVisible()
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlEmail.rlField.setBackgroundResource(R.drawable.bg_simple_field)
                }
            }
        }
    }

    private fun setupPassword() {
        txtPassword.hint = "Enter your password"
//        txtPassword.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        binding.rlPassword.tvInfoField.text = "Password"
        binding.rlPassword.tvError.text = "Password should be at least 8 Characters!"
        binding.rlPassword.ivFields.setImageResource(R.drawable.ic_pass_visible)
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim()
                if (txt.isValidPassword()) {
                    binding.rlPassword.tvError.viewInVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPassword.tvError.viewVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_error_field)
                }
                checkValidation()
            }
        })
        binding.rlPassword.ivFields.setOnClickListener {
            if (txtPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.rlPassword.ivFields.setImageResource(R.drawable.ic_pass_visible)
            } else {
                txtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.rlPassword.ivFields.setImageResource(R.drawable.ic_pass_invisible)
            }
            txtPassword.setSelection(txtPassword.length())
        }
        txtPassword.setOnFocusChangeListener { _, hasFocus ->
            val txt = txtPassword.text.toString().trim()
            if (hasFocus) {
                if (txt.isValidPassword()) {
                    binding.rlPassword.tvError.viewInVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPassword.tvError.viewVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_focus_field)
                }
            } else {
                if (txt.isValidPassword()) {
                    binding.rlPassword.tvError.viewInVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_success_field)
                } else if (txt.length > 1) {
                    binding.rlPassword.tvError.viewVisible()
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_error_field)
                } else {
                    binding.rlPassword.rlField.setBackgroundResource(R.drawable.bg_simple_field)
                }
            }
        }
    }

}


fun checkDate(month: String, day: Int): String {
//    31,28,31,30,31,30,31,31,30,31,30,31
//    January,February,March,April,May,June,July,August,September,October,November,December
    // Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday,Weeknday
    val map = mapOf(
        Pair("January", 31),
        Pair("February", 28),
        Pair("March", 31),
        Pair("April", 30),
        Pair("May", 31),
        Pair("June", 30),
        Pair("July", 31),
        Pair("August", 31),
        Pair("September", 30),
        Pair("October", 31),
        Pair("November", 30),
        Pair("December", 31),
    )
    val hashMap = HashMap<Int, String>()
    hashMap[5] = "Monday"
    hashMap[6] = "Tuesday"
    hashMap[7] = "Wednesday"
    hashMap[8] = "Thursday"
    hashMap[1] = "Friday"
    hashMap[2] = "Saturday"
    hashMap[3] = "Sunday"
    hashMap[4] = "Weeknday"
    var totalDayUntilMothDayGiven = 0
    for (item in map) {
        if (item.key == month) {
            totalDayUntilMothDayGiven += day
            break
        } else {
            totalDayUntilMothDayGiven += item.value
        }
    }
    return if (hashMap.containsKey(totalDayUntilMothDayGiven % 8)) {
        hashMap[totalDayUntilMothDayGiven % 8] ?: ""
    } else {
        ""
    }

}