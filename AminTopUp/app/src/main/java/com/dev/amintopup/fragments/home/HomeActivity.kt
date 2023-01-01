package com.dev.amintopup.fragments.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.BottomGraphDirections
import com.dev.amintopup.BuildConfig
import com.dev.amintopup.MainActivity
import com.dev.amintopup.R
import com.dev.amintopup.base.*
import com.dev.amintopup.databinding.ActivityHomeBinding
import com.dev.amintopup.fragments.menu.MenuAdapter
import com.dev.amintopup.fragments.menu.MenuItemsEnum
import com.dev.amintopup.models.UserItem
import com.dev.amintopup.network.LocalPreference
import com.dev.amintopup.network.NetworkClass
import com.dev.amintopup.network.ResponseNetwork
import com.dev.amintopup.network.URLApi
import com.google.gson.Gson
import org.json.JSONObject

class HomeActivity : BaseActivity() {
    var isWebViewSuccess = false
    val mActivityItem: BaseActivity
        get() {
            return this as? BaseActivity ?: this as BaseActivity
        }
    private lateinit var binding: ActivityHomeBinding
    private val navController: NavController
        get() {
            return findNavController(R.id.nav_host_vendor)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSideMenu()
        setupBtmViews()
        binding.topBar.bnve.llTab1.setOnClickListener {
            setupBtmViews(BtmSelectedPosition.HISTORY)
        }
        binding.topBar.bnve.llTab2.setOnClickListener {
            setupBtmViews(BtmSelectedPosition.HOME)
        }
        binding.topBar.bnve.llTab3.setOnClickListener {
            setupBtmViews(BtmSelectedPosition.ACCOUNT)
        }
        binding.topBar.bnve.iv2.setOnClickListener {
            binding.topBar.bnve.llTab2.performClick()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserInfo() {
        // setup user info here
        val versionCode: Int = BuildConfig.VERSION_CODE
        val versionName: String = BuildConfig.VERSION_NAME
        binding.sideMenuNav.tvApp.text = "App Version $versionCode ($versionName)"
        binding.sideMenuNav.ivUser.loadImage(LocalPreference.shared.user?.profile,Placeholders.USER)
        binding.sideMenuNav.tvName.text = LocalPreference.shared.user?.name ?: ""
        binding.sideMenuNav.tvPhone.text = LocalPreference.shared.user?.phoneNumber ?: ""
    }

    override fun openNotification() {
        super.openNotification()
        val action = BottomGraphDirections.actionToNotification()
        navController.navigate(action)
    }

    override fun openSideMenu() {
        super.openSideMenu()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START, true)
        }
    }


    override fun onResume() {
        super.onResume()
        setupUserInfo()
    }

    fun setupBtmViews(
        selectedBtm: BtmSelectedPosition = BtmSelectedPosition.HOME,
        isOnlyViewUpdate: Boolean = false
    ) {
        if (this::binding.isInitialized)
            when (selectedBtm) {
                BtmSelectedPosition.HISTORY -> {
                    if (!isOnlyViewUpdate) {
                        val action = BottomGraphDirections.actionToHistory()
                        navController.navigate(action)
                    }
                    binding.topBar.bnve.iv1.setImageResource(R.drawable.ic_history_selected)
                    binding.topBar.bnve.iv2.setImageResource(R.drawable.ic_btm_2_unselected)
                    binding.topBar.bnve.iv3.setImageResource(R.drawable.ic_account_unselected)

                    binding.topBar.bnve.tv1.setTextColor(getColor(R.color.darkStrokeAndTextClr))
                    binding.topBar.bnve.tv2.setTextColor(getColor(R.color.btmUnselected))
                    binding.topBar.bnve.tv3.setTextColor(getColor(R.color.btmUnselected))
                }
                BtmSelectedPosition.HOME -> {
                    val action = BottomGraphDirections.actionToHome()
                    if (!isOnlyViewUpdate)
                        navController.navigate(action)
                    binding.topBar.bnve.iv1.setImageResource(R.drawable.ic_history_unselected)
                    binding.topBar.bnve.iv2.setImageResource(R.drawable.ic_btm_2_selected)
                    binding.topBar.bnve.iv3.setImageResource(R.drawable.ic_account_unselected)

                    binding.topBar.bnve.tv1.setTextColor(getColor(R.color.btmUnselected))
                    binding.topBar.bnve.tv2.setTextColor(getColor(R.color.darkStrokeAndTextClr))
                    binding.topBar.bnve.tv3.setTextColor(getColor(R.color.btmUnselected))
                }
                BtmSelectedPosition.ACCOUNT -> {
                    val action = BottomGraphDirections.actionToAccount()
                    if (!isOnlyViewUpdate)
                        navController.navigate(action)
                    binding.topBar.bnve.iv1.setImageResource(R.drawable.ic_history_unselected)
                    binding.topBar.bnve.iv2.setImageResource(R.drawable.ic_btm_2_unselected)
                    binding.topBar.bnve.iv3.setImageResource(R.drawable.ic_account_selected)

                    binding.topBar.bnve.tv1.setTextColor(getColor(R.color.btmUnselected))
                    binding.topBar.bnve.tv2.setTextColor(getColor(R.color.btmUnselected))
                    binding.topBar.bnve.tv3.setTextColor(getColor(R.color.darkStrokeAndTextClr))
                }
            }
    }

    private fun setupSideMenu() {
        binding.sideMenuNav.llLogout.root.setOnClickListener {
            // call logout here
            openSideMenu()
        }

        binding.sideMenuNav.rvMenu.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.sideMenuNav.rvMenu.adapter = MenuAdapter(mContext = this) { selectedItem ->
            openSideMenu()
            when (selectedItem) {
                MenuItemsEnum.MY_ACCOUNT -> {
                    setupBtmViews(BtmSelectedPosition.ACCOUNT)
                }
                MenuItemsEnum.P_HISTORY -> {
                    setupBtmViews(BtmSelectedPosition.HISTORY)
                }
                MenuItemsEnum.ABOUT_AMIN -> {
                    val action = HomeActivityDirections.actionToAboutTermPolicy()
                    action.title = selectedItem.value
                    action.link = "https://www.merriam-webster.com/dictionary/dummy"
                    navController.navigate(action)
                }
                MenuItemsEnum.CONTACT_US -> {
                    val action = BottomGraphDirections.actionToContactUs()
                    navController.navigate(action)

                }
                MenuItemsEnum.PRIVACY_POLICY -> {
                    val action = HomeActivityDirections.actionToAboutTermPolicy()
                    action.title = selectedItem.value
                    action.link =
                        "https://policymaker.io/privacy-policy/?gclid=CjwKCAjwu5yYBhAjEiwAKXk_eCgrKBoCH_MGFoEZWaE4_RWXuqe23RIPFijurzAXzmu45_OFgeIVAxoCyXIQAvD_BwE"
                    navController.navigate(action)
                }
                MenuItemsEnum.TERM_CONDITION -> {
                    val action = HomeActivityDirections.actionToAboutTermPolicy()
                    action.title = selectedItem.value
                    action.link =
                        "https://termly.io/resources/templates/terms-and-conditions-template/"
                    navController.navigate(action)
                }

                MenuItemsEnum.SETTING -> {
                    val action = BottomGraphDirections.actionToSetting()
                    navController.navigate(action)
                }
            }
        }

        binding.sideMenuNav.llLogout.main.setOnClickListener {
            mActivityItem.showLoader("")
            NetworkClass.callApi(URLApi().logout(), object : ResponseNetwork {
                override fun onSuccessResponse(response: String?, message: String) {
                    mActivityItem.hideLoader()
                    LocalPreference.shared.removeAll()
                    startActivity(Intent(mActivityItem, MainActivity::class.java))
                    mActivityItem.finish()
                }

                override fun onErrorResponse(error: String?, response: String?) {
                    mActivityItem.hideLoader()
                    mActivityItem.showToast(error ?: "")
                }

            })
        }

    }

    private fun getProfileData() {
        showLoader("")
        NetworkClass.callApi(URLApi().getProfile(), object : ResponseNetwork {
            @SuppressLint("CheckResult")
            override fun onSuccessResponse(response: String?, message: String) {
                hideLoader()
                val json = JSONObject(response ?: "")
                val user = Gson().fromJson(json.toString(), UserItem::class.java)
                LocalPreference.shared.user = user
            }

            override fun onErrorResponse(error: String?, response: String?) {
                hideLoader()
                showToast(error ?: "")
            }

        })
    }

    private fun closeDrawer(direction: Int) {
        binding.drawerLayout.closeDrawer(direction, true)
    }

    fun hideBtmBar(hide: Boolean) {
        if (this::binding.isInitialized) {
            if (hide) {
                binding.topBar.bnve.root.viewGone()//   .viewGone()
            } else {
                binding.topBar.bnve.root.viewVisible()
            }
        }
    }
}

enum class BtmSelectedPosition {
    HISTORY,
    HOME,
    ACCOUNT,
}