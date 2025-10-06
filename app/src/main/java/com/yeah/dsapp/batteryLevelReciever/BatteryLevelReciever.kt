package com.yeah.dsapp.batteryLevelReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BatteryLevelReceiver : BroadcastReceiver() {
    private var lastBatteryPercentage: Int = -1 // Переменная для хранения предыдущего уровня заряда

    override fun onReceive(context: Context, intent: Intent) {


        val level = intent.getIntExtra("level", -1) // Текущий уровень заряда
        val scale = intent.getIntExtra("scale", -1) // Максимальный уровень заряда (обычно 100)

        if (level >= 0 && scale > 0) {
            val batteryPercentage = ((level / scale.toFloat()) * 100).toInt()

            // Проверяем, изменился ли уровень заряда
            if (batteryPercentage != lastBatteryPercentage) {
                lastBatteryPercentage = batteryPercentage // Обновляем последнее значение

                if (batteryPercentage < 15) {
                    Toast.makeText(context, "Низкий уровень заряда: $batteryPercentage%", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Уровень батареи: ${batteryPercentage}%", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
