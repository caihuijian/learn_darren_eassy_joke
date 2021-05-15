package com.example.testrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by hjcai on 2021/5/13.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<User> mList;
    private final Context mContext;
    private final LayoutInflater mInflater;

    // constructor
    public RecyclerViewAdapter(List<User> list, Context context, LayoutInflater inflater) {
        this.mList = list;
        this.mContext = context;
        this.mInflater = inflater;
    }

    /**
     * 创建条目ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType view的类型可以用来显示多种列表布局
     * @return recyclerview中单个的item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建item
        View itemView = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        // 创建ViewHolder
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 将数据填充到ViewHolder
        User user = mList.get(position);
        holder.mLeftTv.setText(user.getUserName());
        holder.mRightTv.setText(String.valueOf(user.getAge()));

        // 加载图片
        // holder.mImage.layout(0,0,0,0);
        Glide.with(mContext).load(user.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mLeftTv;
        public TextView mRightTv;
        public ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLeftTv = itemView.findViewById(R.id.leftTv);
            mRightTv = itemView.findViewById(R.id.rightTv);
            mImage = itemView.findViewById(R.id.image);
        }
    }
}
