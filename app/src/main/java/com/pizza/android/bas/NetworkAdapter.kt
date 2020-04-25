package com.pizza.android.bas

import android.os.Handler
import com.beust.klaxon.Klaxon
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkAdapter(val mainActivity: MainActivity) {
    inline fun <reified RES> get(path: String, crossinline callback: (RES?)->Unit) {
        val thread = Thread {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
            val url = URL("https://${mainActivity.getString(R.string.backend_server_ip)}${path}")

            val connection = url.openConnection() as HttpsURLConnection

            connection.setRequestProperty("sessionToken", mainActivity.getUserIdentity()?.sessionToken)

            var result: RES? = null

            if(connection.responseCode == 200) {
                val responseInputStream = connection.inputStream
                val responseBodyReader = InputStreamReader(responseInputStream, "UTF-8")
                var responseBodyString = ""

                var data = responseBodyReader.read()
                while(data != -1) {
                    responseBodyString += data
                    data = responseBodyReader.read()
                }

                result = Klaxon().parse<RES>(responseBodyString)
            }

            val mainHandler = Handler(mainActivity.mainLooper)
            mainHandler.post {
                callback(result)
            }
        }
        thread.start()
    }
    inline fun <reified BDY, reified RES> post(path: String, body: BDY, crossinline callback: (RES?)->Unit) {
        val thread = Thread {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
            val url = URL("https://${mainActivity.getString(R.string.backend_server_ip)}${path}")

            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"

            val jsonBody: String = Klaxon().toJsonString(body)

            connection.doOutput = true
            connection.outputStream.write(jsonBody.toByteArray())

            connection.setRequestProperty("sessionToken", mainActivity.getUserIdentity()?.sessionToken)

            var result: RES? = null

            if(connection.responseCode == 200) {
                val responseInputStream = connection.inputStream
                val responseBodyReader = InputStreamReader(responseInputStream, "UTF-8")
                var responseBodyString = ""

                var data = responseBodyReader.read()
                while(data != -1) {
                    responseBodyString += data
                    data = responseBodyReader.read()
                }

                result = Klaxon().parse<RES>(responseBodyString)
            }

            val mainHandler = Handler(mainActivity.mainLooper)
            mainHandler.post {
                callback(result)
            }
        }
        thread.start()
    }
}