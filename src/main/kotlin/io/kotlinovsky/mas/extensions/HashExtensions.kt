package io.kotlinovsky.mas.extensions

import java.security.MessageDigest
import java.util.Base64

fun String.hashMd5(): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    
    return Base64
        .getEncoder()
        .encodeToString(messageDigest.digest(toByteArray()))
}