package com.marknjunge.core.utils

import java.io.IOException

// Has to be IO exception. See https://stackoverflow.com/a/47058587
class NoInternetException : IOException("Check your internet connection")
