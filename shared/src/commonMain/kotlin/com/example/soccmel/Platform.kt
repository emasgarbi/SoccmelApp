package com.example.soccmel

import com.russhwolf.settings.Settings

expect fun randomUUID(): String

expect fun createSettings(): Settings

expect fun getDayOfYear(): Int

expect fun getTimeMillis(): Long
