package com.marknkamau.justjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.marknkamau.justjava.authentication.login.LogInActivity;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.PreferencesInteraction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_email_address)
    EditText etEmailAddress;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.img_view_pass)
    ImageView imgViewPass;
    @BindView(R.id.et_password_repeat)
    EditText etPasswordRepeat;
    @BindView(R.id.img_view_pass_rpt)
    ImageView imgViewPassRpt;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_delivery_address)
    MultiAutoCompleteTextView etDeliveryAddress;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.tv_log_in)
    TextView tvLogIn;

    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
    private FirebaseAuth firebaseAuth;
    private boolean passVisible = false;
    private ProgressDialog progressDialog;
    private boolean passRptVisible = false;

    public static final String DEF_NAME = "defaultName";
    public static final String DEF_PHONE = "defaultPhoneNumber";
    public static final String DEF_ADDRESS = "defaultDeliveryAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.img_view_pass, R.id.img_view_pass_rpt, R.id.btn_sign_up, R.id.tv_log_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_view_pass:
                if (passVisible) {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    imgViewPass.setImageResource(R.drawable.ic_visibility_off);
                    passVisible = false;
                } else {
                    etPassword.setTransformationMethod(null);
                    imgViewPass.setImageResource(R.drawable.ic_visibility);
                    passVisible = true;
                }
                break;
            case R.id.img_view_pass_rpt:
                if (passRptVisible) {
                    etPasswordRepeat.setTransformationMethod(new PasswordTransformationMethod());
                    imgViewPassRpt.setImageResource(R.drawable.ic_visibility_off);
                    passRptVisible = false;
                } else {
                    etPasswordRepeat.setTransformationMethod(null);
                    imgViewPassRpt.setImageResource(R.drawable.ic_visibility);
                    passRptVisible = true;
                }
                break;
            case R.id.btn_sign_up:
                if (fieldsOk()) {
                    disableButtons();
                    createUser();
                }
                break;
            case R.id.tv_log_in:
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                finish();
                break;

        }
    }

    private void enableButtons() {
        btnSignUp.setEnabled(true);
    }

    private void disableButtons() {
        btnSignUp.setEnabled(false);
    }

    private void createUser() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(null);
        progressDialog.setMessage("Creating user");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    signUserIn();
                } else {
                    enableButtons();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUserIn() {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addUserToDatabase();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserToDatabase() {
        PreferencesInteraction.setDefaults(this, name, phone, address);

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference databaseReference = FirebaseDBUtil.getDatabase().getReference().child("users").child(user.getUid());
                    databaseReference.child("name").setValue(name);
                    databaseReference.child("email").setValue(email);
                    databaseReference.child("phone").setValue(phone);
                    databaseReference.child("defaultAddress").setValue(address);

                    Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 100);
                } else {
                    enableButtons();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean fieldsOk() {
        email = etEmailAddress.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String passwordRpt = etPasswordRepeat.getText().toString().trim();

        name = etName.getText().toString().trim();
        phone = etPhoneNumber.getText().toString().trim();
        address = etDeliveryAddress.getText().toString().trim();

        Boolean bool = true;
        Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+");
        Matcher matcher1 = pattern1.matcher(email);

        if (matcher1.matches()) {
            etEmailAddress.setError("Can not use @justjava.com");
            bool = false;
        }
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordRpt)
                || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailAddress.setError("Incorrect format");
            bool = false;
        }
        if (password.length() < 6) {
            etPassword.setError("At least 6 characters");
            bool = false;
        }
        if (!TextUtils.equals(password, passwordRpt)) {
            etPasswordRepeat.setError("Passwords do no match");
            bool = false;
        }
        return bool;
    }
}
