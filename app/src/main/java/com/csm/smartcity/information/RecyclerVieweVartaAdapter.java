package com.csm.smartcity.information;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import com.csm.smartcity.R;
public class RecyclerVieweVartaAdapter
        extends RecyclerView.Adapter
        <RecyclerVieweVartaAdapter.ListItemViewHolder> {

    private List<Model> items;
    private SparseBooleanArray selectedItems;

    RecyclerVieweVartaAdapter(List<Model> modelData) {
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
                inflate(R.layout.item_evarta, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        Model model = items.get(position);
        viewHolder.name.setText(String.valueOf(model.name));
        viewHolder.txtDay.setText(String.valueOf(model.txtDay));
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView txtDay;
        TextView txtPath;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtCompUser);
            txtDay = (TextView) itemView.findViewById(R.id.txtday);
        }

    }
}
