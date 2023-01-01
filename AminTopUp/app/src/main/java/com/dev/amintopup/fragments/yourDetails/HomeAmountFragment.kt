package com.dev.amintopup.fragments.yourDetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.amintopup.base.BaseFragment
import com.dev.amintopup.base.curveitem.RVAdapter
import com.dev.amintopup.databinding.FragmentHomeAmountBinding
import com.dev.amintopup.fragments.home.homeModel.Detail
import com.dev.amintopup.fragments.home.homeModel.HomeModel
import com.dev.amintopup.network.Placeholders
import com.dev.amintopup.network.loadImage
import com.gtomato.android.ui.transformer.WheelViewTransformer
import com.gtomato.android.ui.widget.CarouselView
import com.intuit.sdp.R


class HomeAmountFragment : BaseFragment() {
    private var _binding: FragmentHomeAmountBinding? = null
    private val binding get() = _binding!!
    private var mPrices = ArrayList<String>()
    private var getNumber: String = ""
    private var getNumberFromEdit: String = ""
    private var getName: String = ""
    private var getEmail: String = ""
    private var position = 0
    var list : Detail = Detail()
    private val carousel: CarouselView
        get() {
            return binding.carousel
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeAmountBinding.inflate(inflater, container, false)

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()

        val getNetworkData: HomeModel = arguments?.getSerializable("operator") as HomeModel
        getNumber = arguments?.getString("number", "") ?: ""
        getName = arguments?.getString("name", "") ?: ""
        getEmail = arguments?.getString("email", "") ?: ""

        binding.tvNumber.text = getNumber
        binding.tvName.text = getName
        binding.tvOperatorName.text = getNetworkData.operator_name
        binding.ivNetwork.loadImage(getNetworkData.operator_image, Placeholders.USER)


        if (getName.isEmpty()) {
            binding.rlName.visibility = View.GONE

        }
        if (getEmail.isEmpty()) {
            binding.rlEmail.visibility = View.INVISIBLE
        }
        if (getPhoneDeviceName() == "Huawei") {
            val param = carousel.layoutParams
            param.height = carousel.context.resources.getDimension(R.dimen._230sdp).toInt()
            carousel.layoutParams = param
        }else if (getPhoneDeviceName() == "OPPO") {
            val param = carousel.layoutParams
            param.height = carousel.context.resources.getDimension(R.dimen._230sdp).toInt()
            carousel.layoutParams = param
        }  else if (getPhoneDeviceName() == "vivo" || getPhoneDeviceName() == "Redmi") {
            val param = carousel.layoutParams
            param.height = carousel.context.resources.getDimension(R.dimen._240sdp).toInt()
            carousel.layoutParams = param
        } else {
            val param = carousel.layoutParams
            param.height = carousel.context.resources.getDimension(R.dimen._250sdp).toInt()
            carousel.layoutParams = param

        }

        mPrices.clear()
        mPrices.add("100\nAFN")
        mPrices.add("150\nAFN")
        mPrices.add("200\nAFN")
        mPrices.add("250\nAFN")
        mPrices.add("500\nAFN")
        mPrices.add("750\nAFN")
        mPrices.add("1000\nAFN")
        mPrices.add("1500\nAFN")
        mPrices.add("2000\nAFN")
        mActivityDashboard?.hideBtmBar(true)
        val adapter = RVAdapter(mActivityItem, mPrices)
        carousel.transformer = WheelViewTransformer()
        carousel.adapter = adapter
        carousel.post {
            position = kotlin.math.floor(mPrices.size.toDouble() / 2.toDouble()).toInt()
            carousel.scrollToPosition(position)
            binding.tvPayment.text = mPrices[position].replace("\n", " ")
            adapter.updateSelected(position)
            matchAmountWithList()
        }
        carousel.setOnItemClickListener { _, _, position, adapterPosition ->
            this.position = adapterPosition
            adapter.updateSelected(position)
            binding.tvPayment.text = mPrices[this.position].replace("\n", " ")
            matchAmountWithList()
        }
        carousel.setOnScrollListener(object : CarouselView.OnScrollListener() {
            override fun onScrollEnd(carouselView: CarouselView?) {
                super.onScrollEnd(carouselView)
                position = carouselView?.currentAdapterPosition ?: 0
                adapter.updateSelected(position)
                binding.tvPayment.text = mPrices[position].replace("\n", " ")
                matchAmountWithList()
            }
        })
        binding.btnLow.setOnClickListener {
            position -= 1
            if (position < 0) {
                position = 0
            }
            carousel.smoothScrollToPosition(position)
            binding.tvPayment.text = mPrices[position].replace("\n", " ")
            adapter.updateSelected(position)
            matchAmountWithList()
        }
        binding.btnUp.setOnClickListener {
            position += 1
            if (position >= mPrices.size) {
                position = (mPrices.size - 1)
            }
            carousel.smoothScrollToPosition(position)
            binding.tvPayment.text = mPrices[position].replace("\n", " ")
            adapter.updateSelected(position)
            matchAmountWithList()

        }



        binding.btnContinue.setOnClickListener {
            matchAmountWithList()
            val amount = binding.tvPayment.text.toString()
            val bundle = Bundle()
            bundle.putSerializable("operator", getNetworkData)
            bundle.putSerializable("list", list)
            bundle.putString("number", getNumber)
            bundle.putString("amount", amount)
            bundle.putString("email", getEmail)
            bundle.putString("name", getName)
            findNavController().navigate(
                com.dev.amintopup.R.id.action_homeAmountFragment_to_orderSummaryFragment,
                bundle
            )
        }
        binding.ivNameEdit.setOnClickListener {
            val amount = binding.tvPayment.text.toString()
            val action =
                HomeAmountFragmentDirections.actionHomeAmountFragmentToEditPhoneFragment()
            val bundle = Bundle()
            bundle.putString("From","HomeAmount")
            bundle.putString("email", getEmail)
            bundle.putString("name", getName)

            findNavController().navigate(com.dev.amintopup.R.id.action_homeAmountFragment_to_editPhoneFragment,bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    }

    private fun matchAmountWithList(){
        val getNetworkData: HomeModel = arguments?.getSerializable("operator") as HomeModel
        val getAmount = binding.tvPayment.text.toString()
        val splitAmount =getAmount.split(" ")
        val selectedAmount = splitAmount[0]
        val checkAmount = getNetworkData.details!!

        checkAmount.forEachIndexed { index, detail ->
            if (checkAmount[index].denomination == selectedAmount.toDouble())
            {
                list =  checkAmount[index]
                binding.tvChargeableAmount.text = list.totalAmount.toString()
            }
        }
    }



    private fun setTopBar() {
        binding.topBar.tvTitle.visibility = View.INVISIBLE
        binding.topBar.btnRight.visibility = View.INVISIBLE
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}