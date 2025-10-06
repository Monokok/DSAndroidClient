package com.yeah.utils.converters

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

//@TypeConverter
fun restoreList(listOfString: String?): List<String?> {
    return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
}

//@TypeConverter
fun saveList(listOfString: List<String?>?): String {
    return Gson().toJson(listOfString)
}