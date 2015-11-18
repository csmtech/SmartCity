package com.csm.smartcity.information;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csm.smartcity.R;

import java.util.List;

public class RecyclerViewAnnouncementAdapter
        extends RecyclerView.Adapter
        <RecyclerViewAnnouncementAdapter.ListItemViewHolder> {

    private List<Model> items;
    private SparseBooleanArray selectedItems;

    RecyclerViewAnnouncementAdapter(List<Model> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.announcement, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        Model model = items.get(position);
        viewHolder.txtNews.setText(String.valueOf(model.txtNews));
        viewHolder.txtAnnouncementDate.setText(String.valueOf(model.txtAnnouncementDate));
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtNews;
        TextView txtAnnouncementDate;


        public ListItemViewHolder(View itemView) {
            super(itemView);
            txtNews = (TextView) itemView.findViewById(R.id.txtNews);
            txtAnnouncementDate = (TextView) itemView.findViewById(R.id.txtAnnouncementDate);

        }

    }
}
