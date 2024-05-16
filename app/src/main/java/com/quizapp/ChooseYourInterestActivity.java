package com.quizapp;

import static com.quizapp.Extensions.adjustFullScreen;
import static com.quizapp.Extensions.showToast;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.quizapp.databinding.ActivityChooseYourInterestBinding;

import java.util.ArrayList;

public class ChooseYourInterestActivity extends AppCompatActivity implements ChooseInterestAdapter.OnInterestItemClick {

    private ActivityChooseYourInterestBinding binding;
    private final ArrayList<ChooseInterestModel> interestArray = new ArrayList<>();
    private Integer interestCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChooseYourInterestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        setUpView();
        manageClicks();
    }

    private void setUpView() {
        interestArray.add(new ChooseInterestModel(false, "Algorithms"));
        interestArray.add(new ChooseInterestModel(false, "Data Structures"));
        interestArray.add(new ChooseInterestModel(false, "Web Development"));
        interestArray.add(new ChooseInterestModel(false, "Testing"));
        interestArray.add(new ChooseInterestModel(false, "Front-End Development"));
        interestArray.add(new ChooseInterestModel(false, "Back-End Development"));
        interestArray.add(new ChooseInterestModel(false, "API Development"));
        interestArray.add(new ChooseInterestModel(false, "System Architecture"));
        interestArray.add(new ChooseInterestModel(false, "Network Security"));
        interestArray.add(new ChooseInterestModel(false, "Software Quality Assurance"));
        interestArray.add(new ChooseInterestModel(false, "Blockchain Development"));
        interestArray.add(new ChooseInterestModel(false, "Project Management"));

        binding.interestsRecyclerview.setAdapter(new ChooseInterestAdapter(this, interestArray, this));
    }

    private void manageClicks() {
        binding.nextCardView.setOnClickListener(v -> {
            if (interestCount <= 0) {
                showToast(this, "Please select At least one interest");
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        });
    }

    @Override
    public void onInterestItemClick(int interestCount) {
        this.interestCount = interestCount;
    }
}