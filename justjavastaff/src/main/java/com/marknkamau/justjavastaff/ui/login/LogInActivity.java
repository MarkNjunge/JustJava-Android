package com.marknkamau.justjavastaff.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjavastaff.R;
import com.marknkamau.justjavastaff.ui.main.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.img_visibility)
    ImageView imgVisibility;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private boolean passVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(null);
        progressDialog.setMessage("Authenticating");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        enableButtons();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

        Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+");
        Matcher matcher1 = pattern1.matcher(email);

        if (!matcher1.matches()) {
            Toast.makeText(this, "Must be a JustJava employee", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
