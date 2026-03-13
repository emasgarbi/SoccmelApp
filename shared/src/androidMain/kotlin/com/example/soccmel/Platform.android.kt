package com.example.soccmel

import com.russhwolf.multiplatformsettings.Settings
import com.russhwolf.multiplatformsettings.SharedPreferencesSettings
import android.content.Context
import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()

actual fun getDayOfYear(): Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)

actual fun getTimeMillis(): Long = System.currentTimeMillis()

// Useremo un approccio statico o inizializzato per il Context su Android
private var appContext: Context? = null

fun initAndroidContext(context: Context) {
    appContext = context.applicationContext
}

actual fun createSettings(): Settings {
    val context = appContext ?: throw IllegalStateException("Android Context non inizializzato. Chiama initAndroidContext prima.")
    val sharedPrefs = context.getSharedPreferences("soccmel_prefs", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPrefs)
}
