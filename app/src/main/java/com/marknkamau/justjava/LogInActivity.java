package com.marknkamau.justjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marknkamau.justjava.utils.FirebaseUtil;
import com.marknkamau.justjava.utils.PreferencesInteraction;

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
    @BindView(R.id.tv_forgot_pass)
    TextView tvForgotPass;
    @BindView(R.id.tv_sign_up)
    TextView tvSignUp;

    private String email;
    private FirebaseAuth firebaseAuth;
    private boolean passVisible = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
        }
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
                if (validateFields())
                    signUserIn();
                break;
            case R.id.tv_forgot_pass:
                resetUserPassword();
                break;
            case R.id.tv_sign_up:
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                finish();
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
                            getDefaults(firebaseAuth.getCurrentUser());
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getDefaults(final FirebaseUser user) {
        DatabaseReference database = FirebaseUtil.getDatabase().getReference().child("users/" + user.getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                Toast.makeText(LogInActivity.this, getString(R.string.logged_in_as) + " " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                PreferencesInteraction.setDefaults(getApplicationContext()
                        , dataSnapshot.child("name").getValue().toString()
                        , dataSnapshot.child("phone").getValue().toString()
                        , dataSnapshot.child("defaultAddress").getValue().toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    private void resetUserPassword() {
        disableButtons();
        email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter your email address");
            enableButtons();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    enableButtons();
                    if (task.isSuccessful()) {
                        Toast.makeText(LogInActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
