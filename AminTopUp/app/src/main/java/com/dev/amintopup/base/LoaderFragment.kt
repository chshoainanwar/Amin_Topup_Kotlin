package com.dev.amintopup.base

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dev.amintopup.base.animtation.RotateAnimation
import com.dev.amintopup.databinding.ItemLoaderBinding

class LoaderFragment : DialogFragment() {
    var loadText: String = ""
    private lateinit var binding: ItemLoaderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            ItemLoaderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoaderText.text = loadText
        RotateAnimation.create().with(binding.ivAppLogo)
            .setRepeatCount(RotateAnimation.INFINITE)
            .setRepeatMode(RotateAnimation.RESTART)
            .setDuration(1000)
            .start()
//        RotateAnimation.create().with(binding.ivAppLogo)
//            .setRepeatCount(RotateAnimation.INFINITE)
//            .setRepeatMode(RotateAnimation.RESTART)
//            .setDuration(200)
//            .start()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width =
                Resources.getSystem().displayMetrics.widthPixels.toDouble() * (0.95).toDouble()//ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.window?.setLayout(width.toInt(), height)
            dialog.window?.setGravity(Gravity.CENTER)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }


}