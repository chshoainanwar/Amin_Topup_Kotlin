package com.dev.amintopup.fragments.account

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragmentMyAccountBinding
import com.dev.amintopup.fragments.home.BtmSelectedPosition
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.*
import com.github.drjacky.imagepicker.ImagePicker
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File

class MyAccountFragment : BaseFragment() {


    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                showLoader("")
                val uri = it.data?.data!!
                uriToFileItem(
                    uri,
                ) { file: File ->
                    NetworkClass.callFileUpload(
                        URLApi().linkGenerate(file),
                        file,
                        object :
                            ResponseNetwork {
                            override fun onSuccessResponse(response: String?, message: String) {
                                val imageURI = response.toString()
                                callUploadPicApi(imageURI)
                            }

                            override fun onErrorResponse(error: String?, response: String?) {
                                hideLoader()
                                showToast("First : $error")
                            }

                        })

                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigationForEdit()
        mActivityDashboard?.hideBtmBar(false)
        mActivityDashboard?.setupBtmViews(BtmSelectedPosition.ACCOUNT, true)
        getProfileData()
        binding.tvAddPhoto.setOnClickListener {
            ImagePicker.with(mActivityItem)
                .crop()
                .cropOval()
                .setMultipleAllowed(false)
                .bothCameraGallery()
                .setOutputFormat(Bitmap.CompressFormat.PNG)
                .createIntentFromDialog { launcher.launch(it) }
        }

    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    private fun updateView() {
        binding.tvName.text = LocalPreference.shared.user?.name ?: ""//getName
        binding.tvBirthday.text = LocalPreference.shared.user?.dateofbirth ?: ""
        binding.tvPhoneNumber.text = LocalPreference.shared.user?.phoneNumber ?: ""
        binding.tvEmail.text = LocalPreference.shared.user?.email ?: ""
        binding.tvCountry.text = LocalPreference.shared.user?.country ?: ""
        binding.ivProfile.loadImage(
            LocalPreference.shared.user?.profile ?: "",
            Placeholders.USER
        )
    }

    private fun getProfileData() {
        showLoader("")
        NetworkClass.callApi(URLApi().getProfile(), object : ResponseNetwork {
            @SuppressLint("CheckResult")
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                val json = JSONObject(response ?: "data")
                val user = Gson().fromJson(json.toString(), UserItem::class.java)
                LocalPreference.shared.user = user
                updateView()

            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?: "")
            }

        })
    }

    private fun callUploadPicApi(image: String) {
        val userID = LocalPreference.shared.user?.id?.toString() ?: ""
        NetworkClass.callApi(
            URLApi().updateProfile(userID, "", "", "", "", image, ""),
            object :
                ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    hideLoader()
                    binding.ivProfile.loadImage(image, Placeholders.USER)
                    val json = JSONObject(response ?: "")
                    val userString = json.optJSONObject("data")
                    val user =
                        Gson().fromJson(userString?.toString() ?: "", UserItem::class.java)
                    LocalPreference.shared.user = user
                    updateView()
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    hideLoader()
//                    showToast(error ?: "")
                    showToast("Second : $error")
                }

            })
    }

    private fun setNavigationForEdit() {
        binding.ivNameEdit.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToEditNameFragment()
            findNavController().navigate(action)
        }
        binding.ivAddBirthday.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToEditBirthdayFragment()
            findNavController().navigate(action)
        }
        binding.ivEmailEdit.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToEditEmailFragment()
            findNavController().navigate(action)
        }
        binding.ivCountryEdit.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToEditCountryFragment()
            findNavController().navigate(action)
        }
        binding.ivPhoneNumber.setOnClickListener {
//            val action = BottomGraphDirections.actionHFToDetailFragment()//actionHomeFragmentToYourDetailsFragment()
//
//            findNavController().navigate(action)
        }
    }

}