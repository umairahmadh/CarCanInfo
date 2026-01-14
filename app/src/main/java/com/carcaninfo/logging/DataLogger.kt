package com.carcaninfo.logging

import android.content.Context
import android.util.Log
import com.carcaninfo.model.VehicleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Manager for logging vehicle data to files
 */
class DataLogger(private val context: Context) {
    
    companion object {
        private const val TAG = "DataLogger"
        private const val LOG_DIR = "vehicle_logs"
        private const val MAX_LOG_FILES = 10
    }
    
    private var isLogging = false
    private var currentLogFile: File? = null
    private var logWriter: FileWriter? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
    private val timestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    
    /**
     * Start logging data
     */
    suspend fun startLogging(): Boolean = withContext(Dispatchers.IO) {
        try {
            if (isLogging) {
                Log.w(TAG, "Logging already started")
                return@withContext false
            }
            
            // Create log directory
            val logDir = File(context.getExternalFilesDir(null), LOG_DIR)
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            
            // Create new log file
            val timestamp = dateFormat.format(Date())
            currentLogFile = File(logDir, "vehicle_log_$timestamp.csv")
            logWriter = FileWriter(currentLogFile, true)
            
            // Write CSV header
            logWriter?.appendLine("Timestamp,Speed(km/h),RPM,Coolant(°C),Fuel(%),Load(%),Throttle(%),Battery(V),Intake(°C)")
            logWriter?.flush()
            
            isLogging = true
            Log.i(TAG, "Started logging to: ${currentLogFile?.absolutePath}")
            
            // Clean up old log files
            cleanOldLogs(logDir)
            
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error starting logging: ${e.message}")
            false
        }
    }
    
    /**
     * Stop logging data
     */
    suspend fun stopLogging() = withContext(Dispatchers.IO) {
        try {
            if (!isLogging) {
                return@withContext
            }
            
            logWriter?.flush()
            logWriter?.close()
            logWriter = null
            
            isLogging = false
            Log.i(TAG, "Stopped logging")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping logging: ${e.message}")
        }
    }
    
    /**
     * Log vehicle data
     */
    suspend fun logData(data: VehicleData) = withContext(Dispatchers.IO) {
        if (!isLogging || logWriter == null) {
            return@withContext
        }
        
        try {
            val timestamp = timestampFormat.format(Date(data.timestamp))
            val line = "$timestamp,${data.speed},${data.rpm},${data.coolantTemp}," +
                      "${data.fuelLevel},${data.engineLoad},${data.throttlePosition}," +
                      "${data.batteryVoltage},${data.intakeTemp}"
            
            logWriter?.appendLine(line)
            
            // Flush periodically to ensure data is written
            if (System.currentTimeMillis() % 5000 < 200) {
                logWriter?.flush()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error logging data: ${e.message}")
        }
    }
    
    /**
     * Get list of all log files
     */
    suspend fun getLogFiles(): List<File> = withContext(Dispatchers.IO) {
        val logDir = File(context.getExternalFilesDir(null), LOG_DIR)
        if (!logDir.exists()) {
            return@withContext emptyList()
        }
        
        logDir.listFiles()
            ?.filter { it.isFile && it.name.endsWith(".csv") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }
    
    /**
     * Delete a log file
     */
    suspend fun deleteLogFile(file: File): Boolean = withContext(Dispatchers.IO) {
        try {
            if (file == currentLogFile) {
                Log.w(TAG, "Cannot delete current log file")
                return@withContext false
            }
            
            file.delete()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting log file: ${e.message}")
            false
        }
    }
    
    /**
     * Delete all log files
     */
    suspend fun deleteAllLogs(): Boolean = withContext(Dispatchers.IO) {
        try {
            stopLogging()
            
            val logDir = File(context.getExternalFilesDir(null), LOG_DIR)
            if (logDir.exists()) {
                logDir.listFiles()?.forEach { it.delete() }
            }
            
            Log.i(TAG, "Deleted all log files")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting all logs: ${e.message}")
            false
        }
    }
    
    /**
     * Export log file (returns file path for sharing)
     */
    suspend fun exportLog(file: File): String? = withContext(Dispatchers.IO) {
        try {
            if (!file.exists()) {
                return@withContext null
            }
            
            file.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting log: ${e.message}")
            null
        }
    }
    
    /**
     * Get total size of all log files
     */
    suspend fun getTotalLogSize(): Long = withContext(Dispatchers.IO) {
        val logFiles = getLogFiles()
        logFiles.sumOf { it.length() }
    }
    
    /**
     * Clean up old log files, keeping only MAX_LOG_FILES
     */
    private fun cleanOldLogs(logDir: File) {
        try {
            val logFiles = logDir.listFiles()
                ?.filter { it.isFile && it.name.endsWith(".csv") }
                ?.sortedByDescending { it.lastModified() }
                ?: return
            
            if (logFiles.size > MAX_LOG_FILES) {
                logFiles.drop(MAX_LOG_FILES).forEach { file ->
                    file.delete()
                    Log.d(TAG, "Deleted old log file: ${file.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning old logs: ${e.message}")
        }
    }
    
    /**
     * Check if logging is active
     */
    fun isLogging(): Boolean = isLogging
    
    /**
     * Get current log file
     */
    fun getCurrentLogFile(): File? = currentLogFile
}
