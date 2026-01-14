package com.carcaninfo.can

import android.content.Context
import android.util.Log
import com.carcaninfo.can.builtin.BuiltInCanAdapter

/**
 * Factory for creating the appropriate CAN adapter
 * Automatically detects available CAN interfaces
 */
object CanAdapterFactory {
    
    private const val TAG = "CanAdapterFactory"
    
    enum class AdapterType {
        BUILT_IN,      // Built-in head unit CAN module
        OBD_ADAPTER,   // External OBD-II adapter
        SIMULATED      // Simulated for testing
    }
    
    /**
     * Create the best available CAN adapter
     * Tries in order: Built-in > OBD > Simulated
     */
    suspend fun createAdapter(context: Context, preferredType: AdapterType? = null): CanAdapter {
        Log.d(TAG, "Creating CAN adapter, preferred: $preferredType")
        
        // If user specified a preference, try that first
        if (preferredType != null) {
            val adapter = createAdapterByType(context, preferredType)
            if (adapter.connect()) {
                Log.i(TAG, "Connected using preferred adapter: $preferredType")
                return adapter
            }
            Log.w(TAG, "Failed to connect with preferred adapter: $preferredType")
        }
        
        // Try built-in CAN first
        val builtIn = BuiltInCanAdapter(context)
        if (builtIn.connect()) {
            Log.i(TAG, "Connected using built-in CAN adapter")
            return builtIn
        }
        
        // Try OBD-II adapter
        val obd = ObdCanAdapter()
        if (obd.connect()) {
            Log.i(TAG, "Connected using OBD-II adapter")
            return obd
        }
        
        // Fall back to simulated adapter
        Log.w(TAG, "No real adapters available, using simulated adapter")
        val simulated = SimulatedCanAdapter()
        simulated.connect()
        return simulated
    }
    
    /**
     * Create a specific adapter type
     */
    private fun createAdapterByType(context: Context, type: AdapterType): CanAdapter {
        return when (type) {
            AdapterType.BUILT_IN -> BuiltInCanAdapter(context)
            AdapterType.OBD_ADAPTER -> ObdCanAdapter()
            AdapterType.SIMULATED -> SimulatedCanAdapter()
        }
    }
    
    /**
     * Detect which CAN interfaces are available
     */
    suspend fun detectAvailableAdapters(context: Context): List<AdapterType> {
        val available = mutableListOf<AdapterType>()
        
        // Check built-in
        val builtIn = BuiltInCanAdapter(context)
        if (builtIn.connect()) {
            available.add(AdapterType.BUILT_IN)
            builtIn.disconnect()
        }
        
        // Check OBD
        val obd = ObdCanAdapter()
        if (obd.connect()) {
            available.add(AdapterType.OBD_ADAPTER)
            obd.disconnect()
        }
        
        // Simulated is always available
        available.add(AdapterType.SIMULATED)
        
        Log.d(TAG, "Available adapters: $available")
        return available
    }
}
