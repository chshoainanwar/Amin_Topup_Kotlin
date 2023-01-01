package com.dev.amintopup.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.amintopup.R
import com.dev.amintopup.databinding.DialogErrorBinding
import com.dev.amintopup.databinding.DialogueSuccessPasswordBinding

class TopupErrorDialog : BaseDialogueFragment() {
    private lateinit var binding: DialogErrorBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogErrorBinding.inflate(inflater)

        return binding.root
    }

    var onCallBackForRedirection: (() -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.text = "Unfortunately, Your Topup transaction \n" +
                "was not successful due to"
        binding.tv2.text = "[Error Description]."
        binding.tv3.text = "You can try again later!"
        binding.btnTryAgain.setOnClickListener {
            dismiss()
            onCallBackForRedirection?.invoke()
        }
        isCancelable = true
//        Handler(Looper.getMainLooper()).postDelayed({
//            dismiss()

//        }, 3000)
        onCallBackForRedirection
    }
}