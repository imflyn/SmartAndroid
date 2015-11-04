package com.flyn.smartandroid.ui.interfaces;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RecyclerListBaseAdapter<T> extends RecyclerView.Adapter {

    protected LayoutInflater mInflater;
    protected List<T> data = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected Activity mContext;


    public RecyclerListBaseAdapter(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        this.mContext = (Activity) mRecyclerView.getContext();
        this.mInflater = LayoutInflater.from(mContext);
    }

    public synchronized void setData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public synchronized void setItem(int location, T item) {
        this.data.set(location, item);
        notifyDataSetChanged();
    }

    public synchronized void addItem(T item) {
        this.data.add(item);
        notifyItemInserted(this.data.size() - 1);
    }

    public synchronized void addItem(int location, T item) {
        this.data.add(location, item);
        notifyItemInserted(location);
    }

    public synchronized void addAll(List<T> items) {
        this.data.addAll(items);
        notifyDataSetChanged();
    }

    public synchronized void addAll(int location, List<T> items) {
        this.data.addAll(location, items);
        notifyDataSetChanged();
    }

    public synchronized void removeItem(T item) {
        int location = this.data.indexOf(item);
        this.data.remove(item);
        notifyItemRemoved(location);
    }

    public synchronized void removeItem(int location) {
        this.data.remove(location);
        notifyItemRemoved(location);
    }

    public synchronized void removeAll(List<T> items) {
        this.data.removeAll(items);
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public synchronized void swap(int index1, int index2) {
        Collections.swap(this.data, index1, index2);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public T getItem(int position) {
        return data.get(position);
    }
}