package com.dev.amintopup.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.databinding.IntroItemBinding
import java.io.Serializable

class IntroAdapter(
    private var mData: ArrayList<ItemIntroModel> = ItemIntroModel.getAllItem(),
    private var mContext: BaseActivity
) : RecyclerView.Adapter<IntroVH>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IntroVH {
        return IntroVH(
            LayoutInflater.from(mContext).inflate(
                R.layout.intro_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IntroVH, position: Int) {
        val item = mData[position]
        item.drawable?.let { holder.binding.ivIntro.setImageResource(it) }
        holder.binding.tvIntro.text = item.txt
    }

    override fun getItemCount(): Int = mData.count()
}

class IntroVH(item: View) : RecyclerView.ViewHolder(item) {
    val binding: IntroItemBinding = IntroItemBinding.bind(item)
}

data class ItemIntroModel(
    var id: Int? = 0,
    var drawable: Int? = 0,
    var txt: String? = ""
) : Serializable {
    companion object {
        fun getAllItem(): ArrayList<ItemIntroModel> {
            val items = ArrayList<ItemIntroModel>()
            items.add(
                ItemIntroModel(
                    0,
                    R.drawable.ic_intro_1,
                    "Send Topup to your loved one within\nseconds."
                )
            )
            items.add(
                ItemIntroModel(
                    1,
                    R.drawable.ic_intro_2,
                    "Securely buy Topup with any Credit\nCards. We are not saving your\npayment information!"
                )
            )
            items.add(
                ItemIntroModel(
                    2,
                    R.drawable.ic_intro_3,
                    "With Amin Topup, You can recharge\nany Phone Number in Afghanistan,\nno matter where you live!"
                )
            )
            return items
        }
    }
}