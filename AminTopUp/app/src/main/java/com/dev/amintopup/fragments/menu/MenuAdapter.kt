package com.dev.amintopup.fragments.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.amintopup.R
import com.dev.amintopup.base.BaseActivity
import com.dev.amintopup.databinding.ItemMenuBinding
import java.io.Serializable

class MenuAdapter(
    private var items: Array<MenuItemsEnum> = MenuItemsEnum.values(),
    private var mContext: BaseActivity,
    var callBack: ((MenuItemsEnum) -> Unit)
) : RecyclerView.Adapter<MenuVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVH {
        return MenuVH(LayoutInflater.from(mContext).inflate(R.layout.item_menu, parent, false))
    }

    override fun onBindViewHolder(holder: MenuVH, position: Int) {
        val item = items[position]
        holder.binding.tvMenu.text = item.value
        when (item) {
            MenuItemsEnum.MY_ACCOUNT -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_profile)
            }
            MenuItemsEnum.P_HISTORY -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_history)
            }
            MenuItemsEnum.ABOUT_AMIN -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_about)
            }
            MenuItemsEnum.CONTACT_US -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_contact_us)
            }
            MenuItemsEnum.PRIVACY_POLICY -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_privacy)
            }
            MenuItemsEnum.TERM_CONDITION -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_term)
            }
            MenuItemsEnum.SETTING -> {
                holder.binding.ivMenu.setImageResource(R.drawable.ic_menu_setting)
            }
        }
        holder.itemView.setOnClickListener {
            callBack.invoke(item)
        }
    }

    override fun getItemCount(): Int = items.count()
}

class MenuVH(item: View) : RecyclerView.ViewHolder(item) {
    val binding: ItemMenuBinding = ItemMenuBinding.bind(item)
}

enum class MenuItemsEnum(var value: String) : Serializable {
    MY_ACCOUNT("My Account"),
    P_HISTORY("Purchase History"),
    ABOUT_AMIN("About AminTopup"),
    CONTACT_US("Contact Us"),
    PRIVACY_POLICY("Privacy Policy"),
    TERM_CONDITION("Terms & Condition"),
    SETTING("Setting")
}