package com.example.user.contactlist.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.user.contactlist.R;
import com.example.user.contactlist.databinding.ItemContactBinding;
import com.example.user.contactlist.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.zhouzhuo.zzletterssidebar.adapter.BaseSortRecyclerViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseRecyclerViewHolder;

public class ContactAdapter extends BaseSortRecyclerViewAdapter<Contact, BaseRecyclerViewHolder> {//RecyclerView.Adapter<ContactAdapter.ContactViewHolder>

    private ItemFilter mFilter;


    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {

        if (holder instanceof ContactViewHolder) {
            //must add this
            final int mPos = position - getHeadViewSize();

            if (mPos < mDatas.size()) {

                ContactViewHolder holderCVH = (ContactViewHolder) holder;
//                initLetter(holderCVH, mPos);
                holder.tvLetter.setVisibility(View.GONE);
                holderCVH.onBind(mDatas.get(mPos), clickListener);
                initClickListener(holder, mPos);
            }
        } else if (holder instanceof HeaderHolder) {

        } else if (holder instanceof FooterHolder) {
            ((FooterHolder) holder).tvFoot.setText(getContentCount() + "位联系人");
        }
    }


    ContactAdapter(Context context, List<Contact> contacts) {
        super(context, contacts);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_contact;
    }

    @Override
    public int getHeaderLayoutId() {
        return 0;//R.layout.list_item_head;
    }

    //add a footer, optional, if no need, return 0
    @Override
    public int getFooterLayoutId() {
        return 0;//R.layout.list_item_foot;
    }

    @Override
    public BaseRecyclerViewHolder getViewHolder(View itemView, int type) {
        switch (type) {
            case BaseSortRecyclerViewAdapter.TYPE_HEADER:
                return new HeaderHolder(itemView);
            case BaseSortRecyclerViewAdapter.TYPE_FOOT:
                return new FooterHolder(itemView);
        }
        ItemContactBinding binding = ItemContactBinding.bind(itemView);
        return new ContactViewHolder(binding);
    }


    class ContactViewHolder extends BaseRecyclerViewHolder {//RecyclerView.ViewHolder

        private final ItemContactBinding binding;

        ContactViewHolder(ItemContactBinding itemContactBinding) {
            super(itemContactBinding.getRoot());
            this.binding = itemContactBinding;
        }

        void onBind(Contact contact, OnRecyclerViewClickListener clickListener) {

            binding.contactName.setText(contact.getName());
            binding.contactNumber.setText(contact.getPhoneNumber());
            binding.selectedContactLayout.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onClick(itemView, getAbsoluteAdapterPosition());
            });
            if (contact.getPhotoUri() != null) {
                binding.drawableTextView.setVisibility(View.GONE);
                binding.contactPhoto.setVisibility(View.VISIBLE);
                Contact.loadImage(binding.contactPhoto, contact.getPhotoUri());
            } else {
                binding.contactPhoto.setVisibility(View.GONE);
                binding.drawableTextView.setVisibility(View.VISIBLE);
                GradientDrawable gradientDrawable = (GradientDrawable) binding.drawableTextView.getBackground();
                gradientDrawable.setColor(getRandomColor());
                try{

                    String serviceSubString = (contact.getName().substring(0, 2));
                    binding.drawableTextView.setText(serviceSubString.toUpperCase());
                }catch (StringIndexOutOfBoundsException e){

                    binding.drawableTextView.setText(contact.getName());
                }
            }
            binding.executePendingBindings();
        }
    }

    public static class HeaderHolder extends BaseRecyclerViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterHolder extends BaseRecyclerViewHolder {

        protected TextView tvFoot;

        public FooterHolder(View itemView) {
            super(itemView);
            tvFoot = (TextView) itemView.findViewById(R.id.tv_foot);
        }
    }

    /*        *
     * Here is the key method to apply the animation*/
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
    }

    /**
     * @return a random color which is used a background by
     * services initial letters
     */
    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    /**
     * Gets filter.
     *
     * @return the filter
     */
    public Filter getFilter(List<Contact> items) {
        if (mFilter == null) {
            mFilter = new ItemFilter(items);
        }
        return mFilter;
    }

    private class ItemFilter extends Filter {
        List<Contact> mDatesActual;

        public ItemFilter(List<Contact> mDatesActual) {
            this.mDatesActual = mDatesActual;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Contact> list = mDatesActual;
            final List<Contact> result_list = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getName();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }
            results.values = result_list;
            results.count = result_list.size();
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDatas = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    }
}
