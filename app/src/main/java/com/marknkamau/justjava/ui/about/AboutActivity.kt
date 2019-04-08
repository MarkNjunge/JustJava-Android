package com.marknkamau.justjava.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.libraries.LibrariesActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvVersion.text = "v${BuildConfig.VERSION_NAME}"

        tvSource.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        imgMail.setOnClickListener(this)
        imgLinkedin.setOnClickListener(this)
        imgGithub.setOnClickListener(this)
        imgWebsite.setOnClickListener(this)
        tvPrivacyPolicy.setOnClickListener(this)
        tvLibraries.setOnClickListener { startActivity(Intent(this, LibrariesActivity::class.java)) }
    }

    override fun onClick(view: View) {
        when (view) {
            tvSource -> openUrl("https://github.com/MarkNjunge/JustJava-Android")
            imgBack -> finish()
            imgMail -> sendEmail()
            imgLinkedin -> openUrl("https://linkedin.com/in/marknjunge")
            imgGithub -> openUrl("https://github.com/MarkNjunge")
            imgWebsite -> openUrl("https://marknjunge.com")
            tvPrivacyPolicy -> openUrl("https://marknjunge.com/justjava/privacy")
        }
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    private fun sendEmail() {
        val addresses = arrayOf("contact@marknjunge.com") //Has to be String array or it will ignore
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        startActivity(intent)
    }

}
