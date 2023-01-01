package com.dev.amintopup.base.curveitem;

import static android.view.LayoutInflater.from;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.amintopup.R;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;

/**
 * Created by SachinR on 7/5/2017.
 */
public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private RecyclerView manager;
    private Context context;
    private int selectedPosition = 0;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        manager = recyclerView;
    }

    public void updateSelected(int pos) {
        int oldPosition = selectedPosition;
        selectedPosition = pos;
        notifyDataSetChanged();
//        notifyItemChanged(oldPosition);
//        notifyItemChanged(selectedPosition);
    }

    private ArrayList<String> mItems;

    public RVAdapter(Context context, ArrayList<String> items) {
        inflater = from(context);
        mItems = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_rv_text, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder hold = (Holder) holder;
        if (selectedPosition == position) {
            hold.img.setColorFilter(ContextCompat.getColor(hold.img.getContext(), R.color.orangeClr), android.graphics.PorterDuff.Mode.SRC_IN);
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) hold.img.getLayoutParams();
            param.width = (int) hold.img.getContext().getResources().getDimension(com.intuit.sdp.R.dimen._70sdp);
            param.height = (int) hold.img.getContext().getResources().getDimension(com.intuit.sdp.R.dimen._70sdp);
            hold.img.setLayoutParams(param);
        } else {
            hold.img.setColorFilter(ContextCompat.getColor(hold.img.getContext(), R.color.unselectedClr), android.graphics.PorterDuff.Mode.SRC_IN);
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) hold.img.getLayoutParams();
            param.width = (int) hold.img.getContext().getResources().getDimension(com.intuit.sdp.R.dimen._63sdp);
            param.height = (int) hold.img.getContext().getResources().getDimension(com.intuit.sdp.R.dimen._63sdp);
            hold.img.setLayoutParams(param);
        }
        hold.tv.setText(mItems.get(holder.getAbsoluteAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;

        Holder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tvText);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAbsoluteAdapterPosition();//getAdapterPosition();
                    manager.smoothScrollToPosition(adapterPosition);
                }
            });
        }
    }
}
