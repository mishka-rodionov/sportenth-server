package com.competra.data.util

fun requireEnv(name: String): String =
    System.getenv(name)?.takeIf { it.isNotBlank() }
        ?: error("Environment variable $name is not set. Refusing to start with an insecure default.")
