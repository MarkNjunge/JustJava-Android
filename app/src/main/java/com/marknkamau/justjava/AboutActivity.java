package com.marknkamau.justjava;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_version_number)
    TextView tvVersionNumber;
    @BindView(R.id.img_mail)
    ImageView imgMail;
    @BindView(R.id.img_linkedin)
    ImageView imgLinkedin;
    @BindView(R.id.img_github)
    ImageView imgGithub;
    @BindView(R.id.img_butter_knife)
    ImageView imgButterKnife;
    @BindView(R.id.img_sliding_panel)
    ImageView imgSlidingPanel;
    @BindView(R.id.img_picasso)
    ImageView imgPicasso;
    @BindView(R.id.img_realm)
    ImageView imgRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersionNumber.setText("v " + pInfo.versionName);
            if (BuildConfig.DEBUG)
                tvVersionNumber.append(" (debug)");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.img_back, R.id.img_mail, R.id.img_linkedin, R.id.img_github, R.id.img_butter_knife, R.id.img_sliding_panel, R.id.img_picasso, R.id.img_realm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_mail:
                sendEmail();
                break;
            case R.id.img_linkedin:
                openLinkedInProfile();
                break;
            case R.id.img_github:
                openGitHubProfile();
                break;
            case R.id.img_butter_knife:
                butterKnife();
                break;
            case R.id.img_sliding_panel:
                slidingLibrary();
                break;
            case R.id.img_picasso:
                openPicasso();
                break;
            case R.id.img_realm:
                openRealm();
                break;
        }
    }

    private void openRealm() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://realm.io/products/realm-mobile-database/"));
        startActivity(intent);
    }

    private void openPicasso() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://square.github.io/picasso/"));
        startActivity(intent);
    }

    private void openLinkedInProfile() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://linkedin.com/in/marknkamau"));
        Toast.makeText(this, "linkedin.com/in/marknkamau", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    private void openGitHubProfile() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MarkNKamau"));
        startActivity(intent);
    }

    private void sendEmail() {
        String[] addresses = {"mark.kamau@outlook.com"}; //Has to be String array or it will ignore
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        startActivity(intent);
    }

    private void slidingLibrary() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/umano/AndroidSlidingUpPanel"));
        startActivity(intent);
    }

    private void butterKnife() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jakewharton.github.io/butterknife/"));
        startActivity(intent);
    }

    @OnClick()
    public void onViewClicked() {
    }
}
