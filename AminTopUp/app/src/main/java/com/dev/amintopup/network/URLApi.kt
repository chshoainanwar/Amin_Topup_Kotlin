@file:Suppress("SpellCheckingInspection", "unused", "FunctionName", "EnumEntryName")

package com.dev.amintopup.network


import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dev.amintopup.R
import org.json.JSONObject
import java.io.File


@Suppress("UNUSED_PARAMETER")
class URLApi {


    companion object {
        val baseIp = "kodextech.net/amin-topup/public/"//45.33.19.125
        val BasePath = "http://$baseIp"
        private val TAG = URLApi::class.java.toString()
        val SOCKET_URL = "http://kodextech.net:1025"//"http://kodextech.net:1026"//45.33.19.125
        public val BaseUrl = BasePath //Base URL here

        fun shareLint(post_id: Int): String {
            return "$BasePath/postId=$post_id"
        }
    }

    private var path: String = ""
    private var params: JSONObject = JSONObject()
    var method: NetworkMethod = NetworkMethod.GET
    fun link(): String {
        return BaseUrl + path
    }

    fun param(): JSONObject {
        return params
    }

    fun getRefPublic(linkCode: String): String {
        val string = "$BasePath?ref=${linkCode}&vendor_invitation="
        val message =
            "Join Cleaques Today Using My Referral Code ${linkCode}. And gain 50 points extra \n${string}"
        return message
    }

    fun login(email: String, password: String): URLApi {
        val api = URLApi()
        api.path = "api/login"
        api.params.put("email", email)
        api.params.put("password", password)
        api.method = NetworkMethod.POST
        return api
    }

    fun register(
        email: String,
        password: String,
        phone_Number: String,
        country: String
    ): URLApi {
        val api = URLApi()
        api.path = "api/register"
        api.params.put("email", email)
        api.params.put("password", password)
        api.params.put("phone_number", phone_Number)
        api.params.put("country", country)
        api.method = NetworkMethod.POST
        return api
    }

    fun logout(): URLApi {
        val api = URLApi()
        api.path = "api/logout"
        api.method = NetworkMethod.POST
        return api
    }

    fun frogotPassword(email: String): URLApi {
        val api = URLApi()
        api.path = "api/send_otp"
        api.params.put("email", email)
        api.method = NetworkMethod.POST
        return api
    }

    fun verifyOTP(email: String, otp: String): URLApi {
        val api = URLApi()
        api.path = "api/verify_otp"
        api.params.put("email", email)
        api.params.put("otp", otp)
        api.method = NetworkMethod.POST
        return api
    }

    fun resetPassword(email: String, password: String): URLApi {
        val api = URLApi()
        api.path = "api/reset_password"
        api.params.put("email", email)
        api.params.put("password", password)
        api.method = NetworkMethod.POST
        return api
    }

    fun contactUS(subject: String, category: String, description: String): URLApi {
        val api = URLApi()
        api.path = "api/contact_us"
        api.params.put("subject", subject)
        api.params.put("category", category)
        api.params.put("description", description)
        api.method = NetworkMethod.POST
        return api
    }

    fun linkGenerate(image: File): URLApi {
        val api = URLApi()
        api.path = "api/image_link"
        api.params.put("image", image)
        api.method = NetworkMethod.POST
        return api
    }

    fun updateProfile(
        id: String,
        email: String,
        phone_number: String,
        name: String,
        date_of_birth: String,
        profile: String,
        country: String
    ): URLApi {
        val api = URLApi()
        api.path = "api/update"
        api.params.put("id", id)
        api.params.put("email", email)
        api.params.put("phone_number", phone_number)
        api.params.put("name", name)
        api.params.put("date_of_birth", date_of_birth)
        api.params.put("profile", profile)
        api.params.put("country", country)
        api.method = NetworkMethod.POST
        return api
    }

    fun getProfile(): URLApi {
        val api = URLApi()
        api.path = "api/user_profile"
        api.method = NetworkMethod.POST
        return api
    }

    fun checkTopUpData(number: String): URLApi {
        val api = URLApi()
        api.path = "api/admin/check_operator"
        api.params.put("number", number)
        api.method = NetworkMethod.POST
        return api
    }

    fun topUpHistory(): URLApi {
        val api = URLApi()
        api.path = "api/topup_history"
        api.method = NetworkMethod.POST
        return api
    }

    fun allTopUpList(): URLApi {
        val api = URLApi()
        api.path = "api/all_topups"
        api.method = NetworkMethod.POST
        return api
    }

    fun topUpDeatils(id: String): URLApi {
        val api = URLApi()
        api.path = "api/transaction_detail"
        api.params.put("transaction_id", id)
        api.method = NetworkMethod.POST
        return api
    }

    fun notifications(): URLApi {
        val api = URLApi()
        api.path = "api/notification_list"
        api.method = NetworkMethod.POST
        return api
    }

    fun createTransaction(
        receiver_name: String,
        receiver_email: String,
        receiver_number: String,
        country: String,
        receiver_network: String,
        topup_amount: String,
        topup_amount_usd: String,
        processing_fee: String,
        total_amount_usd: String
    ): URLApi {
        val api = URLApi()
        api.path = "api/create_transaction"
        api.params.put("receiver_name", receiver_name)
        api.params.put("receiver_email", receiver_email)
        api.params.put("receiver_number", receiver_number)
        api.params.put("country", country)
        api.params.put("receiver_network", receiver_network)
        api.params.put("topup_amount", topup_amount)
        api.params.put("topup_amount_usd", topup_amount_usd)
        api.params.put("processing_fee", processing_fee)
        api.params.put("total_amount_usd", total_amount_usd)
        api.method = NetworkMethod.POST
        return api
    }

    fun paymentURL(price_id: String): URLApi {
        val api = URLApi()
        api.path = "api/payment_url"
        api.params.put("price_id", price_id)
        api.method = NetworkMethod.POST
        return api
    }


}

object Randomized {
    fun generate(min: Int, max: Int): Int {
        return min + (Math.random() * (max - min + 1)).toInt()
    }
}


//file picker and upload demo code
//InsGallery.MAX_FILE_SELECT_COUNT = 1
//
//InsGallery.openGalleryImage(
//mActivityItem,
//GlideEngine.createGlideEngine(),
//GlideCacheEngine.createCacheEngine(),
//object : OnResultCallbackListener<LocalMedia> {
//    override fun onResult(result: MutableList<LocalMedia>?) {
//        for (media in (result ?: ArrayList())) {
//            var path = ""
//            path = if (media.isCut && !media.isCompressed) {
//                media.cutPath
//            } else if (media.isCompressed || media.isCut && media.isCompressed) {
//                media.compressPath
//            } else {
//                media.path
//            }
//            val mimeType = media.mimeType
//            val mediaType = PictureMimeType.getMimeType(mimeType)
//            if (mediaType == PictureConfig.TYPE_VIDEO) {
//                if (!path.contains("mp4")) {
//                    path = media.realPath
//                }
//            }
//            var imageURI =
//                if (PictureMimeType.isContent(path) &&
//                    !media.isCut && !media.isCompressed
//                ) Uri.parse(
//                    path
//                ) else Uri.parse(path)
////                            Log.d("Path os", "Path os ${path} , ${media.toString()}")
//            AWSManager.setOnFileUploadListener(
//                path,
//                UploadPath.imageList,
//                mActivityItem,
//                object : AWSManager.UploadListener {
//                    override fun onCredentialsError(fileLink: String) {
//                        Log.d("Path os", "onCredentialsError Path os ${fileLink}")
//                    }
//
//                    override fun onUploadProgressChanged(
//                        id: Int,
//                        bytesCurrent: Long,
//                        bytesTotal: Long,
//                        fileLink: String
//                    ) {
//                        Log.d("Path os", "onUploadProgressChanged Path os ${fileLink}")
//                    }
//
//                    override fun onUploadStateChanged(
//                        id: Int,
//                        state: TransferState?,
//                        fileLink: String
//                    ) {
//                        Log.d("Path os", "onUploadStateChanged Path os ${fileLink}")
//                    }
//
//                    override fun onUploadComplete(url: String, fileLink: String) {
//                        Log.d("Path os", "onUploadComplete Path os ${url}")
//                    }
//
//                    override fun onUploadError(
//                        id: Int,
//                        ex: Exception?,
//                        fileLink: String
//                    ) {
//                        Log.d("Path os", "onUploadError Path os ${ex?.localizedMessage ?: ""}")
//                    }
//
//                })
//            if (path.contains("mp4") || path.contains("mov")) {
//                //Video Case
//
//            } else {
//                // Image Case
//
//            }
//        }
//
//    }
//
//    override fun onCancel() {
//
//    }
//
//}
//)

fun ImageView.loadImage(
    link: Any?,
    drawable: Placeholders = Placeholders.DEFAULT,
    isForCircle: Boolean = false
) {
    this.tag = link?.toString() ?: ""

    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.setColorSchemeColors(this.context.getColor(R.color.darkStrokeAndTextClr))
    circularProgressDrawable.setTint(this.context.getColor(R.color.darkStrokeAndTextClr))
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
//        if (this.parent is ViewGroup) {
//            val viewTick = getViewsByTag(this.parent as ViewGroup, "1212")
//            viewTick?.forEach {
//                if (showBadge == true) {
//                    it.viewVisible()
//                } else {
//                    it.viewGone()
//                }
//            }
//        }
    } catch (e: IllegalArgumentException) {

    }
}

enum class Placeholders(var intValue: Int) {
    USER(R.drawable.iv_men),
    DEFAULT(R.drawable.ic_appbg),
    DEFAULT_DUMMY(R.drawable.ic_applogo)
}