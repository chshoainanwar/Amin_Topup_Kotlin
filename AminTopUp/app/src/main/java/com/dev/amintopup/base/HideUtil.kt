/*
 * Copyright 2016 yinglan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.dev.amintopup.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.amintopup.R
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class HideUtil private constructor(activity: AppCompatActivity, content: ViewGroup?) {
    @SuppressLint("ClickableViewAccessibility")
    private fun getScrollView(viewGroup: ViewGroup?, activity: Activity) {
        if (null == viewGroup) {
            return
        }
        val count = viewGroup.childCount
        for (i in 0 until count) {
            val view = viewGroup.getChildAt(i)
            when (view) {
                is ScrollView -> {
                    view.setOnTouchListener { _, motionEvent ->
                        dispatchTouchEvent(activity, motionEvent)
                        false
                    }
                }
                is AbsListView -> {
                    view.setOnTouchListener { _, motionEvent ->
                        dispatchTouchEvent(activity, motionEvent)
                        false
                    }
                }
                is RecyclerView -> {
                    view.setOnTouchListener { _, motionEvent ->
                        dispatchTouchEvent(activity, motionEvent)
                        false
                    }
                }
                is ViewGroup -> {
                    getScrollView(view, activity)
                }
            }
            if (view.isClickable && view is TextView && view !is EditText) {
                view.setOnTouchListener { _, motionEvent ->
                    dispatchTouchEvent(activity, motionEvent)
                    false
                }
            }
        }
    }


    private fun dispatchTouchEvent(mActivity: Activity, ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = mActivity.currentFocus
            if (null != v && isShouldHideInput(v, ev)) {
                hideSoftInput(mActivity, v.windowToken)
            }
        }
        return false
    }


    private fun isShouldHideInput(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val rect = Rect()
            v.getHitRect(rect)
            return !rect.contains(event.x.toInt(), event.y.toInt())
        }
        return true
    }


    private fun hideSoftInput(mActivity: Activity, token: IBinder?) {
        if (token != null) {
            val im =
                (mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            im.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    companion object {

        fun init(activity: AppCompatActivity) {
            HideUtil(activity, null)
        }


        fun init(activity: AppCompatActivity, content: ViewGroup?) {
            HideUtil(activity, content)
        }


        fun hideSoftKeyboard(activity: Activity?) {
            if (null == activity) {
                throw RuntimeException("Error System")
            }
            val view = activity.currentFocus
            if (null != view) {
                val inputMethodManager =
                    (activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }


        fun hideSoftKeyboard(view: View?) {
            if (null != view) {
                val inputMethodManager =
                    view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            } else {
                throw RuntimeException("Error System")
            }
        }


        fun hideDialogSoftKeyboard(dialog: Dialog?) {
            if (null == dialog) {
                throw RuntimeException("Error System")
            }
            val view = dialog.currentFocus
            if (null != view) {
                val inputMethodManager =
                    dialog.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }


    init {
        try {
            var contentCheckItem = content
            if (contentCheckItem == null) {
                contentCheckItem = activity.findViewById(R.id.content) as ViewGroup
            }
            getScrollView(contentCheckItem, activity)
            contentCheckItem.setOnTouchListener { _, motionEvent ->
                dispatchTouchEvent(activity, motionEvent)
                false
            }
        } catch (e: NullPointerException) {
        }

    }
}


fun Context.showToast(msg: Any) {
    if (msg.toString().isNotEmpty())
        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}

fun String?.imageFile(): Boolean {
    Log.d("links are", "links are ${this}")
    // Regex to check valid image file extension.
    val regex = "([^\\s]+(\\.(?i)(png|gif|bmp|svg|jpeg|jpg))$)"//jpe?g|

    // Compile the ReGex
    val p: Pattern = Pattern.compile(regex)

    // If the string is empty
    // return false
    if (this == null) {
        return false
    }

    // Pattern class contains matcher() method
    // to find matching between given string
    // and regular expression.
    val m: Matcher = p.matcher(this)

    // Return if the string
    // matched the ReGex
    val vlaue = m.matches()
    Log.d("links are", "links are ${vlaue}")
    return vlaue
}


fun View.viewVisible() {
    this.visibility = View.VISIBLE
}

fun View.viewGone() {
    this.visibility = View.GONE
}

fun View.viewInVisible() {
    this.visibility = View.INVISIBLE
}

fun View.viewVisibility(): Int {
    return this.visibility
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

fun View.isInVisible(): Boolean {
    return this.visibility == View.INVISIBLE
}

fun <T> generateList(response: String, type: Class<Array<T>>): ArrayList<T> {
    val arrayList = ArrayList<T>()
    if (response.isEmpty() || response == "null" || response == "\"[]\"") {
        return arrayList
    }
    //CollectionUtils.listOf
    arrayList.addAll(Gson().fromJson<Array<T>>(response, type))
    return arrayList
}

fun ImageView.loadImage(
    link: Any?,
    drawable: Placeholders = Placeholders.DEFAULT,
    isForCircle: Boolean = false,
    showBadge: Boolean? = false
) {
    this.tag = link.toString()
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.setColorSchemeColors(this.context.getColor(R.color.black))
    circularProgressDrawable.setTint(this.context.getColor(R.color.black))
    circularProgressDrawable.start()
    try {
        if (isForCircle) {
            Glide.with(this)
                .asBitmap()
                .load(link)
                .circleCrop()
                .placeholder(drawable.intValue)//circularProgressDrawable
                .error(drawable.intValue)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(this)
        } else {
            Glide.with(this)
                .load(link)
                .placeholder(drawable.intValue)//circularProgressDrawable
                .error(drawable.intValue)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(this)
        }
        if (this.parent is ViewGroup) {
            val viewTick = getViewsByTag(this.parent as ViewGroup, "1212")
            viewTick?.forEach {
                if (showBadge == true) {
                    it.viewVisible()
                } else {
                    it.viewGone()
                }

            }
        }
    } catch (e: IllegalArgumentException) {

    }
}

enum class Placeholders(var intValue: Int) {
    USER(R.drawable.ic_baseline_person_24_black),
    DEFAULT(R.drawable.ic_applogo),
    DEFAULT_DUMMY(R.drawable.ic_applogoblue)
}

private fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View>? {
    val views = ArrayList<View>()
    val childCount = root.childCount
    for (i in 0 until childCount) {
        val child = root.getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(getViewsByTag(child, tag)!!)
        }
        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }
    }
    return views
}

fun String.extractUrl() =
    this
        .split(" ")
        .firstOrNull { Patterns.WEB_URL.matcher(it).find() }

val screenSizeWidth
    get() = Resources.getSystem().displayMetrics.widthPixels
val screenSizeHeight
    get() = Resources.getSystem().displayMetrics.heightPixels


fun String.shareText(title: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, this)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, title)
    context.startActivity(shareIntent)
}

fun String?.viewShare(context: Context?) {

    try {
        if (context != null)
            ShareCompat.IntentBuilder(context)
                .setType("text/plain")
                .setChooserTitle("Share Post")
                .setText(this)
                .startChooser();

    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()

    }
}

fun String.isValidEmail(): Boolean {
    val email = this
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun String.isValidPassword(): Boolean {
    val email = this
    return !TextUtils.isEmpty(email) && email.count() > 7
}

//val screenSizeWidth
//    get() = Resources.getSystem().displayMetrics.widthPixels
//val screenSizeHeight
//    get() = Resources.getSystem().displayMetrics.heightPixels

fun String.getSpecialToCustom(
    input: String = "yyyy-MM-dd'T'HH:mm:ss",
    output: String = "dd MMM yyyy"
): String {

    try {
        val utcFormat = SimpleDateFormat(input, Locale.getDefault())
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date?
        date = utcFormat.parse(this.split(".").firstOrNull() ?: "")
        val localTimeFormate = SimpleDateFormat(output, Locale.getDefault())
        localTimeFormate.timeZone = TimeZone.getDefault()
        localTimeFormate.parse(localTimeFormate.format(date ?: Date()))
        return localTimeFormate.format(date ?: Date())
    } catch (ex: ParseException) {
        try {
            val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date: Date?
            date = utcFormat.parse(this.split(".").firstOrNull() ?: "")
            val localTimeFormate = SimpleDateFormat(output, Locale.getDefault())
            localTimeFormate.timeZone = TimeZone.getDefault()
            localTimeFormate.parse(localTimeFormate.format(date ?: Date()))
            return localTimeFormate.format(date ?: Date())
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
    }
    return ""

}