package com.carcaninfo.settings

import com.carcaninfo.can.CanAdapterFactory

/**
 * Application settings data model
 */
data class AppSettings(
    // Unit preferences
    val useMetric: Boolean = true,              // true = metric (km/h, °C), false = imperial (mph, °F)
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val speedUnit: SpeedUnit = SpeedUnit.KMH,
    
    // Display preferences
    val refreshRate: Int = 200,                 // Refresh interval in milliseconds (100-1000ms)
    val gaugeStyle: GaugeStyle = GaugeStyle.DIGITAL,
    val darkTheme: Boolean = true,
    val autoNightMode: Boolean = false,         // Auto switch to night mode
    
    // Adapter preferences
    val preferredAdapter: CanAdapterFactory.AdapterType = CanAdapterFactory.AdapterType.BUILT_IN,
    val autoConnect: Boolean = true,
    
    // Data logging
    val enableLogging: Boolean = false,
    val logInterval: Int = 1000,               // Log interval in milliseconds
    val maxLogSize: Long = 100 * 1024 * 1024, // 100 MB
    
    // Advanced
    val showDebugInfo: Boolean = false,
    val customCanIds: Boolean = false
)

enum class TemperatureUnit(val symbol: String) {
    CELSIUS("°C"),
    FAHRENHEIT("°F")
}

enum class SpeedUnit(val symbol: String) {
    KMH("km/h"),
    MPH("mph")
}

enum class GaugeStyle {
    DIGITAL,     // Large numeric display (current)
    ANALOG,      // Circular gauge
    BAR,         // Horizontal bar graph
    MIXED        // Combination of styles
}
