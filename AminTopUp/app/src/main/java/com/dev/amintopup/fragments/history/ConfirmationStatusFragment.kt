package com.dev.amintopup.fragments.history

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.BuildConfig
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.getSpecialToCustom

import com.dev.amintopup.base.viewGone
import com.dev.amintopup.base.viewVisible
import com.dev.amintopup.databinding.FragmentConfirmationStatusBinding
import com.dev.amintopup.fragments.history.topupDeatilsModel.TopupDetailModel
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject
import java.io.*
import java.net.URLConnection


class ConfirmationStatusFragment : BaseFragment() {
    private var _binding: FragmentConfirmationStatusBinding? = null
    private val binding get() = _binding!!
    val transactionID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConfirmationStatusBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()
        mActivityDashboard?.hideBtmBar(true)
        val getId = arguments?.getString("transaction_id", "") ?: ""
        callTopUpDetailApi(getId)

    }

    private fun callTopUpDetailApi(getId: String) {
        showToast("")
        NetworkClass.callApi(URLApi().topUpDeatils(getId), object : ResponseNetwork {
            @SuppressLint("SetTextI18n")
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                val json = JSONObject(response ?: "data")
                val data =
                    Gson().fromJson(json.toString(), TopupDetailModel::class.java)
                binding.tvReceiverName.text = data.receiver_name
                binding.tvPhoneNumber.text = data.receiver_number
                binding.tvReceiverEmail.text = data.receiver_email
                binding.tvNetwork.text = data.receiver_network
                binding.tvTopUpAmount.text = data.topup_amount.toString() + " ANF"
                val date = data.created_at?.getSpecialToCustom(output = "MM / dd / yyyy")
                val time = data.created_at?.getSpecialToCustom(output = "HH:mm aa")
                binding.tvRechargeDate.text = date
                binding.tvRechargeTime.text = " $time"
                binding.tvTransactionID.text = data.id.toString()
                binding.tvPaidAmount.text = data.topup_amount.toString() + " ANF"
                val status = data.status
                if (status!! == 0){
                    binding.tvStatus.setTextColor(Color.parseColor("#FF3636"))
                    binding.tvStatus.text =  "Failed"
                }else{
                    binding.tvStatus.setTextColor(Color.parseColor("#3DAB25"))
                    binding.tvStatus.text =  "Success"
                }

            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?: " ")
                binding.main.visibility = GONE
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun setTopBar() {
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.tvTitle.text = "Transaction Detail"
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnDownload.setOnClickListener {
            binding.ivLogo.viewVisible()
            takeScreenForDownload()
            binding.ivLogo.viewGone()
        }
        binding.btnShare.setOnClickListener {
            binding.ivLogo.viewVisible()
            takeScreen()
            binding.ivLogo.viewGone()
        }
    }

    private fun loadBitmapFromView(context: Context, v: View): Bitmap? {
        val dm: DisplayMetrics = context.resources.displayMetrics
        v.measure(
            MeasureSpec.makeMeasureSpec(dm.widthPixels, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(dm.heightPixels, MeasureSpec.EXACTLY)
        )
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val returnedBitmap = Bitmap.createBitmap(
            v.measuredWidth,
            v.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val c = Canvas(returnedBitmap)
        v.draw(c)
        return returnedBitmap
    }

    private fun takeScreen() {
        val bitmap: Bitmap? =
            loadBitmapFromView(mActivityItem, binding.llScreenShot) //get Bitmap from the view
        val imageFile: File =
            File.createTempFile("Receipt", ".jpeg", mActivityItem.externalCacheDir)
//            Environment.getExternalStorageDirectory() + File.separator.toString() + "screen_" + System.currentTimeMillis()
//                .toString() + ".jpeg"
//        val imageFile = File(mPath)
        var fout: OutputStream? = null
        try {
            fout = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, fout)
            fout.flush()
            shareFile(imageFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fout?.close()
        }
    }

    private fun saveImageInQ(bitmap: Bitmap?): Uri? {
        val filename = "AminTopup_IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }
        }

        //use application context to get contentResolver
        val contentResolver = mActivityItem.contentResolver
        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
        fos?.use { bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, it) }
        contentValues.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        }
        imageUri?.let { mActivityItem.contentResolver.update(it, contentValues, null, null) }

        return imageUri
    }

    private fun takeScreenForDownload() {
//        val title = "Amin Topup"
        val bitmap: Bitmap? =
            loadBitmapFromView(mActivityItem, binding.llScreenShot) //get Bitmap from the view
        val uri = saveImageInQ(bitmap)
        uri?.openUri(mActivityItem)

    }

    private fun shareFile(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            mActivityItem,
            BuildConfig.APPLICATION_ID + "." + mActivityItem.localClassName + ".provider",
            file
        )
        val intentShareFile = Intent(Intent.ACTION_SEND)
        intentShareFile.type = URLConnection.guessContentTypeFromName(file.name)
        intentShareFile.putExtra(
            Intent.EXTRA_STREAM,
            uri
        )
        intentShareFile.putExtra(Intent.EXTRA_TITLE, "Receipt");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Amin Topup receipt.");
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        val chooser = Intent.createChooser(intentShareFile, "Share File")
        chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivity(chooser)
    }

}

fun Uri.openUri(context: Context) {
//    val uri = Uri.parse("market://details?id=$packageName")
    val goToMarketIntent = Intent(Intent.ACTION_VIEW, this)

    var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
    goToMarketIntent.addFlags(flags)
    try {
        context.startActivity(goToMarketIntent, null)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            this
        )

        context.startActivity(intent, null)
    }
}