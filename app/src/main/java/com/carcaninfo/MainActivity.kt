package com.carcaninfo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carcaninfo.can.CanAdapter
import com.carcaninfo.can.CanAdapterFactory
import com.carcaninfo.dtc.DtcManager
import com.carcaninfo.logging.DataLogger
import com.carcaninfo.model.ConnectionStatus
import com.carcaninfo.model.VehicleData
import com.carcaninfo.settings.SettingsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Main activity displaying vehicle CAN data
 * Now with support for built-in CAN modules, DTC reading, and data logging
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var canAdapter: CanAdapter
    private lateinit var dtcManager: DtcManager
    private lateinit var dataLogger: DataLogger
    private lateinit var settingsManager: SettingsManager
    
    private var updateJob: Job? = null
    private var loggingJob: Job? = null
    private var connectionStatus = ConnectionStatus.DISCONNECTED
    
    // UI Elements
    private lateinit var statusText: TextView
    private lateinit var batteryVoltage: TextView
    private lateinit var speedValue: TextView
    private lateinit var rpmValue: TextView
    private lateinit var coolantValue: TextView
    private lateinit var fuelValue: TextView
    private lateinit var loadValue: TextView
    private lateinit var throttleValue: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Hide system UI for immersive experience
        // Use newer API for Android 11+ (API 30+) with fallback for older versions
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
        
        initializeViews()
        initializeManagers()
        connectToAdapter()
    }
    
    private fun initializeViews() {
        statusText = findViewById(R.id.statusText)
        batteryVoltage = findViewById(R.id.batteryVoltage)
        speedValue = findViewById(R.id.speedValue)
        rpmValue = findViewById(R.id.rpmValue)
        coolantValue = findViewById(R.id.coolantValue)
        fuelValue = findViewById(R.id.fuelValue)
        loadValue = findViewById(R.id.loadValue)
        throttleValue = findViewById(R.id.throttleValue)
    }
    
    private fun initializeManagers() {
        // Initialize settings manager and load settings
        settingsManager = SettingsManager(this)
        val settings = settingsManager.loadSettings()
        
        // Initialize data logger
        dataLogger = DataLogger(this)
        
        // DTC manager will be initialized after CAN adapter connection
    }
    
    private fun connectToAdapter() {
        lifecycleScope.launch {
            updateConnectionStatus(ConnectionStatus.CONNECTING)
            
            val settings = settingsManager.loadSettings()
            
            // Use adapter factory to get the best available adapter
            // This will try built-in CAN first, then OBD adapter, then simulated
            canAdapter = CanAdapterFactory.createAdapter(
                this@MainActivity,
                if (settings.autoConnect) settings.preferredAdapter else null
            )
            
            if (canAdapter.isConnected()) {
                updateConnectionStatus(ConnectionStatus.CONNECTED)
                
                // Initialize DTC manager with connected adapter
                dtcManager = DtcManager(canAdapter)
                
                // Start data updates
                startDataUpdates()
                
                // Start logging if enabled in settings
                if (settings.enableLogging) {
                    startDataLogging()
                }
            } else {
                updateConnectionStatus(ConnectionStatus.ERROR)
            }
        }
    }
    
    private fun startDataUpdates() {
        updateJob?.cancel()
        
        val settings = settingsManager.loadSettings()
        val refreshRate = settings.refreshRate.toLong()
        
        updateJob = lifecycleScope.launch {
            while (isActive && canAdapter.isConnected()) {
                try {
                    val data = canAdapter.readVehicleData()
                    updateUI(data)
                    delay(refreshRate)
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateConnectionStatus(ConnectionStatus.ERROR)
                    break
                }
            }
        }
    }
    
    private fun startDataLogging() {
        loggingJob?.cancel()
        
        val settings = settingsManager.loadSettings()
        val logInterval = settings.logInterval.toLong()
        
        lifecycleScope.launch {
            dataLogger.startLogging()
            
            loggingJob = launch {
                while (isActive && canAdapter.isConnected()) {
                    try {
                        val data = canAdapter.readVehicleData()
                        dataLogger.logData(data)
                        delay(logInterval)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        break
                    }
                }
            }
        }
    }
    
    private fun updateUI(data: VehicleData) {
        runOnUiThread {
            val settings = settingsManager.loadSettings()
            
            // Update speed (convert to mph if imperial)
            val speed = if (settings.useMetric) data.speed else (data.speed * 0.621371).toInt()
            speedValue.text = speed.toString()
            
            // Update RPM
            rpmValue.text = data.rpm.toString()
            
            // Update coolant temperature (convert to Fahrenheit if imperial)
            val coolant = if (settings.useMetric) data.coolantTemp else ((data.coolantTemp * 9/5) + 32)
            coolantValue.text = coolant.toString()
            
            // Update fuel level
            fuelValue.text = data.fuelLevel.toString()
            
            // Update engine load
            loadValue.text = data.engineLoad.toString()
            
            // Update throttle position
            throttleValue.text = data.throttlePosition.toString()
            
            // Update battery voltage
            batteryVoltage.text = String.format("%.1f V", data.batteryVoltage)
        }
    }
    
    private fun updateConnectionStatus(status: ConnectionStatus) {
        connectionStatus = status
        runOnUiThread {
            statusText.text = when (status) {
                ConnectionStatus.DISCONNECTED -> getString(R.string.disconnected)
                ConnectionStatus.CONNECTING -> getString(R.string.connecting)
                ConnectionStatus.CONNECTED -> getString(R.string.connected)
                ConnectionStatus.ERROR -> getString(R.string.error_connection)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            updateJob?.cancel()
            loggingJob?.cancel()
            dataLogger.stopLogging()
            canAdapter.disconnect()
        }
    }
    
    override fun onPause() {
        super.onPause()
        updateJob?.cancel()
    }
    
    override fun onResume() {
        super.onResume()
        if (canAdapter.isConnected()) {
            startDataUpdates()
        }
    }
}
