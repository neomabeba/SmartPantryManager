package com.example.smartpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.Holder> {

    public interface Listener {
        void edit(Ingredient i);
        void delete(Ingredient i);
    }

    private List<Ingredient> originalList;
    private List<Ingredient> filteredList;
    private final Listener listener;

    public PantryAdapter(List<Ingredient> items, Listener listener) {
        this.originalList = items != null ? items : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.originalList);
        this.listener = listener;
    }

    public void setItems(List<Ingredient> x) {
        this.originalList = x != null ? x : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.originalList);
        notifyDataSetChanged();
    }

    public void filter(String query, String statusFilter) {
        filteredList.clear();
        String q = query == null ? "" : query.toLowerCase(Locale.US).trim();

        for (Ingredient i : originalList) {
            boolean matchesName = i.name.toLowerCase(Locale.US).contains(q);
            DateUtils.Status status = DateUtils.getExpiryStatus(i.expiry);

            boolean matchesStatus = true;
            if ("EXPIRING_SOON".equalsIgnoreCase(statusFilter)) {
                matchesStatus = (status == DateUtils.Status.EXPIRING_SOON);
            } else if ("EXPIRED".equalsIgnoreCase(statusFilter)) {
                matchesStatus = (status == DateUtils.Status.EXPIRED);
            } else if ("FRESH".equalsIgnoreCase(statusFilter)) {
                matchesStatus = (status == DateUtils.Status.FRESH);
            }

            if (matchesName && matchesStatus) {
                filteredList.add(i);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_pantry, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Ingredient i = filteredList.get(pos);
        Context context = h.itemView.getContext();

        h.name.setText(i.name);

        String formattedQty = (i.quantity % 1 == 0) ? String.format(Locale.US, "%.0f", i.quantity) : String.format(Locale.US, "%.2f", i.quantity);
        h.qty.setText("Quantity: " + formattedQty + " " + i.unit);

        String expiryLabel = DateUtils.getExpiryLabel(i.expiry);
        h.expiry.setText(expiryLabel);

        DateUtils.Status status = DateUtils.getExpiryStatus(i.expiry);
        if (status == DateUtils.Status.EXPIRED) {
            h.expiryBadge.setText("Expired");
            h.expiryBadge.setBackgroundResource(R.drawable.bg_badge_expired);
            h.expiryBadge.setTextColor(ContextCompat.getColor(context, R.color.expired_text));
        } else if (status == DateUtils.Status.EXPIRING_SOON) {
            h.expiryBadge.setText("Expiring Soon");
            h.expiryBadge.setBackgroundResource(R.drawable.bg_badge_warning);
            h.expiryBadge.setTextColor(ContextCompat.getColor(context, R.color.warning_text));
        } else if (status == DateUtils.Status.FRESH) {
            h.expiryBadge.setText("Fresh");
            h.expiryBadge.setBackgroundResource(R.drawable.bg_badge_good);
            h.expiryBadge.setTextColor(ContextCompat.getColor(context, R.color.fresh_text));
        } else {
            h.expiryBadge.setText("No Date");
            h.expiryBadge.setBackgroundResource(R.drawable.bg_chip_unselected);
            h.expiryBadge.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        }

        h.edit.setOnClickListener(v -> listener.edit(i));
        h.del.setOnClickListener(v -> listener.delete(i));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, qty, expiry, expiryBadge;
        View edit, del;

        Holder(View v) {
            super(v);
            name = v.findViewById(R.id.itemName);
            qty = v.findViewById(R.id.itemQuantity);
            expiry = v.findViewById(R.id.itemExpiry);
            expiryBadge = v.findViewById(R.id.tvExpiryBadge);
            edit = v.findViewById(R.id.edit);
            del = v.findViewById(R.id.delete);
        }
    }
}
