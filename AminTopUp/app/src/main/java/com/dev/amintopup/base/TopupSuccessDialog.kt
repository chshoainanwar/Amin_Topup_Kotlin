package com.dev.amintopup.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.amintopup.databinding.DialogueSuccessPasswordBinding

class TopupSuccessDialog: BaseDialogueFragment() {

    private lateinit var binding: DialogueSuccessPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogueSuccessPasswordBinding.inflate(inflater)

        return binding.root
    }


    var onCallBackForRedirection: (() -> Unit)? = null
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.visibility = View.GONE
        binding.tv2.text = "Topup successfully sent to Ali. \n" +
                "Thank you for using Amin Topup!"
        isCancelable = true
        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
            onCallBackForRedirection?.invoke()
            onCallBackForRedirection?.invoke()
        }, 3000)
//        onCallBackForRedirection
    }
}