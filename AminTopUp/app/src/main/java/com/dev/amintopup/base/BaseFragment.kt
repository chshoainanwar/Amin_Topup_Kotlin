package com.dev.amintopup.base


import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.dev.amintopup.fragments.home.HomeActivity
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.Socket


open class BaseFragment : Fragment() {
    var mSocket: Socket? = null
    val mActivityItem: BaseActivity
        get() {
            return activity as? BaseActivity ?: requireActivity() as BaseActivity
        }
    val mActivityDashboard: HomeActivity?
        get() {
            return mActivityItem as? HomeActivity
        }


    fun uriToFileItem(
        selectedImageUri: Uri,
        callBack: ((File) -> Unit)
    ) {
        scopeIO.launch(Dispatchers.IO) {
            val parcelFileDescriptor =
                mActivityItem.contentResolver.openFileDescriptor(selectedImageUri, "r", null)
                    ?: return@launch
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File.createTempFile("image", ".png", mActivityItem.externalCacheDir)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val compressedImageFile = Compressor.compress(mActivityItem, file)
            scopeMain.launch(Dispatchers.Main) {
                callBack.invoke(compressedImageFile)
            }
        }


    }


    val scopeIO: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    val scopeMain: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view is ViewGroup) {
            HideUtil.init(mActivityItem, view)
        }
    }


    fun showLoader(text: String = "") {
        mActivityItem.runOnUiThread {
            mActivityItem.showLoader(text)
        }
    }

    fun showToast(msg: Any?) {
        mActivityItem.showToast(msg.toString())
    }

    fun getPhoneDeviceName(): String {
        val model = Build.BRAND // returns brand name
        return model;
    }

    fun hideLoader() {
        mActivityItem.runOnUiThread {
            mActivityItem.hideLoader()
        }
    }

    fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }

}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })


}