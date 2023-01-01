package com.dev.amintopup.fragments.orderSummary

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.R
import com.dev.amintopup.base.AfterTaxDialog
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.TopupErrorDialog
import com.dev.amintopup.base.TopupSuccessDialog
import com.dev.amintopup.databinding.FragmentOrderSummaryBinding
import com.dev.amintopup.fragments.adapters.CountryPickerAdapter
import com.dev.amintopup.fragments.home.homeModel.Detail
import com.dev.amintopup.fragments.home.homeModel.HomeModel
import com.dev.amintopup.fragments.orderSummary.model.OrderSummaryModel
import com.dev.amintopup.network.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.json.JSONObject

class OrderSummaryFragment : BaseFragment() {
    var country: ArrayList<Country> = ArrayList()
    private var _binding: FragmentOrderSummaryBinding? = null
    private val binding get() = _binding!!
    private var getNumber: String = ""
    private var getNumberFromEdit: String = ""
    private var getPayment: String = ""
    private var getEmail: String = ""
    private var getName: String = ""
    private lateinit var dialog: BottomSheetDialog
    var getDetails: Detail = Detail()
    var getNetworkData: HomeModel = HomeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme)

        country.addAll(World.getAllCountries())
        setTopBar()


        getNetworkData = arguments?.getSerializable("operator") as HomeModel
        getDetails = arguments?.getSerializable("list") as Detail
        getNumber = arguments?.getString("number","") ?: ""
        getPayment = arguments?.getString("amount","") ?: ""
        getEmail = arguments?.getString("email","") ?: ""
        getName = arguments?.getString("name","") ?: ""
        if (getName == ""){
            getName = getNumber
        }
        getNumberFromEdit = arguments?.getString("EditPhone", "") ?: ""

        binding.tvNumber.text = getNumber
        binding.tvTopUpAmount.text = getPayment
        binding.tvOperatorName.text = getNetworkData.operator_name
        binding.tvAmountAfterTax.text = getDetails.receiver_get_AFN.toString() + " AFN"
        binding.tvTopUpAmountInUSD.text = getDetails.topup_usd.toString()
        binding.tvProcessingFee.text = getDetails.processing_fee.toString()
        binding.tvTotalAmount.text = getDetails.totalAmount.toString()
        binding.tvTotalPayable.text = getDetails.totalAmount.toString()

        binding.ivNetwork.loadImage(getNetworkData.operator_image, Placeholders.USER)

        binding.ivNumberEdit.setOnClickListener {
            val action =
                OrderSummaryFragmentDirections.actionOrderSummaryFragmentToEditPhoneFragment()
            val bundle = Bundle()
            bundle.putString("From","OrderSummary")
            bundle.putSerializable("From",getDetails)
            bundle.putSerializable("operator", getNetworkData)
            bundle.putSerializable("list", getDetails)
            bundle.putString("number", getNumber)
            bundle.putString("amount", getPayment)
            bundle.putString("email", getEmail)
            bundle.putString("name", getName)
            findNavController().navigate(R.id.action_orderSummaryFragment_to_editPhoneFragment,bundle)
        }
        binding.ivAmountEdit.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnPay.setOnClickListener {
            callCreateTransactionApi()
        }
        binding.tvOpenTex.setOnClickListener {
            val frag = AfterTaxDialog()
            frag.show(childFragmentManager, frag.toString())
        }
    }

    private fun setTopBar() {
        binding.topBar.tvTitle.visibility = View.INVISIBLE
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        if(mActivityDashboard?.isWebViewSuccess == true){
            mActivityDashboard?.isWebViewSuccess = false
            // Call Api For Success
            Log.d("Link", "Link SUCCESS")
        }
    }
    private fun callCreateTransactionApi() {
        showLoader("")
        val splitAmount =getPayment.split(" ")
        val selectedAmount = splitAmount[0]
        NetworkClass.callApi(
            URLApi().createTransaction(
                getName,
                getEmail,
                getNumber,
                "Afghanistan",
                getNetworkData.operator_name.toString(),
                selectedAmount,
                getDetails.topup_usd.toString(),
                getDetails.processing_fee.toString(),
                getDetails.totalAmount.toString()
            ), object : ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    callPaymentURLApi()
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    hideLoader()
                    showToast("First $error")
                }

            })
    }

    private fun callPaymentURLApi(){
        showLoader("")
        NetworkClass.callApi(URLApi().paymentURL(getDetails.product_code_stripe.toString()), object : ResponseNetwork{
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                val json = JSONObject(response ?: "")
//                val userString = json.optJSONObject("data") ?:""
                val user =
                    Gson().fromJson(json.toString(), OrderSummaryModel::class.java)
                val getPaymentURL = user.payment_url
                showToast(getPaymentURL)
                val action =
                    OrderSummaryFragmentDirections.actionOrderSummaryFragmentToWebViewFragment()
                action.url = getPaymentURL
                findNavController().navigate(action)
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast("Second $error")
            }

        })
    }

    fun showSuccessfulDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_succcessfull)
        val window = dialog.window
        window!!.setLayout(
            AbsListView.LayoutParams.MATCH_PARENT,
            AbsListView.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.5f)
        window.attributes = wlp
        dialog.show()
    }

    fun cardBottomSheetDialog() {
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_card)
        val window: Window? = dialog.window
        window?.setLayout(
            AbsListView.LayoutParams.MATCH_PARENT,
            AbsListView.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val etCardNumber = dialog.findViewById<View>(R.id.etCardNumber) as EditText
        val etCardDate = dialog.findViewById<View>(R.id.etCardDate) as EditText
        val etCardCVV = dialog.findViewById<View>(R.id.etCardCVV) as EditText
        val etCountry = dialog.findViewById<View>(R.id.etCountry) as EditText
        val btnContinue = dialog.findViewById<View>(R.id.btnContinue) as AppCompatButton

        etCountry.setOnClickListener {
            val toolTip = mActivityItem.showToolTip(it, Gravity.TOP, R.layout.popup, "Teom")
            val rv = toolTip.findViewById<RecyclerView>(R.id.rlCountryList)
            rv.layoutManager = LinearLayoutManager(mActivityItem, RecyclerView.VERTICAL, false)
            rv.adapter = CountryPickerAdapter(country, mActivityItem) { selectedCountry ->
                etCountry.setText(selectedCountry.name)
                toolTip.dismiss()
            }
        }

        //Set Card Format
        etCardNumber.addTextChangedListener(object : TextWatcher {
            private val space = ' '
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // Remove all spacing char
                var pos = 0
                while (true) {
                    if (pos >= s.length) break
                    if (space == s[pos] && ((pos + 1) % 5 != 0 || pos + 1 == s.length)) {
                        s.delete(pos, pos + 1)
                    } else {
                        pos++
                    }
                }

                // Insert char where needed.
                pos = 4
                while (true) {
                    if (pos >= s.length) break
                    val c = s[pos]
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space)
                    }
                    pos += 5
                }
            }
        })

        //Set Expiry Date Format
        etCardDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 0 && s.length % 3 == 0) {
                    val c = s[s.length - 1]
                    if ('/' == c) {
                        s.delete(s.length - 1, s.length)
                    }
                }
                if (s.length > 0 && s.length % 3 == 0) {
                    val c = s[s.length - 1]
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), "/").size <= 2) {
                        s.insert(s.length - 1, "/")
                    }
                }
            }
        })
        btnContinue.setOnClickListener {
            val strCountry = etCountry.text.toString()
            val getCardNumber = etCardNumber.text.toString()
            val getCardDate = etCardDate.text.toString()
            val strCardCVV = etCardCVV.text.toString()
            if (getCardNumber.length < 19) {
                Toast.makeText(
                    activity,
                    "Please write card number in correct format",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (getCardDate.length < 5) {
                Toast.makeText(
                    activity,
                    "Please write card date in correct format",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (strCardCVV.length < 3) {
                Toast.makeText(activity, "Please write cvc in correct format", Toast.LENGTH_SHORT)
                    .show()
            } else if (strCountry.isEmpty()) {
                Toast.makeText(activity, "Please select country name", Toast.LENGTH_SHORT).show()
            } else {
                //Get Card Number Without Space
//                val cardNumber = getCardNumber.split(" ".toRegex()).toTypedArray()
//                val cardNumber1 = cardNumber[0]
//                val cardNumber2 = cardNumber[1]
//                val cardNumber3 = cardNumber[2]
//                val cardNumber4 = cardNumber[3]
//                val cardNumberWithoutSpace = cardNumber1 + cardNumber2 + cardNumber3 + cardNumber4
                //Get Card Expiry Date Without "/"
//                val cardDate = getCardDate.split("/".toRegex()).toTypedArray()
//                val cardMonth = cardDate[0]
//                val cardYear = "20" + cardDate[1]
                //                    callPaymentAPI(cardNumberWithoutSpace, cardMonth, cardYear, strCardCVV, amount, userID, eventID);
                dialog.dismiss()
                val frag = TopupErrorDialog()
                frag.onCallBackForRedirection = {

                    val frag1 = TopupSuccessDialog()
                    frag1.onCallBackForRedirection = {
                        val action = BottomGraphDirections.homeFragmentBase()
                        findNavController().backQueue.clear()
                        findNavController().clearBackStack(R.id.homeFragmentBase)
                        findNavController().navigate(action)
                    }
//                    frag1.dis
                    frag1.show(childFragmentManager, frag.toString())
                }
                frag.show(childFragmentManager, frag.toString())
            }
        }
        dialog.show()
    }
}