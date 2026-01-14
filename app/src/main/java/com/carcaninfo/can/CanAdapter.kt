package com.carcaninfo.can

import com.carcaninfo.model.VehicleData

/**
 * Interface for CAN adapter communication
 */
interface CanAdapter {
    /**
     * Connect to the CAN adapter
     */
    suspend fun connect(): Boolean
    
    /**
     * Disconnect from the CAN adapter
     */
    fun disconnect()
    
    /**
     * Check if adapter is connected
     */
    fun isConnected(): Boolean
    
    /**
     * Read vehicle data from the adapter
     */
    suspend fun readVehicleData(): VehicleData
    
    /**
     * Send a raw CAN command
     */
    suspend fun sendCommand(command: String): String?
}
