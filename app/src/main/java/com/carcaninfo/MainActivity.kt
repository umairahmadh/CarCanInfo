package com.carcaninfo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carcaninfo.can.CanAdapter
import com.carcaninfo.can.SimulatedCanAdapter
import com.carcaninfo.model.ConnectionStatus
import com.carcaninfo.model.VehicleData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Main activity displaying vehicle CAN data
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var canAdapter: CanAdapter
    private var updateJob: Job? = null
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
        initializeAdapter()
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
    
    private fun initializeAdapter() {
        // Use simulated adapter for now
        // Replace with ObdCanAdapter() when connected to real hardware
        canAdapter = SimulatedCanAdapter()
    }
    
    private fun connectToAdapter() {
        lifecycleScope.launch {
            updateConnectionStatus(ConnectionStatus.CONNECTING)
            
            val connected = canAdapter.connect()
            
            if (connected) {
                updateConnectionStatus(ConnectionStatus.CONNECTED)
                startDataUpdates()
            } else {
                updateConnectionStatus(ConnectionStatus.ERROR)
            }
        }
    }
    
    private fun startDataUpdates() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch {
            while (isActive && canAdapter.isConnected()) {
                try {
                    val data = canAdapter.readVehicleData()
                    updateUI(data)
                    delay(200) // Update every 200ms for smooth display
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateConnectionStatus(ConnectionStatus.ERROR)
                    break
                }
            }
        }
    }
    
    private fun updateUI(data: VehicleData) {
        runOnUiThread {
            // Update speed
            speedValue.text = data.speed.toString()
            
            // Update RPM
            rpmValue.text = data.rpm.toString()
            
            // Update coolant temperature
            coolantValue.text = data.coolantTemp.toString()
            
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
        updateJob?.cancel()
        canAdapter.disconnect()
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
