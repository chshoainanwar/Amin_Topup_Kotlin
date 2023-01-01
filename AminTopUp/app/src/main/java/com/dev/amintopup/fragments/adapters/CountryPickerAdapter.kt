package com.dev.amintopup.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.Country
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.base.viewGone
import com.dev.amintopup.databinding.CountryItemBinding

class CountryPickerAdapter(
    private var mData: ArrayList<Country>,
    private var mContext: BaseActivity,
    var onCallBack: ((Country) -> Unit)
) : RecyclerView.Adapter<CountryVH>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryVH {
        return CountryVH(
            LayoutInflater.from(mContext).inflate(
                R.layout.country_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CountryVH, position: Int) {
        val item = mData[position]
        holder.binding.ivFields.setImageResource(item.flagResource)
        holder.binding.tvPhoneCode.text = item.name
        holder.itemView.setOnClickListener {
            onCallBack.invoke(item)
        }
    }

    override fun getItemCount(): Int = mData.count()
}

class CountryVH(item: View) : RecyclerView.ViewHolder(item) {
    val binding: CountryItemBinding = CountryItemBinding.bind(item)
}

class LanguagePickerAdapter(
    private var mData: ArrayList<String>,
    private var mContext: BaseActivity,
    var onCallBack: ((String) -> Unit)
) : RecyclerView.Adapter<CountryVH>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryVH {
        return CountryVH(
            LayoutInflater.from(mContext).inflate(
                R.layout.country_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CountryVH, position: Int) {
        val item = mData[position]
        holder.binding.ivFields.viewGone()
        holder.binding.tvPhoneCode.text = item
        holder.itemView.setOnClickListener {
            onCallBack.invoke(item)
        }
    }

    override fun getItemCount(): Int = mData.count()
}