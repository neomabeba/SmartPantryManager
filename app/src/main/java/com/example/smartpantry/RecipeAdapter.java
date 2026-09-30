package com.example.smartpantry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {

    private List<Recipe> originalList;
    private List<Recipe> filteredList;

    public RecipeAdapter(List<Recipe> items) {
        this.originalList = items != null ? items : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.originalList);
    }

    public void setItems(List<Recipe> x) {
        this.originalList = x != null ? x : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.originalList);
        notifyDataSetChanged();
    }

    public void filter(String query, String filterType) {
        filteredList.clear();
        String q = query == null ? "" : query.toLowerCase(Locale.US).trim();

        for (Recipe r : originalList) {
            boolean matchesName = r.name.toLowerCase(Locale.US).contains(q);
            if (!matchesName) {
                // Check if any ingredient matches query
                for (RecipeIngredient ri : r.ingredients) {
                    if (ri.name.toLowerCase(Locale.US).contains(q)) {
                        matchesName = true;
                        break;
                    }
                }
            }

            boolean matchesType = true;
            if ("COOKABLE".equalsIgnoreCase(filterType)) {
                matchesType = r.isCookable;
            } else if ("PARTIAL".equalsIgnoreCase(filterType)) {
                matchesType = !r.isCookable;
            }

            if (matchesName && matchesType) {
                filteredList.add(r);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_recipe, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Recipe r = filteredList.get(pos);
        Context context = h.itemView.getContext();

        h.name.setText(r.name);

        int totalIng = r.ingredients.size();
        int matchedIng = totalIng - r.missingCount;
        h.info.setText(matchedIng + " of " + totalIng + " ingredients available");

        if (r.isCookable) {
            h.matchBadge.setText("Ready to cook");
            h.matchBadge.setBackgroundResource(R.drawable.bg_badge_good);
            h.matchBadge.setTextColor(ContextCompat.getColor(context, R.color.fresh_text));
            h.missingPreview.setVisibility(View.GONE);
        } else {
            h.matchBadge.setText("Missing " + r.missingCount + (r.missingCount == 1 ? " item" : " items"));
            h.matchBadge.setBackgroundResource(R.drawable.bg_badge_warning);
            h.matchBadge.setTextColor(ContextCompat.getColor(context, R.color.warning_text));

            if (!r.missingIngredients.isEmpty()) {
                h.missingPreview.setVisibility(View.VISIBLE);
                h.missingPreview.setText("Missing: " + r.missingIngredients.get(0));
            } else {
                h.missingPreview.setVisibility(View.GONE);
            }
        }

        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipeId", r.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, info, matchBadge, missingPreview;

        Holder(View v) {
            super(v);
            name = v.findViewById(R.id.recipeName);
            info = v.findViewById(R.id.recipeInfo);
            matchBadge = v.findViewById(R.id.tvMatchBadge);
            missingPreview = v.findViewById(R.id.tvMissingPreview);
        }
    }
}
