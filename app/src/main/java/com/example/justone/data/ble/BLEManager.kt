package com.example.justone.data.ble

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.pm.PackageManager
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat

class BLEManager(private val context: Context) {
    private val bluetoothManager by lazy {
        context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bleAdvertiser
        get() = bluetoothManager.adapter.bluetoothLeAdvertiser

    private val bleScanner
        get() = bluetoothManager.adapter.bluetoothLeScanner

    private val advertisingParameters by lazy {
        AdvertisingSetParameters.Builder()
            .setLegacyMode(false)
            .setInterval(AdvertisingSetParameters.INTERVAL_MIN)
            .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
            .setPrimaryPhy(BluetoothDevice.PHY_LE_1M)
            .setSecondaryPhy(BluetoothDevice.PHY_LE_2M)
            .build()
    }

    private val scanSettings by lazy {
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            .setReportDelay(1000L)
            .build()
    }

    private var currentAdvertisingSet: AdvertisingSet? = null

    private val advertisingSetCallback = object : AdvertisingSetCallback() {
        override fun onAdvertisingSetStarted(
            advertisingSet: AdvertisingSet,
            txPower: Int,
            status: Int
        ) {
            currentAdvertisingSet = advertisingSet
        }

        override fun onAdvertisingSetStopped(advertisingSet: AdvertisingSet) {
            currentAdvertisingSet = null
        }
    }

    private var scanResultsCallback : ((MutableList<String>) -> Unit)? = null

    private val scanCallback = object : ScanCallback() {
        override fun onBatchScanResults(scanResults: MutableList<ScanResult>?) {
            super.onBatchScanResults(scanResults)
            scanResults?.let {
                val results: MutableList<String> = ArrayList()
                for (scanResult in it) {
                    scanResult.scanRecord?.let { record ->
                        val data = record.serviceData[record.serviceUuids.first()]
                        results.add(data.toString())
                    }
                }
                scanResultsCallback?.invoke(results)
            }
        }
    }

    fun startAdvertising(uuid: String, data: String) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val advertiseData = AdvertiseData.Builder()
            .addServiceData(ParcelUuid.fromString(uuid), data.toByteArray())
            .build()

        if (currentAdvertisingSet == null) {
            bleAdvertiser.startAdvertisingSet(
                advertisingParameters,
                advertiseData,
                null,
                null,
                null,
                advertisingSetCallback
            )
        } else {
            currentAdvertisingSet?.setAdvertisingData(advertiseData)
        }
    }

    fun stopAdvertising() {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        bleAdvertiser.stopAdvertisingSet(advertisingSetCallback)
    }

    fun startScan(uuid: String, callback: (MutableList<String>) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val filters = listOf<ScanFilter>(
            ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(uuid)).build()
        )
        scanResultsCallback = callback
        bleScanner.startScan(filters, scanSettings, scanCallback)
    }

    fun stopScan() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        scanResultsCallback = null
        bleScanner.stopScan(scanCallback)
    }
}