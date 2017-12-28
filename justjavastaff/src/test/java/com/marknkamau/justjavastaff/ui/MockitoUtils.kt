package com.marknkamau.justjavastaff.ui

import com.marknkamau.justjavastaff.authentication.AuthenticationService
import org.mockito.Mockito

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

fun anyAuthActionListener(): AuthenticationService.AuthListener? = Mockito.any(AuthenticationService.AuthListener::class.java)