package com.dev.amintopup.fragments.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.base.Placeholders
import com.dev.amintopup.base.getSpecialToCustom
import com.dev.amintopup.base.loadImage
import com.dev.amintopup.databinding.ItemAllTopupBinding
import com.dev.amintopup.fragments.history.model.AllTopup

class HistoryAdapter(
    var context: BaseActivity,
    var list: ArrayList<AllTopup>,
    var callback2: ((transaction_id: Int, position: Int) -> Unit),
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_all_topup, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val mList = list[holder.absoluteAdapterPosition]
//        Glide.with(holder.binding.ivImage).load(mList.receiver_email).into(holder.binding.ivImage)
        holder.binding.ivImage.loadImage(mList.receiver_image,Placeholders.USER)
        holder.binding.tvAFN.text = "${mList.topup_amount?.toString() ?: "0.0"} AFN"
        holder.binding.tvName.text = mList.receiver_name
        holder.binding.tvNumber.text = mList.receiver_number
        val date = mList.created_at?.getSpecialToCustom(output = "MMMM dd, yyyy")
        val time = mList.created_at?.getSpecialToCustom(output = "HH:mm aa")

        val status = mList.status
        if (status == 0) {

            Glide.with(holder.binding.ivStatus).load(R.drawable.ic_iv_alert)
                .into(holder.binding.ivStatus)
        } else {
            Glide.with(holder.binding.ivStatus).load(R.drawable.ic_iv_tick)
                .into(holder.binding.ivStatus)
        }
        holder.binding.tvDateTime.text = "$date at $time"
        holder.itemView.setOnClickListener {
            val transactionId = mList.id ?: 0
            callback2.invoke(transactionId, holder.absoluteAdapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemAllTopupBinding = ItemAllTopupBinding.bind(itemView)
    }
}