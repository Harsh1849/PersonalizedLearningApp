package com.quizapp;

import static com.quizapp.Extensions.ISQUESTION;
import static com.quizapp.Extensions.QUIZDATA;
import static com.quizapp.Extensions.QUIZPOSITION;
import static com.quizapp.Extensions.USERDATA;
import static com.quizapp.Extensions.USERNAME;
import static com.quizapp.Extensions.adjustFullScreen;
import static com.quizapp.Extensions.hideProgress;
import static com.quizapp.Extensions.isConnect;
import static com.quizapp.Extensions.showProgress;
import static com.quizapp.Extensions.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.quizapp.databinding.ActivityHomeBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    String username;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    ArrayList<TaskModel> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(USERDATA, MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME, "");
        getOnBackPressedDispatcher().addCallback(callback);
        setUpView();
    }

    private void setUpView() {
        binding.usernameTextView.setText(username);
        ArrayList<ModelClass> userdata = databaseHelper.getUserData(username);
        if (!userdata.isEmpty()) {
            Glide.with(this).load(userdata.get(0).imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_user).error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(binding.profileImageView);
        }
        binding.generateNewQuizCardView.setOnClickListener(v -> {
            getQuizList();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList = databaseHelper.getAllTasks(username);
        setTaskAdapter();
    }

    private void getQuizList() {
        if (isConnect(this)) {
            showProgress(this);
            Call<QuizModel> call = RetrofitClient.getInstance().getMyApi().getQuizList("Android%20Development");
            call.enqueue(new Callback<QuizModel>() {
                @Override
                public void onResponse(@NonNull Call<QuizModel> call, @NonNull Response<QuizModel> response) {
                    hideProgress();
                    if (response.code() == 200) {
                        TaskModel taskModel = new TaskModel();
                        taskModel.taskTitle = "Generated Task " + (taskList.size() + 1);
                        taskModel.taskDecs = "This is the AI Generated Quiz.";
                        taskModel.userName = username;
                        taskModel.isTaskComplete = 0;
                        taskModel.quizModel = response.body();
                        databaseHelper.insertTaskData(taskModel);

                        taskList = databaseHelper.getAllTasks(username);
                        setTaskAdapter();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QuizModel> call, @NonNull Throwable t) {
                    hideProgress();
                    showToast(HomeActivity.this, t.getMessage());
                }
            });
        }
    }

    private void setTaskAdapter() {

        if (taskList.isEmpty()) {
            binding.taskRecyclerView.setVisibility(View.GONE);
            binding.noDataFound.setVisibility(View.VISIBLE);
        } else {
            binding.taskRecyclerView.setVisibility(View.VISIBLE);
            binding.noDataFound.setVisibility(View.GONE);
        }

        binding.taskRecyclerView.setAdapter(new TaskAdapter(HomeActivity.this, taskList));
        ArrayList<Integer> taskDue = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).isTaskComplete == 0) {
                taskDue.add(i);
            }
        }
        binding.taskDueTextView.setText("You have "+(taskDue.size())+" task due");
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            // Back is pressed... Finishing the activity
            finishAffinity();
        }
    };
}