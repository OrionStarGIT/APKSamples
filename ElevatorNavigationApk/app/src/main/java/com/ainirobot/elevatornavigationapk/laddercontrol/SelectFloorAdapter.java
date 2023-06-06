package com.ainirobot.elevatornavigationapk.laddercontrol;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ainirobot.coreservice.bean.MultiFloorInfo;
import com.ainirobot.elevatornavigationapk.R;

import java.util.ArrayList;
import java.util.List;

public class SelectFloorAdapter extends RecyclerView.Adapter<SelectFloorAdapter.FloorViewHolder> {
    private Context mContext;
    private List<MultiFloorInfo> mDataList = new ArrayList<>();

    private int mSelectPosition = 0;
    private FloorClickListener mClickListener;

    public interface FloorClickListener {
        void onFloorClick(int position);
    }

    public SelectFloorAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<MultiFloorInfo> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    public void setClickListener(FloorClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setSelectPosition(int position) {
        mSelectPosition = position;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_floor_list_item,
                parent, false);
        return new FloorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FloorViewHolder holder, int position) {
        MultiFloorInfo data = mDataList.get(position);
        holder.tvFloor.setText(data.getFloorAlias());

        if (mSelectPosition == position) {
            holder.tvFloor.setTextColor(mContext.getColor(R.color.confirm_color));
        } else {
            holder.tvFloor.setTextColor(mContext.getColor(R.color.black));
        }

        final int p = position;
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(mSelectPosition);
            mSelectPosition = p;
            notifyItemChanged(mSelectPosition);

            if (mClickListener != null) {
                mClickListener.onFloorClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class FloorViewHolder extends RecyclerView.ViewHolder {
        TextView tvFloor;

        public FloorViewHolder(View itemView) {
            super(itemView);
            tvFloor = itemView.findViewById(R.id.select_floor_item_text);
        }
    }
}
