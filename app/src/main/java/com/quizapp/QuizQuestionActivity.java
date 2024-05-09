package com.quizapp;

import static com.quizapp.Extensions.ISQUESTION;
import static com.quizapp.Extensions.QUIZDATA;
import static com.quizapp.Extensions.adjustFullScreen;
import static com.quizapp.Extensions.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.quizapp.databinding.ActivityQuizQuestionBinding;

import java.util.ArrayList;

public class QuizQuestionActivity extends AppCompatActivity {
    private ActivityQuizQuestionBinding binding;
    private TaskModel taskModel;
    private Boolean isQuestion;
    DatabaseHelper databaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuizQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        init();
        databaseHelper = new DatabaseHelper(this);
    }

    private void init() {
        if (getIntent() != null) {
            taskModel = new Gson().fromJson(getIntent().getStringExtra(QUIZDATA), TaskModel.class);
            isQuestion = getIntent().getBooleanExtra(ISQUESTION, false);
            Log.e("quizModel", "init: " + taskModel.quizModel.toString());
            setData();

            if (isQuestion) {
                binding.submitCardView.setVisibility(View.VISIBLE);
                binding.continueCardView.setVisibility(View.GONE);
                binding.taskTitleTextView.setVisibility(View.VISIBLE);
                binding.taskDecsTextView.setVisibility(View.VISIBLE);
                binding.yourResultTextView.setVisibility(View.GONE);
            } else {
                binding.submitCardView.setVisibility(View.INVISIBLE);
                binding.continueCardView.setVisibility(View.VISIBLE);
                binding.taskTitleTextView.setVisibility(View.GONE);
                binding.taskDecsTextView.setVisibility(View.GONE);
                binding.yourResultTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setData() {
        if (taskModel.quizModel != null) {
            binding.taskTitleTextView.setText(taskModel.taskTitle);
            binding.taskDecsTextView.setText(taskModel.taskDecs);
            binding.quizRecyclerview.setAdapter(new QuizListAdapter(this, taskModel.quizModel.quiz, isQuestion));

            binding.submitCardView.setOnClickListener(v -> {
                ArrayList<Integer> quizIdArray = new ArrayList<>();
                for (int i = 0; i < taskModel.quizModel.quiz.size(); i++) {
                    for (int j = 0; j < taskModel.quizModel.quiz.get(i).options.size(); j++) {
                        if (taskModel.quizModel.quiz.get(i).options.get(j).isAnswered == 1) {
                            quizIdArray.add(taskModel.quizModel.quiz.get(i).options.get(j).isAnswered);
                        }
                    }
                }

                if (quizIdArray.size() != taskModel.quizModel.quiz.size()) {
                    showToast(QuizQuestionActivity.this, "Please first select all quiz answer.");
                    return;
                }
                showToast(QuizQuestionActivity.this, "Quiz answer submit successfully.");
                if (databaseHelper.updateTaskData(taskModel.id, 1, taskModel.quizModel)) {
                    Intent intent = new Intent(QuizQuestionActivity.this, QuizQuestionActivity.class);
                    intent.putExtra(QUIZDATA, new Gson().toJson(taskModel, TaskModel.class));
                    intent.putExtra(ISQUESTION, false);
                    startActivity(intent);
                    finish();
                }
            });

            binding.continueCardView.setOnClickListener(v -> finish());
        }
    }


}