package com.marknkamau.justjava.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBackAbout.setOnClickListener { finish() }
        val appVersion = "v${BuildConfig.VERSION_NAME} ${if (BuildConfig.BUILD_TYPE == "debug") "(debug)" else ""}"
        binding.tvAppVersionAbout.text = appVersion
        binding.tvSourceCodeAbout.setOnClickListener { openUrl("https://github.com/MarkNjunge/JustJava-Android") }
        binding.imgEmailAbout.setOnClickListener { sendEmail() }
        binding.imgLinkedInAbout.setOnClickListener { openUrl("https://linkedin.com/in/marknjunge") }
        binding.imgWebsiteAbout.setOnClickListener { openUrl("https://marknjunge.com") }
        binding.imgGithubAbout.setOnClickListener { openUrl("https://github.com/MarkNjunge") }
        binding.tvPrivacyPolicyAbout.setOnClickListener { openUrl("https://justjava.store/privacy") }

        // See https://github.com/google/play-services-plugins/pull/62
        binding.tvLicensesAbout.visibility = View.GONE
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    private fun sendEmail() {
        val addresses = arrayOf("contact@marknjunge.com") // Has to be String array or it will ignore
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        startActivity(intent)
    }
}
