package com.dev.amintopup.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.amintopup.R
import com.dev.amintopup.databinding.DialogAfterTaxBinding
import com.dev.amintopup.databinding.DialogueSuccessPasswordBinding

class AfterTaxDialog : BaseDialogueFragment() {
    private lateinit var binding: DialogAfterTaxBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAfterTaxBinding.inflate(inflater)

        return binding.root
    }

    var onCallBackForRedirection: (() -> Unit)? = null
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.visibility = View.VISIBLE
        binding.tv2.text = "ATRA (Afghanistan Telecom Regulatory Authority) impose 10% tax charged on all telecommunication users while adding Topup. While Amin Topup sending the full Topup Amount to Telecom partner for recharge, after the Topup Amount reach to customer wallet, The operator deduct 10% Tax from the total Topup Amount. The 10% Tax is not withhold or under control of AminTopup."
        binding.btnTryAgain.setOnClickListener {
            dismiss()
            onCallBackForRedirection?.invoke()
        }
        isCancelable = true
        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()

        }, 3000)
        onCallBackForRedirection
    }
}