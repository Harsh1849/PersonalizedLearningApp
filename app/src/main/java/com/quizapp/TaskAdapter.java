package com.quizapp;

import static com.quizapp.Extensions.ISQUESTION;
import static com.quizapp.Extensions.QUIZDATA;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<TaskModel> taskArray;

    public TaskAdapter(Context context, ArrayList<TaskModel> taskArray) {
        this.context = context;
        this.taskArray = taskArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemview_tasks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.taskTitleTextView.setText(taskArray.get(position).taskTitle);
        holder.taskDecsTextView.setText(taskArray.get(position).taskDecs);
        holder.startTaskCardView.setOnClickListener(v -> {
            boolean isQuestion;
            isQuestion = taskArray.get(position).isTaskComplete != 1;
            Intent intent = new Intent(context, QuizQuestionActivity.class);
            intent.putExtra(QUIZDATA, new Gson().toJson(taskArray.get(position), TaskModel.class));
            intent.putExtra(ISQUESTION, isQuestion);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskArray.size();
//        return 3;
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
        MaterialCardView startTaskCardView;
        TextView taskTitleTextView, taskDecsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.task_title_textView);
            taskDecsTextView = itemView.findViewById(R.id.task_decs_textView);
            startTaskCardView = itemView.findViewById(R.id.start_task_cardView);
        }
    }
}
