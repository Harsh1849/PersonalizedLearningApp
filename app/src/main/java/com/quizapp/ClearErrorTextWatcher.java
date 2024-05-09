package com.quizapp;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class ClearErrorTextWatcher implements TextWatcher {
    private final TextInputLayout inputLayout;

    ClearErrorTextWatcher(TextInputLayout inputLayout) {
        this.inputLayout = inputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Clear error when user starts typing
        inputLayout.setErrorEnabled(false);
        inputLayout.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Not needed
    }
}
