package com.quizapp;

import static com.quizapp.Extensions.checkEmailPattern;
import static com.quizapp.Extensions.checkEmptyString;
import static com.quizapp.Extensions.clearError;
import static com.quizapp.Extensions.hideKeyboard;
import static com.quizapp.Extensions.showError;
import static com.quizapp.Extensions.showToast;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.quizapp.databinding.ActivityRegisterBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    DatabaseHelper databaseHelper;
    private final Integer REQUEST_CODE = 102;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        adjustFullScreen(binding.getRoot());
        binding.main.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });

        setUpView();
        manageClicks();
        databaseHelper = new DatabaseHelper(this);
    }

    private void setUpView() {
        binding.usernameTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.usernameTil));
        binding.emailTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.emailTil));
        binding.cemailTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.cemailTil));
        binding.passwordTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.passwordTil));
        binding.confirmPasswordTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.confirmPasswordTil));
        binding.phoneNumberTiet.addTextChangedListener(new ClearErrorTextWatcher(binding.phoneNumberTil));
    }

    private void manageClicks() {

        binding.profileCardView.setOnClickListener(v -> setupImagePicker());
        binding.signUpCardView.setOnClickListener(v -> {
            if (!isValid()) return;

            hideKeyboard(this, binding.getRoot());

            String username = Objects.requireNonNull(binding.usernameTiet.getText()).toString();
            String email = Objects.requireNonNull(binding.emailTiet.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordTiet.getText()).toString();
            String confirmPassword = Objects.requireNonNull(binding.confirmPasswordTiet.getText()).toString();
            String phoneNumber = Objects.requireNonNull(binding.phoneNumberTiet.getText()).toString();

            if (databaseHelper.checkUserName(username)) {
                showToast(this, "This username already exists! Please try again using another username.");
                return;
            }

            if (databaseHelper.checkEmail(email)) {
                showToast(this, "This email already exists! Please try again using another email.");
                return;
            }

            if (databaseHelper.checkPhoneNumber(phoneNumber)) {
                showToast(this, "This phone number already exists! Please try again using another phone number.");
                return;
            }

            ModelClass model = new ModelClass(username, email, password, confirmPassword, imageUrl);
            model.setUserName(username);
            model.setEmail(email);
            model.setPassword(password);
            model.setPhoneNumber(phoneNumber);
            model.setImageUrl(imageUrl);

            if (databaseHelper.insertUserData(model)) {
                showToast(this, "User Register Successfully");
                startActivity(new Intent(RegisterActivity.this, ChooseYourInterestActivity.class));
                finish();
            }
        });
    }

    private boolean isValid() {
        if (checkEmptyString(binding.usernameTiet.getText())) {
            showError(binding.usernameTil, getString(R.string.please_enter_username));
            return false;
        } else {
            clearError(binding.usernameTil);
        }
        if (checkEmptyString(binding.emailTiet.getText())) {
            showError(binding.emailTil, getString(R.string.please_enter_email));
            return false;
        } else {
            clearError(binding.emailTil);
        }
        if (checkEmailPattern(binding.emailTiet.getText())) {
            showError(binding.emailTil, getString(R.string.enter_valid_email));
            return false;
        } else {
            clearError(binding.emailTil);
        }
        if (checkEmptyString(binding.cemailTiet.getText())) {
            showError(binding.cemailTil, getString(R.string.please_enter_confirm_email));
            return false;
        } else {
            clearError(binding.cemailTil);
        }
        if (!binding.emailTiet.getText().toString().matches(binding.cemailTiet.getText().toString().trim())) {
            showError(binding.cemailTil, getString(R.string.email_not_match));
            return false;
        } else {
            clearError(binding.cemailTil);
        }
        if (checkEmptyString(binding.passwordTiet.getText())) {
            showError(binding.passwordTil, getString(R.string.please_enter_password));
            return false;
        } else {
            clearError(binding.passwordTil);
        }
        if (binding.passwordTiet.getText().length() < 8) {
            showError(binding.passwordTil, getString(R.string.enter_valid_password));
            return false;
        } else {
            clearError(binding.passwordTil);
        }
        if (checkEmptyString(binding.confirmPasswordTiet.getText())) {
            showError(binding.confirmPasswordTil, getString(R.string.please_enter_confirm_password));
            return false;
        } else {
            clearError(binding.confirmPasswordTil);
        }
        if (!binding.passwordTiet.getText().toString().matches(binding.confirmPasswordTiet.getText().toString().trim())) {
            showError(binding.confirmPasswordTil, getString(R.string.password_not_match));
            return false;
        } else {
            clearError(binding.confirmPasswordTil);
        }
        if (checkEmptyString(binding.phoneNumberTiet.getText())) {
            showError(binding.phoneNumberTil, getString(R.string.enter_number));
            return false;
        } else {
            clearError(binding.phoneNumberTil);
        }
        if (binding.phoneNumberTiet.getText().length() < 6) {
            showError(binding.phoneNumberTil, getString(R.string.enter_valid_number));
            return false;
        } else {
            clearError(binding.phoneNumberTil);
        }
//        if (checkEmptyString(imageUrl)) {
//            showToast(this, "Please select profile photo.");
//            return false;
//        }
        return true;
    }

    private void setupImagePicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }
        } else {
//            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "File"), REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast(this, getString(R.string.accept_permissions));
//                    requestPermissions(
//                            this,
//                            getString(R.string.APP_NAME),
//                            getString(R.string.PROVIDE_PERMISSIONS),
//                            getString(R.string.CANCEL),
//                            getString(R.string.SETTINGS)
//                    );
            } else {
                openGallery();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "onActivityResult resultCode>>: " + resultCode + " , resultCode>>>> " + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        File imageFile = getFileFromUri(getFileExtension(this, Uri.parse(uri.toString())), getApplicationContext().getContentResolver(), uri, getApplicationContext().getCacheDir());
                        Log.d("CheckFile >>>>", "onActivityResult: " + imageFile);
                        Log.d("CheckFile 1234>>>>", "onActivityResult: " + imageFile.getAbsolutePath());
                        if (imageFile.exists()) {
                            imageUrl = imageFile.toString();
                            Glide.with(RegisterActivity.this).load(imageFile.toString()).apply(new RequestOptions().placeholder(R.drawable.ic_user).error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(binding.profileImageView);
                        }
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }
            }
        }
    }

    private File getFileFromUri(String fileExtension, ContentResolver contentResolver, Uri uri, File directory) throws IOException {
        File file = File.createTempFile("file_", "." + fileExtension, directory);
        InputStream inputStream = contentResolver.openInputStream(uri);
        if (inputStream != null) {
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        }
        return file;
    }

    public static String getFileExtension(Context context, Uri uri) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        String mimeType = cR.getType(uri);
        if (mimeType != null) {
            return mime.getExtensionFromMimeType(mimeType);
        }
        return null;
    }
}