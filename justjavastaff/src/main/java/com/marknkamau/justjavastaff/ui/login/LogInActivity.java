package com.marknkamau.justjavastaff.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marknkamau.justjavastaff.JustJavaStaffApp;
import com.marknkamau.justjavastaff.R;
import com.marknkamau.justjavastaff.authentication.AuthenticationService;
import com.marknkamau.justjavastaff.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity implements LoginView {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.img_visibility)
    ImageView imgVisibility;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    private boolean passVisible = false;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        AuthenticationService auth = ((JustJavaStaffApp) getApplication()).getAuth();
        presenter = new LoginPresenter(auth, this);

        if (auth.currentEmployee() != null) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }
    }

    @OnClick({R.id.btn_log_in, R.id.img_visibility})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_visibility:
                if (passVisible) {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    imgVisibility.setImageResource(R.drawable.ic_visibility_off);
                    passVisible = false;
                } else {
                    imgVisibility.setImageResource(R.drawable.ic_visibility);
                    etPassword.setTransformationMethod(null);
                    passVisible = true;
                }
                break;
            case R.id.btn_log_in:
                if (validateFields())
                    signUserIn();
                break;
        }
    }

    private void signUserIn() {
        disableButtons();
        pbLoading.setVisibility(View.VISIBLE);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        presenter.signIn(email, password);
    }

    private void disableButtons() {
        btnLogIn.setEnabled(false);
    }

    private void enableButtons() {
        btnLogIn.setEnabled(true);
    }

    private boolean validateFields() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            return false;
        }

        return true;
    }

    @Override
    public void displayMessage(String message) {
        enableButtons();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignedIn() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }
}
