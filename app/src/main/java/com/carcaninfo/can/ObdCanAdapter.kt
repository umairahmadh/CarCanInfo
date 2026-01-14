package com.carcaninfo.can

import android.util.Log
import com.carcaninfo.model.VehicleData
import kotlinx.coroutines.delay

/**
 * OBD-II CAN adapter implementation using ELM327 protocol
 * Supports standard OBD-II PIDs for vehicle data
 */
class ObdCanAdapter : CanAdapter {
    
    companion object {
        private const val TAG = "ObdCanAdapter"
        
        // Standard OBD-II PIDs
        private const val PID_ENGINE_RPM = "010C"
        private const val PID_VEHICLE_SPEED = "010D"
        private const val PID_COOLANT_TEMP = "0105"
        private const val PID_FUEL_LEVEL = "012F"
        private const val PID_ENGINE_LOAD = "0104"
        private const val PID_THROTTLE_POSITION = "0111"
        private const val PID_INTAKE_TEMP = "010F"
        
        // ELM327 commands
        private const val CMD_RESET = "ATZ"
        private const val CMD_ECHO_OFF = "ATE0"
        private const val CMD_PROTOCOL_AUTO = "ATSP0"
        private const val CMD_VOLTAGE = "ATRV"
    }
    
    private var connected = false
    
    override suspend fun connect(): Boolean {
        try {
            Log.d(TAG, "Connecting to OBD-II adapter...")
            
            // Initialize ELM327
            sendCommand(CMD_RESET)
            delay(1500)
            
            sendCommand(CMD_ECHO_OFF)
            delay(100)
            
            sendCommand(CMD_PROTOCOL_AUTO)
            delay(100)
            
            connected = true
            Log.d(TAG, "Connected successfully")
            return true
            
        } catch (e: Exception) {
            Log.e(TAG, "Connection failed: ${e.message}")
            connected = false
            return false
        }
    }
    
    override fun disconnect() {
        connected = false
        Log.d(TAG, "Disconnected")
    }
    
    override fun isConnected(): Boolean = connected
    
    override suspend fun readVehicleData(): VehicleData {
        if (!connected) {
            return VehicleData()
        }
        
        return try {
            val speed = readSpeed()
            val rpm = readRpm()
            val coolantTemp = readCoolantTemp()
            val fuelLevel = readFuelLevel()
            val engineLoad = readEngineLoad()
            val throttlePosition = readThrottlePosition()
            val batteryVoltage = readBatteryVoltage()
            val intakeTemp = readIntakeTemp()
            
            VehicleData(
                speed = speed,
                rpm = rpm,
                coolantTemp = coolantTemp,
                fuelLevel = fuelLevel,
                engineLoad = engineLoad,
                throttlePosition = throttlePosition,
                batteryVoltage = batteryVoltage,
                intakeTemp = intakeTemp
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error reading vehicle data: ${e.message}")
            VehicleData()
        }
    }
    
    override suspend fun sendCommand(command: String): String? {
        // TODO: Implement actual serial/USB communication
        // This is a placeholder that returns mock responses for testing
        Log.d(TAG, "Sending command: $command")
        delay(50)
        
        // Return mock responses for testing (will be replaced with actual adapter communication)
        return when {
            command.startsWith("010C") -> "41 0C 1F 40" // RPM mock (2-byte: ~2000 RPM)
            command.startsWith("010D") -> "41 0D 2D" // Speed mock
            command.startsWith("0105") -> "41 05 5A" // Coolant mock
            command.startsWith("012F") -> "41 2F 80" // Fuel mock
            command.startsWith("0104") -> "41 04 40" // Load mock
            command.startsWith("0111") -> "41 11 50" // Throttle mock
            command.startsWith("010F") -> "41 0F 3C" // Intake temp mock
            command.startsWith("ATRV") -> "12.8V" // Voltage mock
            else -> "OK"
        }
    }
    
    private suspend fun readSpeed(): Int {
        val response = sendCommand(PID_VEHICLE_SPEED)
        return parseHexValue(response, 0)
    }
    
    private suspend fun readRpm(): Int {
        val response = sendCommand(PID_ENGINE_RPM)
        return parseMultiByteValue(response, 0) / 4
    }
    
    private suspend fun readCoolantTemp(): Int {
        val response = sendCommand(PID_COOLANT_TEMP)
        return parseHexValue(response, -40)
    }
    
    private suspend fun readFuelLevel(): Int {
        val response = sendCommand(PID_FUEL_LEVEL)
        return (parseHexValue(response, 0) * 100) / 255
    }
    
    private suspend fun readEngineLoad(): Int {
        val response = sendCommand(PID_ENGINE_LOAD)
        return (parseHexValue(response, 0) * 100) / 255
    }
    
    private suspend fun readThrottlePosition(): Int {
        val response = sendCommand(PID_THROTTLE_POSITION)
        return (parseHexValue(response, 0) * 100) / 255
    }
    
    private suspend fun readBatteryVoltage(): Float {
        val response = sendCommand(CMD_VOLTAGE)
        return parseVoltage(response)
    }
    
    private suspend fun readIntakeTemp(): Int {
        val response = sendCommand(PID_INTAKE_TEMP)
        return parseHexValue(response, -40)
    }
    
    private fun parseHexValue(response: String?, offset: Int = 0): Int {
        if (response.isNullOrEmpty()) return 0
        
        return try {
            // Parse OBD-II response format: "41 0C 1A F8" 
            // Expected format: [mode+0x40] [PID] [data bytes...]
            val bytes = response.split(" ").filter { it.length == 2 }
            
            // Validate response has proper OBD-II format
            if (bytes.size >= 3 && bytes[0] == "41") {
                // bytes[0] = 41 (response to mode 01)
                // bytes[1] = PID
                // bytes[2+] = data
                val value = bytes[2].toInt(16)
                value + offset
            } else {
                0
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse hex value: ${e.message}")
            0
        }
    }
    
    private fun parseMultiByteValue(response: String?, offset: Int = 0): Int {
        if (response.isNullOrEmpty()) return 0
        
        return try {
            // Parse OBD-II multi-byte response format: "41 0C 1A F8"
            // For RPM and other 2-byte values: ((A*256)+B)
            val bytes = response.split(" ").filter { it.length == 2 }
            
            // Validate response has proper OBD-II format
            if (bytes.size >= 4 && bytes[0] == "41") {
                // bytes[0] = 41 (response to mode 01)
                // bytes[1] = PID
                // bytes[2] = A (high byte)
                // bytes[3] = B (low byte)
                val a = bytes[2].toInt(16)
                val b = bytes[3].toInt(16)
                (a * 256 + b) + offset
            } else {
                0
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse multi-byte value: ${e.message}")
            0
        }
    }
    
    private fun parseVoltage(response: String?): Float {
        if (response.isNullOrEmpty()) return 0f
        
        return try {
            response.replace("V", "").trim().toFloat()
        } catch (e: Exception) {
            0f
        }
    }
}
