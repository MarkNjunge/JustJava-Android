package com.marknkamau.justjava

import com.marknkamau.justjava.authentication.AuthenticationService
import org.mockito.Mockito

fun anyAuthActionListener(): AuthenticationService.AuthActionListener? = Mockito.any(AuthenticationService.AuthActionListener::class.java)

