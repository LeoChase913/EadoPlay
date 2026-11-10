package com.shilapi.xcertplay

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.util.Log
import com.shilapi.xcertplay.orchestration.WirelessHotspotMode

/** Starts the car's existing hotspot without changing its saved SSID or password. */
internal object CarHotspotLaunchStarter {
    private const val TAG = "eadoplay-hotspot-open"
    private const val AP_ENABLED = 13
    private const val AP_ENABLING = 12
    private const val POLL_COUNT = 8

    fun startIfNeeded(context: Context) {
        if (AirPlayPersistence.loadWirelessHotspotMode(context) != WirelessHotspotMode.MANUAL) {
            Log.i(TAG, "Skipped: connection mode is not the car hotspot")
            return
        }
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        if (wifi == null) {
            Log.w(TAG, "Wi-Fi service unavailable")
            return
        }
        try {
            val stateMethod = WifiManager::class.java.getMethod("getWifiApState")
            val startMethod = WifiManager::class.java.getMethod(
                "setWifiApEnabled", WifiConfiguration::class.java, Boolean::class.javaPrimitiveType!!
            )
            repeat(POLL_COUNT) { attempt ->
                val state = stateMethod.invoke(wifi) as Int
                if (state == AP_ENABLED) {
                    Log.i(TAG, "Car hotspot is already enabled")
                    return
                }
                if (state != AP_ENABLING && (attempt == 0 || attempt == 6)) {
                    val accepted = startMethod.invoke(wifi, null, true) as Boolean
                    Log.i(TAG, "Car hotspot start requested; accepted=$accepted attempt=${attempt + 1}")
                }
                Thread.sleep(1000)
            }
            if ((stateMethod.invoke(wifi) as Int) != AP_ENABLED) {
                Log.w(TAG, "Car hotspot did not reach enabled state after startup attempts")
            }
        } catch (error: Exception) {
            Log.w(TAG, "Car hotspot could not be started; use car settings", error)
        }
    }
}
