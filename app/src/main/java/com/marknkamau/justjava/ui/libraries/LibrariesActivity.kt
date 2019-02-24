package com.marknkamau.justjava.ui.libraries

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.Library
import kotlinx.android.synthetic.main.activity_libraries.*

class LibrariesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libraries)

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

        rvLibraries.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvLibraries.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvLibraries.adapter = LibrariesAdapter(libraries) { library ->
            openUrl(library.link)
        }
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}
