package com.carcaninfo.model

/**
 * Data class representing vehicle telemetry data
 */
data class VehicleData(
    val speed: Int = 0,                    // km/h
    val rpm: Int = 0,                      // revolutions per minute
    val coolantTemp: Int = 0,              // °C
    val fuelLevel: Int = 0,                // percentage
    val engineLoad: Int = 0,               // percentage
    val throttlePosition: Int = 0,         // percentage
    val batteryVoltage: Float = 0f,        // volts
    val intakeTemp: Int = 0,               // °C
    val timestamp: Long = System.currentTimeMillis()
)
