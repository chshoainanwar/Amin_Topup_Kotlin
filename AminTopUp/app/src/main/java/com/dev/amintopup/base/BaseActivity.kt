package com.dev.amintopup.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dev.amintopup.R
import com.dev.amintopup.base.tooltip.SimpleTooltip
import com.google.gson.Gson


@Suppress("unused")

abstract class BaseActivity : AppCompatActivity() {

    private var mViewGroup: ViewGroup? = null
    var dialogueLoader: LoaderFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogueLoader = LoaderFragment()
    }

    open fun openNotification() {

    }

    open fun openSideMenu() {

    }

    fun showLoader(text: String = "") {
        try {
            if (dialogueLoader?.isAdded == false) {
                dialogueLoader?.loadText = text
                dialogueLoader?.show(supportFragmentManager, LoaderFragment::class.java.toString())
            }
        } catch (e: IllegalStateException) {

        }


    }

    fun hideLoader() {
        try {
            dialogueLoader?.dismiss()
        } catch (e: IllegalStateException) {

        }

    }

    fun onSetupViewGroup(item: ViewGroup) {
        mViewGroup = item
        HideUtil.init(this, mViewGroup)
    }

    //val toolTip = showToolTip(menuView, Gravity.BOTTOM, R.layout.dashboard_menu_view, "Teom")
    fun showToolTip(
        v: View,
        position: Int,
        layout: Int,
        text: String,
        showArrow: Boolean = false,
        padding: Int = com.intuit.sdp.R.dimen._minus1sdp
    ): SimpleTooltip {
        val tooltip = SimpleTooltip.Builder(v.context)
            .anchorView(v)
            .focusable(true)
            .showArrow(showArrow)
            .backgroundColor(getColor(R.color.black_alpha))
            .text(text)
            .dismissOnOutsideTouch(true)
            .dismissOnInsideTouch(true)
            .modal(true)
            .gravity(position)
            .animated(false)
//            .contentView(layout, android.R.id.text1)
            .contentView(layout)
            .padding(padding)
            .focusable(true)
            .overlayMatchParent(true)
            .build()
        tooltip.show()
        return tooltip
    }

    fun <T> generateList(response: String, type: Class<Array<T>>): ArrayList<T> {

        if (response.isEmpty() || response == "null" || response == "\"[]\"") {
            return arrayListOf()
        }
//        arrayList.addAll(CollectionUtils.listOf(*Gson().fromJson(response, type)))
        val arrayList = ArrayList<T>()
        arrayList.addAll(Gson().fromJson(response, type).toList())
        return arrayList
    }
}