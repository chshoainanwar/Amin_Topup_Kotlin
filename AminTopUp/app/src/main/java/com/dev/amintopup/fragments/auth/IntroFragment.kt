package com.dev.amintopup.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.databinding.FragIntroBinding
import com.dev.amintopup.fragments.adapters.IntroAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class IntroFragment : BaseFragment() {

    private var _binding: FragIntroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val vp: ViewPager2
        get() {
            return binding.vpIntro
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragIntroBinding.inflate(inflater, container, false)
        return binding.root

    }

    var currentPage = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp.adapter = IntroAdapter(mContext = mActivityItem)
        vp.orientation = ORIENTATION_HORIZONTAL
//        vp.isUserInputEnabled = false
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                checkBtnState()
            }
        })
        binding.cvBg.setOnClickListener {
            if (vp.currentItem == 2) {
                val action =
                    IntroFragmentDirections.actionIntroToLogin()
//                    IntroFragmentDirections.actionToAboutTermPolicy()
                findNavController().navigate(action)
            } else {
                currentPage += 1
                if (currentPage >= 3) {
                    currentPage = 2
                }
                vp.setCurrentItem(currentPage, true)
            }

        }
    }

    fun checkBtnState() {
        when (vp.currentItem) {
            0 -> {
                binding.cvBg.setCardBackgroundColor(mActivityItem.getColor(R.color.white))
                binding.ivNextOrTick.setImageResource(R.drawable.ic_arrow)
            }
            1 -> {
                binding.cvBg.setCardBackgroundColor(mActivityItem.getColor(R.color.white))
                binding.ivNextOrTick.setImageResource(R.drawable.ic_arrow)
            }
            2 -> {
                binding.cvBg.setCardBackgroundColor(mActivityItem.getColor(R.color.orangeClr))
                binding.ivNextOrTick.setImageResource(R.drawable.ic_check)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}