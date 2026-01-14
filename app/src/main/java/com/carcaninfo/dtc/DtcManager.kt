package com.carcaninfo.dtc

import android.util.Log
import com.carcaninfo.can.CanAdapter
import kotlinx.coroutines.delay

/**
 * Manager for reading and clearing Diagnostic Trouble Codes
 */
class DtcManager(private val canAdapter: CanAdapter) {
    
    companion object {
        private const val TAG = "DtcManager"
        
        // OBD-II Mode 03 - Request emission-related DTCs
        private const val MODE_03_REQUEST_DTCS = "03"
        
        // OBD-II Mode 04 - Clear DTCs
        private const val MODE_04_CLEAR_DTCS = "04"
        
        // OBD-II Mode 07 - Request pending DTCs
        private const val MODE_07_PENDING_DTCS = "07"
        
        // OBD-II Mode 0A - Request permanent DTCs
        private const val MODE_0A_PERMANENT_DTCS = "0A"
        
        // Common DTC descriptions
        private val DTC_DESCRIPTIONS = mapOf(
            "P0300" to "Random/Multiple Cylinder Misfire Detected",
            "P0301" to "Cylinder 1 Misfire Detected",
            "P0302" to "Cylinder 2 Misfire Detected",
            "P0303" to "Cylinder 3 Misfire Detected",
            "P0304" to "Cylinder 4 Misfire Detected",
            "P0420" to "Catalyst System Efficiency Below Threshold",
            "P0171" to "System Too Lean (Bank 1)",
            "P0172" to "System Too Rich (Bank 1)",
            "P0440" to "Evaporative Emission System Malfunction",
            "P0128" to "Coolant Thermostat (Coolant Temp Below Threshold)",
            "P0401" to "Exhaust Gas Recirculation Flow Insufficient",
            "P0442" to "Evaporative Emission System Leak Detected (small leak)",
            "P0455" to "Evaporative Emission System Leak Detected (large leak)",
            "P0113" to "Intake Air Temperature Sensor Circuit High",
            "P0118" to "Engine Coolant Temperature Circuit High",
            "P0335" to "Crankshaft Position Sensor Circuit Malfunction",
            "P0340" to "Camshaft Position Sensor Circuit Malfunction"
        )
    }
    
    /**
     * Read current DTCs
     */
    suspend fun readCurrentDtcs(): List<DiagnosticTroubleCode> {
        return readDtcs(MODE_03_REQUEST_DTCS, DiagnosticTroubleCode.Status.CURRENT)
    }
    
    /**
     * Read pending DTCs
     */
    suspend fun readPendingDtcs(): List<DiagnosticTroubleCode> {
        return readDtcs(MODE_07_PENDING_DTCS, DiagnosticTroubleCode.Status.PENDING)
    }
    
    /**
     * Read permanent DTCs
     */
    suspend fun readPermanentDtcs(): List<DiagnosticTroubleCode> {
        return readDtcs(MODE_0A_PERMANENT_DTCS, DiagnosticTroubleCode.Status.PERMANENT)
    }
    
    /**
     * Read all DTCs
     */
    suspend fun readAllDtcs(): List<DiagnosticTroubleCode> {
        val all = mutableListOf<DiagnosticTroubleCode>()
        all.addAll(readCurrentDtcs())
        all.addAll(readPendingDtcs())
        all.addAll(readPermanentDtcs())
        return all
    }
    
    /**
     * Clear all DTCs
     */
    suspend fun clearAllDtcs(): Boolean {
        return try {
            Log.d(TAG, "Clearing all DTCs...")
            val response = canAdapter.sendCommand(MODE_04_CLEAR_DTCS)
            delay(2000) // Wait for ECU to clear codes
            
            if (response != null) {
                Log.i(TAG, "DTCs cleared successfully")
                true
            } else {
                Log.w(TAG, "Failed to clear DTCs")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing DTCs: ${e.message}")
            false
        }
    }
    
    /**
     * Read DTCs with specified mode
     */
    private suspend fun readDtcs(mode: String, status: DiagnosticTroubleCode.Status): List<DiagnosticTroubleCode> {
        return try {
            Log.d(TAG, "Reading DTCs with mode: $mode")
            val response = canAdapter.sendCommand(mode)
            
            if (response != null) {
                parseDtcResponse(response, status)
            } else {
                Log.w(TAG, "No response from DTC request")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading DTCs: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Parse DTC response from OBD-II
     * Format: "43 01 33 01 00 00" where 01 33 is the DTC
     */
    private fun parseDtcResponse(response: String, status: DiagnosticTroubleCode.Status): List<DiagnosticTroubleCode> {
        val dtcs = mutableListOf<DiagnosticTroubleCode>()
        
        try {
            val bytes = response.split(" ").mapNotNull { 
                it.trim().takeIf { s -> s.length == 2 }?.toIntOrNull(16) 
            }
            
            // First byte is mode response (43, 47, or 4A)
            // Second byte is number of DTCs
            if (bytes.size >= 2) {
                val count = bytes[1]
                Log.d(TAG, "Found $count DTC(s)")
                
                // Each DTC is 2 bytes
                var i = 2
                while (i < bytes.size - 1 && dtcs.size < count) {
                    val byte1 = bytes[i]
                    val byte2 = bytes[i + 1]
                    
                    val code = decodeDtc(byte1, byte2)
                    if (code != null) {
                        dtcs.add(
                            DiagnosticTroubleCode(
                                code = code,
                                description = DTC_DESCRIPTIONS[code] ?: "Unknown DTC",
                                severity = determineSeverity(code),
                                status = status
                            )
                        )
                    }
                    
                    i += 2
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing DTC response: ${e.message}")
        }
        
        return dtcs
    }
    
    /**
     * Decode DTC from two bytes
     */
    private fun decodeDtc(byte1: Int, byte2: Int): String? {
        // First 2 bits of byte1 determine the prefix
        val prefixBits = (byte1 shr 6) and 0x03
        val prefix = when (prefixBits) {
            0 -> 'P'
            1 -> 'C'
            2 -> 'B'
            3 -> 'U'
            else -> return null
        }
        
        // Next 2 bits are the first digit
        val digit1 = (byte1 shr 4) and 0x03
        
        // Last 4 bits of byte1 are the second digit
        val digit2 = byte1 and 0x0F
        
        // byte2 is the last two digits in hex
        val lastDigits = String.format("%02X", byte2)
        
        return "$prefix$digit1$digit2$lastDigits"
    }
    
    /**
     * Determine severity based on DTC code
     */
    private fun determineSeverity(code: String): DiagnosticTroubleCode.Severity {
        return when {
            // Critical codes
            code.startsWith("P03") -> DiagnosticTroubleCode.Severity.CRITICAL  // Misfire
            code in listOf("P0335", "P0340") -> DiagnosticTroubleCode.Severity.CRITICAL  // Sensor failures
            
            // Warning codes
            code.startsWith("P04") -> DiagnosticTroubleCode.Severity.WARNING  // Emissions
            code.startsWith("P01") -> DiagnosticTroubleCode.Severity.WARNING  // Fuel/Air
            
            // Info codes
            else -> DiagnosticTroubleCode.Severity.INFO
        }
    }
}
