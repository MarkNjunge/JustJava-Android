package com.marknkamau.justjava.ui.about

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.R

import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val pInfo: PackageInfo
        try {
            pInfo = packageManager.getPackageInfo(packageName, 0)
            tvVersionNumber.text = "v " + pInfo.versionName
            if (BuildConfig.DEBUG)
                tvVersionNumber.append(" (debug)")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        imgBack.setOnClickListener(this)
        imgMail.setOnClickListener(this)
        imgLinkedin.setOnClickListener(this)
        imgGithub.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            imgBack -> finish()
            imgMail -> sendEmail()
            imgLinkedin -> openLinkedInProfile()
            imgGithub -> openGitHubProfile()
        }
    }

    private fun openLinkedInProfile() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://linkedin.com/in/marknkamau"))
        Toast.makeText(this, "linkedin.com/in/marknkamau", Toast.LENGTH_LONG).show()
        startActivity(intent)
    }

    private fun openGitHubProfile() = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MarkNjunge")))


    private fun sendEmail() {
        val addresses = arrayOf("mark.kamau@outlook.com") //Has to be String array or it will ignore
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        startActivity(intent)
    }

}
