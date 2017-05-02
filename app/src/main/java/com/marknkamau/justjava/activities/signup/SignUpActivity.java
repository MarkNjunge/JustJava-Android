package com.marknkamau.justjava.activities.signup;

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
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.activities.login.LogInActivity;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.PreferencesInteraction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SignUpActivity extends AppCompatActivity implements SignUpActivityView {

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
    private boolean passVisible = false;
    private ProgressDialog progressDialog;
    private boolean passRptVisible = false;

    public static final String DEF_NAME = "defaultName";
    public static final String DEF_PHONE = "defaultPhoneNumber";
    public static final String DEF_ADDRESS = "defaultDeliveryAddress";

    SignUpActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        presenter = new SignUpActivityPresenter(this);

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
                createUser();
                break;
            case R.id.tv_log_in:
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                finish();
                break;

        }
    }

    @Override
    public void enableUserInteraction() {
        btnSignUp.setBackgroundResource(R.drawable.large_button);
        btnSignUp.setEnabled(true);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void disableUserInteraction() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(null);
        progressDialog.setMessage("Creating user");
        progressDialog.show();
        btnSignUp.setBackgroundResource(R.drawable.large_button_disabled);
        btnSignUp.setEnabled(false);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        PreferencesInteraction.setDefaults(this, name, phone, address);
        finish();
    }


    private void createUser() {
        if (fieldsOk()) {
            presenter.createUser(email, password, name, phone, address);
        }
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
