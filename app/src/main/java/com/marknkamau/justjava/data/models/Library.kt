package com.marknkamau.justjava.data.models

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Library(val name: String, val author: String, val license: Int, val link: String) {
    companion object {
        const val MIT = 1
        const val APACHE2 = 2
    }

    val licenseText: String
        get() {
            return  when(license){
                MIT -> "MIT"
                APACHE2 -> "Apache-2.0"
                else -> "Unknown"
            }
        }

}