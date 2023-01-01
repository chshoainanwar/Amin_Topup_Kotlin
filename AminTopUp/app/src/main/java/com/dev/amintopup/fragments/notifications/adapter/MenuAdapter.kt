package com.dev.amintopup.fragments.notifications.adapter

import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binaryfork.spanny.Spanny
import com.bumptech.glide.Glide
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.databinding.ItemNotificationBinding
import com.dev.amintopup.fragments.notifications.model.Data


class NotificationAdapter(
    private var items: ArrayList<Data>,
    private var mContext: BaseActivity,
    var callBack: ((Int) -> Unit)
) : RecyclerView.Adapter<NotificationVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        return NotificationVH(
            LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        val item = items[position]
        holder.binding.tvNotification.text = item.message
        val status = item.notification_status
        if (status == 0) {
            Glide.with(holder.binding.ivStatus).load(R.drawable.ic_iv_alert)
                .into(holder.binding.ivStatus)
        } else {
            Glide.with(holder.binding.ivStatus).load(R.drawable.ic_iv_tick)
                .into(holder.binding.ivStatus)
        }

        
//        if (position % 2 == 0) {
//            holder.binding.mainLL.setBackgroundColor(mContext.getColor(R.color.light_blue))
//            val spanny = Spanny(
//                "Unfortunately, Your Topup transaction was not successful due to",
//                ForegroundColorSpan(mContext.getColor(R.color.darkStrokeAndTextClr))
//            ).append(" [Error Description]", ForegroundColorSpan(mContext.getColor(R.color.redClr)))
//                .append(".", ForegroundColorSpan(mContext.getColor(R.color.darkStrokeAndTextClr)))
//            holder.binding.tvNotification.text = spanny
//        } else {
//            holder.binding.mainLL.setBackgroundColor(mContext.getColor(R.color.white))
//            val spanny = Spanny(
//                "Topup",
//                ForegroundColorSpan(mContext.getColor(R.color.darkStrokeAndTextClr))
//            ).append(" successfully", ForegroundColorSpan(mContext.getColor(R.color.greenClr)))
//                .append(
//                    " sent to Ali. Thank you for using Amin Topup!",
//                    ForegroundColorSpan(mContext.getColor(R.color.darkStrokeAndTextClr))
//                )
//            holder.binding.tvNotification.text = spanny
//        }
//        Unfortunately, Your Topup transaction was not successful due to [Error Description].
//        val spanny = Spanny("StyleSpan", StyleSpan(Typeface.BOLD_ITALIC))
//            .append("CustomTypefaceSpan", CustomTypefaceSpan(typeface))
//            .append("CustomAlignmentSpan", CustomAlignmentSpan(CustomAlignmentSpan.RIGHT_TOP))
//            .append("\nUnderlineSpan, ", UnderlineSpan())
//            .append(" TypefaceSpan, ", TypefaceSpan("serif"))
//            .append("URLSpan, ", URLSpan("google.com"))
//            .append("StrikethroughSpan", StrikethroughSpan())
//            .append("\nQuoteSpan", QuoteSpan(Color.RED))
//            .append("\nPlain text")
//            .append("SubscriptSpan", SubscriptSpan())
//            .append("SuperscriptSpan", SuperscriptSpan())
//            .append("\n\nBackgroundSpan", BackgroundColorSpan(Color.LTGRAY))
//            .append("\n\nCustomBackgroundSpan", CustomBackgroundSpan(Color.DKGRAY, dp(16)))
//            .append("\n\nForegroundColorSpan", ForegroundColorSpan(Color.LTGRAY))
//            .append("\nAlignmentSpan", AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER))
//            .append(
//                "\nTextAppearanceSpan\n",
//                TextAppearanceSpan(this, android.R.style.TextAppearance_Medium)
//            )
//            .append(
//                "ImageSpan",
//                ImageSpan(ApplicationProvider.getApplicationContext(), R.mipmap.ic_launcher)
//            )
//            .append("\nRelativeSizeSpan", RelativeSizeSpan(1.5f))
//            .append(
//                "\n\nMultiple spans",
//                StyleSpan(Typeface.ITALIC),
//                UnderlineSpan(),
//                TextAppearanceSpan(this, android.R.style.TextAppearance_Large),
//                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
//                BackgroundColorSpan(Color.LTGRAY)
//            )
//        val spanny = Spanny(
//            "Unfortunately, Your Topup transaction was not successful due to",
//            ForegroundColorSpan(mContext.getColor(R.color.darkStrokeAndTextClr))
//        ).append(" [Error Description]", ForegroundColorSpan(mContext.getColor(R.color.redClr)))
//            .append(".", ForegroundColorSpan(mContext.getColor(R.color.darkStrokeAndTextClr)))
//        holder.binding.tvNotification.setText(spanny)
    }

    override fun getItemCount(): Int = items.count()
}

class NotificationVH(item: View) : RecyclerView.ViewHolder(item) {
    val binding: ItemNotificationBinding = ItemNotificationBinding.bind(item)
}
