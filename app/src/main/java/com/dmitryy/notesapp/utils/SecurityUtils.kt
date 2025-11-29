package com.dmitryy.notesapp.utils

import android.os.Build
import java.io.File

object SecurityUtils {
    
    /**
     * Check if the device is rooted by looking for common root indicators
     */
    fun isRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }
    
    /**
     * Check if running on an emulator
     */
    fun hasEmulator(): Boolean {
        return checkEmulatorBuildProperties() || checkEmulatorFiles()
    }
    
    /**
     * Check if Magisk is installed
     */
    fun hasMagisk(): Boolean {
        return checkMagiskFiles() || checkMagiskProperties()
    }
    
    // Root detection methods
    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }
    
    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        return paths.any { File(it).exists() }
    }
    
    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val bufferedReader = process.inputStream.bufferedReader()
            bufferedReader.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }
    
    // Emulator detection methods
    private fun checkEmulatorBuildProperties(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }
    
    private fun checkEmulatorFiles(): Boolean {
        val knownFiles = arrayOf(
            "/dev/socket/qemud",
            "/dev/qemu_pipe",
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
        )
        return knownFiles.any { File(it).exists() }
    }
    
    // Magisk detection methods
    private fun checkMagiskFiles(): Boolean {
        val magiskPaths = arrayOf(
            "/sbin/.magisk",
            "/sbin/.core/mirror",
            "/sbin/.core/img",
            "/sbin/.core/db-0/magisk.db",
            "/cache/magisk.log",
            "/data/adb/magisk",
            "/data/adb/magisk.img",
            "/data/adb/magisk.db",
            "/data/adb/magisk_simple"
        )
        return magiskPaths.any { File(it).exists() }
    }
    
    private fun checkMagiskProperties(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec("getprop ro.boot.vbmeta.device_state")
            val bufferedReader = process.inputStream.bufferedReader()
            val output = bufferedReader.readLine()
            output != null && output.contains("unlocked")
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }
}
