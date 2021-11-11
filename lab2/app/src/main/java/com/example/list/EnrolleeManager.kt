package com.example.list

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder


class EnrolleeManager(_file: File) {
    var file: File = _file

    fun save(list: MutableList<Enrollee>): Boolean {
        if (list.isEmpty()) return false
        //val gson = GsonBuilder().setPrettyPrinting().create()
        val gson = Gson()
        val jsonString: String = gson.toJson(list)
        try {
            file.writeText(jsonString, Charsets.UTF_8)
        }
        catch (e: Exception) {
            return false
        }
        return true
    }

    fun read(list: MutableList<Enrollee>): Boolean {
        //val gson = GsonBuilder().setPrettyPrinting().create()
        val gson = Gson()
        var jsonString: String
        try {
            jsonString = file.readText(Charsets.UTF_8)
        }
        catch (e: Exception) {
            return false
        }
        val enrolleeType = object : TypeToken<MutableList<Enrollee>>() {}.type
        val tempList: MutableList<Enrollee> = gson.fromJson(jsonString, enrolleeType)
        list.clear()
        for (elem in tempList) {
            list.add(elem)
        }
        return true
    }

    fun read(list: MutableList<Enrollee>, inputStream: InputStream?): Boolean {
        val gson = Gson()
        var jsonString: String
        try {
            val streamReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val responseStrBuilder = StringBuilder(2048)

            var inputStr: String?
            while (streamReader.readLine().also { inputStr = it } != null)
                responseStrBuilder.append(inputStr)
            jsonString = responseStrBuilder.toString()
        }
        catch (e: Exception) {
            return false
        }
        val enrolleeType = object : TypeToken<MutableList<Enrollee>>() {}.type
        val tempList: MutableList<Enrollee> = gson.fromJson(jsonString, enrolleeType)
        list.clear()
        for (elem in tempList) {
            list.add(elem)
        }
        return true
    }
}