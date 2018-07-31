package com.marknkamau.justjava.ui.about

import android.support.v7.widget.RecyclerView
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

class LibrariesAdapter(private val onClick: (Library) -> Unit) : RecyclerView.Adapter<LibrariesAdapter.ViewHolder>() {

    private var data: List<Library> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_library, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position], onClick)

    fun setItems(data: List<Library>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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