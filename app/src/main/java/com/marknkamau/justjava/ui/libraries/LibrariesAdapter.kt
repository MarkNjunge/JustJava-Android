package com.marknkamau.justjava.ui.libraries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.Library
import kotlinx.android.synthetic.main.item_library.view.*
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LibrariesAdapter(private val data: List<Library>, private val onClick: (Library) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<LibrariesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_library, parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], onClick)
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(library: Library, onClick: (Library) -> Unit) {
            with(itemView) {
                tvName.text = library.name
                tvAuthor.text = library.author
                tvLicense.text = library.licenseText

                rootLayout.setOnClickListener { onClick(library) }
            }
        }
    }
}