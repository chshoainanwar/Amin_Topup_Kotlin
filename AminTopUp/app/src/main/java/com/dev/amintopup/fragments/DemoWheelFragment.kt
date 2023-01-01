package com.dev.amintopup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.MaterialColor
import com.dev.amintopup.base.MaterialColor.getContrastColor
import com.dev.amintopup.base.MaterialColorAdapter
import com.dev.amintopup.base.animtation.CircularWheelView
import com.dev.amintopup.databinding.FragDemoWheelBinding
import com.lukedeighton.wheelview.WheelView
import com.lukedeighton.wheelview.WheelView.OnWheelItemClickListener


class DemoWheelFragment : BaseFragment() {

    private var _binding: FragDemoWheelBinding? = null
    private val ITEM_COUNT = 20

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val wheelView: WheelView
        get() {
            return binding.wheelview
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragDemoWheelBinding.inflate(inflater, container, false)

        return binding.root

    }

    private fun setWheelPicker() {
        val list = ArrayList<String>()
        (0..59).forEach {
            list.add("$it")
        }
        binding.circularWheelPickerOne.setDataSet(list)
        binding.circularWheelPickerOne.setWheelItemSelectionListener(object :
            CircularWheelView.WheelItemSelectionListener {
            override fun onItemSelected(index: Int) {
                showToast("Selected position is : $index")
                showToast("Get Current Item : ${binding.circularWheelPickerOne.getCurrentItem()}")
                showToast("Get Current Position : ${binding.circularWheelPickerOne.getCurrentPosition()}")
//                Log.d(TAG, "Selected position is : $index")
//                Log.d(TAG, "Get Current Item : ${binding.circularWheelPickerOne.getCurrentItem()}")
//                Log.d(TAG, "Get Current Position : ${binding.circularWheelPickerOne.getCurrentPosition()}")
            }
        })

        val list2 = ArrayList<String>()
        (0..59).forEach {
            list2.add(it.toString())
        }

        binding.circularWheelPickerTwo.setDataSet(list2)
        binding.circularWheelPickerTwo.setWheelItemSelectionListener(object :
            CircularWheelView.WheelItemSelectionListener {
            override fun onItemSelected(index: Int) {
                showToast("Selected position is : $index")
                showToast("Get Current Item : ${binding.circularWheelPickerTwo.getCurrentItem()}")
                showToast("Get Current Position : ${binding.circularWheelPickerTwo.getCurrentPosition()}")
//                Log.d(TAG, "Selected position is : $index")
//                Log.d(TAG, "Get Current Item : ${binding.circularWheelPickerTwo.getCurrentItem()}")
//                Log.d(TAG, "Get Current Position : ${binding.circularWheelPickerTwo.getCurrentPosition()}")
            }
        })

        binding.left.setOnClickListener {
            binding.circularWheelPickerOne.setCurrentPosition(
                binding.position.text.toString().toInt()
            )
        }
        binding.right.setOnClickListener {
            binding.circularWheelPickerTwo.setCurrentPosition(
                binding.position.text.toString().toInt()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWheelPicker()

        val f = ResourcesCompat.getFont(mActivityItem, R.font.poppins_bold)
        binding.left.typeface = f
        val entries: ArrayList<Map.Entry<String, Int>> =
            ArrayList<Map.Entry<String, Int>>(ITEM_COUNT)
        for (i in 0 until ITEM_COUNT) {
            val entry = MaterialColor.random(mActivityItem, "\\D*_500$")
            entries.add(entry)
        }
        wheelView.adapter = MaterialColorAdapter(entries)

        //a listener for receiving a callback for when the item closest to the selection angle changes

        //a listener for receiving a callback for when the item closest to the selection angle changes
        wheelView.setOnWheelItemSelectedListener { parent, _, position -> //get the item at this position
            val selectedEntry: Map.Entry<String, Int> =
                (parent.adapter as MaterialColorAdapter).getItem(position)
            parent.setSelectionColor(getContrastColor(selectedEntry))
        }

        wheelView.onWheelItemClickListener =
            OnWheelItemClickListener { _, position, isSelected ->
                val msg = "$position $isSelected"
                Toast.makeText(mActivityItem, msg, Toast.LENGTH_SHORT).show()
            }

        //initialise the selection drawable with the first contrast color

        //initialise the selection drawable with the first contrast color
        wheelView.setSelectionColor(getContrastColor(entries[0]))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}