package com.grotesque.saa.common.adapter;

import android.support.v7.widget.RecyclerView;

import com.grotesque.saa.board.adapter.NewBoardAdapter;
import com.grotesque.saa.common.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiItemAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Row<?>> mRows = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBindView(getItem(position));
    }

    @SuppressWarnings("unchecked")
    public <ITEM> ITEM getItem(int position) {
        return (ITEM) mRows.get(position).getItem();
    }
    public <ITEM> List<ITEM> getAllItem(){
        List<ITEM> items = new ArrayList<>();
        for(int i=0; i<mRows.size(); i++){
            items.add((ITEM) mRows.get(i).getItem());
        }
        return items;
    }
    public void setItemViewType(int position, int itemViewType){
        if(mRows.get(position).getItemViewType() == NewBoardAdapter.CARD_HOLDER || mRows.get(position).getItemViewType() == NewBoardAdapter.BASIC_HOLDER)
            mRows.get(position).setItemViewType(itemViewType);
    }
    public void setRows(List<Row<?>> mRows) {
        this.mRows.clear();
        this.mRows.addAll(mRows);
    }
    public void setRow(Row<?> mRow) {
        this.mRows.add(mRow);
    }
    public void setRow(int position, Row<?> mRow) {
        this.mRows.add(position, mRow);
    }

    public void removeRow(int position) {
        this.mRows.remove(position);
    }

    public boolean hasItemViewType(int itemViewType){
        for(Row r : mRows){
            if(r.getItemViewType() == itemViewType)
                return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return mRows.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mRows.get(position).getItemViewType();
    }

    public static class Row<ITEM> {
        private ITEM item;
        private int itemViewType;

        private Row(ITEM item, int itemViewType) {
            this.item = item;
            this.itemViewType = itemViewType;
        }
        
        public static <T> Row<T> create(T item, int itemViewType) {
            return new Row<>(item, itemViewType);
        }

        public ITEM getItem() {
            return item;
        }

        public int getItemViewType() {
            return itemViewType;
        }

        public void setItemViewType(int itemViewType){
            this.itemViewType = itemViewType;
        }
    }

}