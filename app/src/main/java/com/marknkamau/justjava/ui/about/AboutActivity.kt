package com.marknkamau.justjava.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.Library
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvVersion.text = "v${BuildConfig.VERSION_NAME}"

        val libraries = mutableListOf(
                Library("Retrofit", "Square", Library.APACHE2, "http://square.github.io/retrofit/"),
                Library("Gson", "Google", Library.APACHE2, "https://github.com/google/gson"),
                Library("RxJava", "ReactiveX", Library.APACHE2, "https://github.com/ReactiveX/RxJava"),
                Library("RxAndroid", "ReactiveX", Library.APACHE2, "https://github.com/ReactiveX/RxAndroid"),
                Library("RxKotlin", "ReactiveX", Library.APACHE2, "https://github.com/ReactiveX/RxKotlin"),
                Library("Picasso", "Square", Library.APACHE2, "http://square.github.io/picasso/"),
                Library("Timber", "Jake Wharton", Library.APACHE2, "https://github.com/JakeWharton/timber"),
                Library("Mockito", "Mockito", Library.MIT, "https://github.com/mockito/mockito"),
                Library("Mockito-Kotlin", "Niek Haarman", Library.MIT, "https://github.com/nhaarman/mockito-kotlin")
        ).sortedBy { it.name }

        rvLibraries.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvLibraries.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayout.VERTICAL))
        val librariesAdapter = LibrariesAdapter { library ->
            openUrl(library.link)
        }
        rvLibraries.adapter = librariesAdapter
        librariesAdapter.setItems(libraries)

        tvSource.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        imgMail.setOnClickListener(this)
        imgLinkedin.setOnClickListener(this)
        imgGithub.setOnClickListener(this)
        imgWebsite.setOnClickListener(this)
        tvPrivacyPolicy.setOnClickListener(this)

        scrollView.post{
            Runnable {
                scrollView.scrollTo(0, scrollView.top)
            }.run()
        }
    }

    override fun onClick(view: View) {
        when (view) {
            tvSource -> openUrl("https://github.com/MarkNjunge/JustJava-Android")
            imgBack -> finish()
            imgMail -> sendEmail()
            imgLinkedin -> openUrl("https://linkedin.com/in/marknjunge")
            imgGithub -> openUrl("https://github.com/MarkNjunge")
            imgWebsite -> openUrl("https://marknjunge.com")
            tvPrivacyPolicy -> openUrl("https://marknjunge.com/projects/justjava/privacy-policy")
        }
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))


    private fun sendEmail() {
        val addresses = arrayOf("mark.kamau@outlook.com") //Has to be String array or it will ignore
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        startActivity(intent)
    }

}
