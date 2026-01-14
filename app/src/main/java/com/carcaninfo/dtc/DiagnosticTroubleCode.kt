package com.carcaninfo.dtc

/**
 * Diagnostic Trouble Code data model
 */
data class DiagnosticTroubleCode(
    val code: String,                    // e.g., "P0301"
    val description: String,             // Human-readable description
    val severity: Severity,              // How serious is the problem
    val status: Status,                  // Current, pending, or permanent
    val timestamp: Long = System.currentTimeMillis()
) {
    enum class Severity {
        INFO,       // Informational
        WARNING,    // Warning - should be addressed
        CRITICAL    // Critical - immediate attention required
    }
    
    enum class Status {
        CURRENT,    // Currently active
        PENDING,    // Pending - may become active
        PERMANENT   // Permanent - stored in ECU
    }
    
    /**
     * Get severity color
     */
    fun getSeverityColor(): Int {
        return when (severity) {
            Severity.INFO -> 0xFF2196F3.toInt()      // Blue
            Severity.WARNING -> 0xFFFF9800.toInt()   // Orange
            Severity.CRITICAL -> 0xFFF44336.toInt()  // Red
        }
    }
}

/**
 * DTC code types
 */
enum class DtcType(val prefix: Char, val description: String) {
    POWERTRAIN('P', "Powertrain"),         // Engine, transmission
    CHASSIS('C', "Chassis"),                // ABS, suspension
    BODY('B', "Body"),                      // Airbags, climate control
    NETWORK('U', "Network");                // CAN bus communication
    
    companion object {
        fun fromCode(code: String): DtcType? {
            if (code.isEmpty()) return null
            return values().find { it.prefix == code[0] }
        }
    }
}
