package com.ainirobot.laddercontrolrobotos.laddercontrol;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ainirobot.coreservice.client.actionbean.Pose;
import com.ainirobot.laddercontrolrobotos.R;

import java.util.ArrayList;
import java.util.List;

public class SelectDestinationAdapter extends RecyclerView.Adapter<SelectDestinationAdapter.RoomViewHolder> {
    private Context mContext;
    private List<Pose> mDataList = new ArrayList<>();

    private int mSelectPosition = -1;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(Pose pose);
    }

    public SelectDestinationAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Pose> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    public void setClickListener(ItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setSelectPosition(int position) {
        mSelectPosition = position;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_room_list_item,
                parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Pose data = mDataList.get(position);
        holder.tvDestination.setText(data.getName());

        if (mSelectPosition == position) {
            holder.tvDestination.setTextColor(mContext.getColor(R.color.color_58c3fe));
            holder.layoutBg.setBackgroundResource(R.drawable.delivery_select_item_white_bg);
        } else {
            holder.tvDestination.setTextColor(mContext.getColor(R.color.white));
            holder.layoutBg.setBackgroundResource(R.drawable.delivery_select_item_bg);
        }
        holder.itemView.setOnClickListener(v -> {
            int selectPosition = position;
            mSelectPosition = selectPosition;
            notifyDataSetChanged();

            if (mClickListener != null) {
                mClickListener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        View layoutBg;
        TextView tvDestination;

        public RoomViewHolder(View itemView) {
            super(itemView);
            layoutBg = itemView.findViewById(R.id.select_destination_item_bg);
            tvDestination = itemView.findViewById(R.id.select_destination_item_text);
        }
    }
}
