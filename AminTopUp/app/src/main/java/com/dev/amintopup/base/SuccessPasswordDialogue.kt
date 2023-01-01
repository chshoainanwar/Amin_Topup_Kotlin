package com.dev.amintopup.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.amintopup.databinding.DialogueSuccessPasswordBinding

class SuccessPasswordDialogue : BaseDialogueFragment() {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
            onCallBackForRedirection?.invoke()
        }, 3000)
//        onCallBackForRedirection
    }
}