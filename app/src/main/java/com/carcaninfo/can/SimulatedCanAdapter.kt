package com.carcaninfo.can

import com.carcaninfo.model.VehicleData
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Simulated CAN adapter for testing and development
 * This will be replaced with actual adapter implementation
 */
class SimulatedCanAdapter : CanAdapter {
    
    private var connected = false
    private var speed = 0
    private var rpm = 0
    
    override suspend fun connect(): Boolean {
        delay(1000) // Simulate connection delay
        connected = true
        return true
    }
    
    override fun disconnect() {
        connected = false
        speed = 0
        rpm = 0
    }
    
    override fun isConnected(): Boolean = connected
    
    override suspend fun readVehicleData(): VehicleData {
        if (!connected) {
            return VehicleData()
        }
        
        // Simulate realistic vehicle data changes
        speed = (speed + Random.nextInt(-5, 6)).coerceIn(0, 180)
        rpm = (rpm + Random.nextInt(-200, 201)).coerceIn(600, 6000)
        
        return VehicleData(
            speed = speed,
            rpm = rpm,
            coolantTemp = Random.nextInt(80, 95),
            fuelLevel = Random.nextInt(20, 100),
            engineLoad = Random.nextInt(10, 80),
            throttlePosition = Random.nextInt(0, 100),
            batteryVoltage = 12.0f + Random.nextFloat() * 2.0f,
            intakeTemp = Random.nextInt(20, 40)
        )
    }
    
    override suspend fun sendCommand(command: String): String? {
        if (!connected) return null
        
        delay(50) // Simulate command processing
        return "OK"
    }
}
