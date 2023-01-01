package com.dev.amintopup.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

open class BaseDialogueFragment : DialogFragment() {
    lateinit var mContext: Context
    lateinit var mActivity: BaseActivity
    lateinit var mView: View


    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.setCancelable(tr)
//        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as BaseActivity
    }

    protected open fun onBecameVisibleToUser() {
        //
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
//        HideUtil.init(mActivity)
    }

    protected open fun onRecycle() {
        //
    }

    final override fun onDestroy() {
        onRecycle()
        super.onDestroy()
    }

    protected open fun onBecameInvisibleToUser() {
        //
    }
//
//    final override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser) {
//            onBecameVisibleToUser()
//        } else {
//            onBecameInvisibleToUser()
//        }
//    }
}