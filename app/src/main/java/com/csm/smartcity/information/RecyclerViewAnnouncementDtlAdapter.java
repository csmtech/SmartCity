package com.csm.smartcity.information;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csm.smartcity.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

public class RecyclerViewAnnouncementDtlAdapter
        extends RecyclerView.Adapter
        <RecyclerViewAnnouncementDtlAdapter.ListItemViewHolder> {

    private List<Model> items;
    private SparseBooleanArray selectedItems;

    RecyclerViewAnnouncementDtlAdapter(List<Model> modelData) {
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
                inflate(R.layout.item_announcement, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        Model model = items.get(position);
        viewHolder.txtAnnouncement.setHtmlFromString(String.valueOf(model.txtAnnouncement), new HtmlTextView.RemoteImageGetter());
        /*viewHolder.txtDay.setText(String.valueOf(model.txtDay));*/
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        HtmlTextView txtAnnouncement;


        public ListItemViewHolder(View itemView) {
            super(itemView);
            txtAnnouncement = (HtmlTextView) itemView.findViewById(R.id.txtAnnouncement);

        }

    }
}
