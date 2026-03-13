package com.example.soccmel

import com.russhwolf.multiplatformsettings.Settings
import com.russhwolf.multiplatformsettings.NSUserDefaultsSettings
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.timeIntervalSince1970

actual fun randomUUID(): String = NSUUID().UUIDString()

actual fun getDayOfYear(): Int {
    val date = NSDate()
    val calendar = NSCalendar.currentCalendar
    return calendar.ordinalityOfUnit(NSCalendarUnitDay, inUnit = platform.Foundation.NSCalendarUnitYear, forDate = date).toInt()
}

actual fun getTimeMillis(): Long {
    return (NSDate().timeIntervalSince1970 * 1000).toLong()
}

actual fun createSettings(): Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}
