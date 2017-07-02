package com.marknkamau.justjava.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marknkamau.justjava.JustJavaApp;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.data.PreferencesRepository;
import com.marknkamau.justjava.ui.signup.SignUpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity implements LogInActivityView {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.img_visibility)
    ImageView imgVisibility;
    @BindView(R.id.tv_forgot_pass)
    TextView tvForgotPass;
    @BindView(R.id.tv_sign_up)
    TextView tvSignUp;

    private String email;
    private boolean passVisible = false;
    private ProgressDialog progressDialog;
    private LogInActivityPresenter presenter;

    @Inject
    PreferencesRepository preferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        ((JustJavaApp) getApplication()).getAppComponent().inject(this);
        presenter = new LogInActivityPresenter(this, preferencesRepository);
        presenter.checkSignInStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkSignInStatus();
    }

    @OnClick({R.id.btn_log_in, R.id.img_visibility, R.id.tv_forgot_pass, R.id.tv_sign_up})
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
                signIn();
                break;
            case R.id.tv_forgot_pass:
                resetUserPassword();
                break;
            case R.id.tv_sign_up:
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                break;
        }
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void signIn() {
        if (validateFields()) {
            disableButtons();
            presenter.signIn(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
        }
    }

    @Override
    public void resetUserPassword() {
        disableButtons();
        email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter your email address");
            enableButtons();
        } else {
            presenter.resetUserPassword(email);
        }
    }

    @Override
    public void displayMessage(String message) {
        enableButtons();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(null);
        progressDialog.setMessage("Authenticating");
        progressDialog.show();
    }

    @Override
    public void dismissDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void finishSignUp() {
        finish();
    }

    private void disableButtons() {
        btnLogIn.setEnabled(false);
        tvSignUp.setEnabled(false);
        tvForgotPass.setEnabled(false);
    }

    private void enableButtons() {
        btnLogIn.setEnabled(true);
        tvSignUp.setEnabled(true);
        tvForgotPass.setEnabled(true);
    }

    private boolean validateFields() {
        email = etEmail.getText().toString().trim();
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

}
