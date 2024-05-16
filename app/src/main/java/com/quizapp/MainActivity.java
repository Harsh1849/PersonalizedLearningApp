package com.quizapp;

import static com.quizapp.Extensions.USERDATA;
import static com.quizapp.Extensions.USERNAME;
import static com.quizapp.Extensions.adjustFullScreen;
import static com.quizapp.Extensions.checkEmptyString;
import static com.quizapp.Extensions.clearError;
import static com.quizapp.Extensions.hideKeyboard;
import static com.quizapp.Extensions.showError;
import static com.quizapp.Extensions.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.quizapp.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        manageClicks();
        setUpView();
        databaseHelper = new DatabaseHelper(this);

        getOnBackPressedDispatcher().addCallback(callback);
    }

    private void setUpView() {
        binding.usernameTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.usernameTil));
        binding.passwordTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.passwordTil));

    }

    private void manageClicks() {
        binding.signInCardView.setOnClickListener(v -> {
            if (isValid()) {
                hideKeyboard(this, binding.getRoot());
                boolean checkData = databaseHelper.checkLogIn(Objects.requireNonNull(binding.usernameTiet.getText()).toString(), Objects.requireNonNull(binding.passwordTiet.getText()).toString());
                if (checkData) {
                    showToast(this, "Login Successfully");
                    sharedPreferences = getSharedPreferences(USERDATA, MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(USERNAME, binding.usernameTiet.getText().toString());
                    editor.commit();
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    showToast(this, "Invalid Username or Password!");
                }
            }
        });

        binding.signUpCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }

    private boolean isValid() {
        if (checkEmptyString(binding.usernameTiet.getText())) {
            showError(binding.usernameTil, getString(R.string.please_enter_username));
            return false;
        } else {
            clearError(binding.usernameTil);
        }
        if (checkEmptyString(binding.passwordTiet.getText())) {
            showError(binding.passwordTil, getString(R.string.please_enter_password));
            return false;
        } else {
            clearError(binding.passwordTil);
        }
        return true;
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            // Back is pressed... Finishing the activity
            finishAffinity();
        }
    };
}