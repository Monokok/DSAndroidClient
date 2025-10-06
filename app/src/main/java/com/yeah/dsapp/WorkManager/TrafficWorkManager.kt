package com.yeah.dsapp.WorkManager

import android.content.Context
import android.net.TrafficStats
import android.os.BatteryManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters



class TrafficMonitorWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private val TAG = "TrafficMonitorWorker" // Тег для логирования

    override fun doWork(): Result {
        // Получаем текущие данные о трафике
        val rxBytesBefore = TrafficStats.getUidRxBytes(android.os.Process.myUid())
        val txBytesBefore = TrafficStats.getUidTxBytes(android.os.Process.myUid())

        Log.d(TAG, "Before Traffic - RxBytes: $rxBytesBefore, TxBytes: $txBytesBefore")
//        Thread.sleep(10000)

        val rxBytesAfter = TrafficStats.getUidRxBytes(android.os.Process.myUid()) //отслеживают трафик только текущего приложения
        val txBytesAfter = TrafficStats.getUidTxBytes(android.os.Process.myUid())

        val rxDelta = rxBytesAfter - rxBytesBefore
        val txDelta = txBytesAfter - txBytesBefore

        // Получаем информацию о батарее
        val batteryManager = applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        // Логируем данные о трафике и заряде батареи
        Log.d("TrafficMonitor", "Received: $rxDelta bytes")
        Log.d("TrafficMonitor", "Transmitted: $txDelta bytes")
        Log.d("BatteryMonitor", "Battery Level: $batteryLevel%")

        // Вернем результат, можно отправить его в интерфейс или лог
        return Result.success()
    }
}

