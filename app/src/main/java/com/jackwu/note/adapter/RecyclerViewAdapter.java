package com.jackwu.note.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

/**
 * Created by JackWu
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private int layout;
    private List<T> ts;
    private OnRecyclerViewItemClickListener listener;//item点击监听
    private OnRecyclerViewItemLongClickListener longListener;

    public RecyclerViewAdapter(int layout, List<T> ts) {
        this.ts = ts;
        this.layout = layout;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        populate(holder, ts.get(position));
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }
        if (longListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longListener.onItemLongClick(view, holder.getAdapterPosition());
                    return true;
                }
            });
        }

    }


    public abstract void populate(RecyclerViewHolder holder, T t);//抽象方法，适配数据及view

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongListener(OnRecyclerViewItemLongClickListener listener) {
        this.longListener = listener;
    }

    public T getItem(int position) {
        return ts.get(position);
    }

    @Override
    public int getItemCount() {
        return ts == null ? 0 : ts.size();
    }
}
