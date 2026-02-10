package com.ayoub.gastosgo.utils

import java.security.MessageDigest

object Cifrado {
    fun hashSHA256(texto: String): String {
        val bytes = texto.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}