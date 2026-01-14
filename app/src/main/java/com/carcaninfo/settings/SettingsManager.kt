package com.carcaninfo.settings

import android.content.Context
import android.content.SharedPreferences
import com.carcaninfo.can.CanAdapterFactory

/**
 * Manager for persisting and retrieving app settings
 */
class SettingsManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "CarCanInfoPrefs"
        
        // Keys
        private const val KEY_USE_METRIC = "use_metric"
        private const val KEY_TEMP_UNIT = "temp_unit"
        private const val KEY_SPEED_UNIT = "speed_unit"
        private const val KEY_REFRESH_RATE = "refresh_rate"
        private const val KEY_GAUGE_STYLE = "gauge_style"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_AUTO_NIGHT_MODE = "auto_night_mode"
        private const val KEY_PREFERRED_ADAPTER = "preferred_adapter"
        private const val KEY_AUTO_CONNECT = "auto_connect"
        private const val KEY_ENABLE_LOGGING = "enable_logging"
        private const val KEY_LOG_INTERVAL = "log_interval"
        private const val KEY_MAX_LOG_SIZE = "max_log_size"
        private const val KEY_SHOW_DEBUG = "show_debug"
        private const val KEY_CUSTOM_CAN_IDS = "custom_can_ids"
    }
    
    /**
     * Load settings from SharedPreferences
     */
    fun loadSettings(): AppSettings {
        return AppSettings(
            useMetric = prefs.getBoolean(KEY_USE_METRIC, true),
            temperatureUnit = TemperatureUnit.valueOf(
                prefs.getString(KEY_TEMP_UNIT, TemperatureUnit.CELSIUS.name) ?: TemperatureUnit.CELSIUS.name
            ),
            speedUnit = SpeedUnit.valueOf(
                prefs.getString(KEY_SPEED_UNIT, SpeedUnit.KMH.name) ?: SpeedUnit.KMH.name
            ),
            refreshRate = prefs.getInt(KEY_REFRESH_RATE, 200),
            gaugeStyle = GaugeStyle.valueOf(
                prefs.getString(KEY_GAUGE_STYLE, GaugeStyle.DIGITAL.name) ?: GaugeStyle.DIGITAL.name
            ),
            darkTheme = prefs.getBoolean(KEY_DARK_THEME, true),
            autoNightMode = prefs.getBoolean(KEY_AUTO_NIGHT_MODE, false),
            preferredAdapter = CanAdapterFactory.AdapterType.valueOf(
                prefs.getString(KEY_PREFERRED_ADAPTER, CanAdapterFactory.AdapterType.BUILT_IN.name) 
                    ?: CanAdapterFactory.AdapterType.BUILT_IN.name
            ),
            autoConnect = prefs.getBoolean(KEY_AUTO_CONNECT, true),
            enableLogging = prefs.getBoolean(KEY_ENABLE_LOGGING, false),
            logInterval = prefs.getInt(KEY_LOG_INTERVAL, 1000),
            maxLogSize = prefs.getLong(KEY_MAX_LOG_SIZE, 100 * 1024 * 1024),
            showDebugInfo = prefs.getBoolean(KEY_SHOW_DEBUG, false),
            customCanIds = prefs.getBoolean(KEY_CUSTOM_CAN_IDS, false)
        )
    }
    
    /**
     * Save settings to SharedPreferences
     */
    fun saveSettings(settings: AppSettings) {
        prefs.edit().apply {
            putBoolean(KEY_USE_METRIC, settings.useMetric)
            putString(KEY_TEMP_UNIT, settings.temperatureUnit.name)
            putString(KEY_SPEED_UNIT, settings.speedUnit.name)
            putInt(KEY_REFRESH_RATE, settings.refreshRate)
            putString(KEY_GAUGE_STYLE, settings.gaugeStyle.name)
            putBoolean(KEY_DARK_THEME, settings.darkTheme)
            putBoolean(KEY_AUTO_NIGHT_MODE, settings.autoNightMode)
            putString(KEY_PREFERRED_ADAPTER, settings.preferredAdapter.name)
            putBoolean(KEY_AUTO_CONNECT, settings.autoConnect)
            putBoolean(KEY_ENABLE_LOGGING, settings.enableLogging)
            putInt(KEY_LOG_INTERVAL, settings.logInterval)
            putLong(KEY_MAX_LOG_SIZE, settings.maxLogSize)
            putBoolean(KEY_SHOW_DEBUG, settings.showDebugInfo)
            putBoolean(KEY_CUSTOM_CAN_IDS, settings.customCanIds)
            apply()
        }
    }
    
    /**
     * Reset all settings to defaults
     */
    fun resetToDefaults() {
        prefs.edit().clear().apply()
    }
}
