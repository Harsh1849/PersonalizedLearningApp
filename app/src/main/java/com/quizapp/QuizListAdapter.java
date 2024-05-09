package com.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Quiz> quizArray;
    private final Boolean isQuestion;

    public QuizListAdapter(Context context, ArrayList<Quiz> quizArray, Boolean isQuestion) {
        this.context = context;
        this.quizArray = quizArray;
        this.isQuestion = isQuestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemview_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionNumberTextView.setText((position + 1) + ". Question " + (position + 1));
        holder.questionTextView.setText(quizArray.get(position).question);
        holder.optionsRecyclerView.setAdapter(new OptionsAdapter(context, quizArray.get(position).options, isQuestion));

    }

    @Override
    public int getItemCount() {
        return quizArray.size();
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
        TextView questionNumberTextView, questionTextView;
        RecyclerView optionsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.question_number_textView);
            questionTextView = itemView.findViewById(R.id.question_textView);
            optionsRecyclerView = itemView.findViewById(R.id.options_recyclerView);
        }
    }
}