package com.ayoub.gastosgo.utils // O tu paquete: com.ayoub.gastosgo.ui

import java.security.MessageDigest

object Cifrado {
    fun cifrar(texto: String): String {
        val bytes = texto.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        // Convertimos los bytes a texto Hexadecimal
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}