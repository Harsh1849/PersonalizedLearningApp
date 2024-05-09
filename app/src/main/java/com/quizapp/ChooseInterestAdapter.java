package com.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChooseInterestAdapter extends RecyclerView.Adapter<ChooseInterestAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<ChooseInterestModel> interestArray;
    private final OnInterestItemClick onInterestItemClick;
    private int interestCount = 0;

    public ChooseInterestAdapter(Context context, ArrayList<ChooseInterestModel> interestArray, OnInterestItemClick onInterestItemClick) {
        this.context = context;
        this.interestArray = interestArray;
        this.onInterestItemClick = onInterestItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemview_intrest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.interests_textView.setText(interestArray.get(position).getInterestName());

        holder.interestLayout.setOnClickListener(v -> {
            if (interestArray.get(position).isSelected()) {
                holder.interestLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_gredient_choose));
                interestArray.get(position).setSelected(false);
                interestCount--;
            } else {
                holder.interestLayout.setBackgroundColor(context.getColor(R.color.yellow));
                interestArray.get(position).setSelected(true);
                interestCount++;
            }

            onInterestItemClick.onInterestItemClick(interestCount);
        });
    }

    @Override
    public int getItemCount() {
        return interestArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView interests_textView;
        LinearLayout interestLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            interestLayout = itemView.findViewById(R.id.interestLayout);
            interests_textView = itemView.findViewById(R.id.interests_textView);
        }
    }

    public interface OnInterestItemClick {
        void onInterestItemClick(int interestCount);
    }
}
