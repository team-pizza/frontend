package com.pizza.android.bas.networking

import android.os.Handler
import com.beust.klaxon.Klaxon
import com.pizza.android.bas.MainActivity
import com.pizza.android.bas.R
import java.io.InputStreamReader
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class NetworkAdapter(val mainActivity: MainActivity) {

    val dummyVerifier = HostnameVerifier { hostname: String, session: SSLSession ->
        return@HostnameVerifier true
    }

    init {
        val trustManager = object: X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        }
        try {
            val sslc = SSLContext.getInstance("TLS")
            sslc.init(null, arrayOf(trustManager), SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.socketFactory)
        } catch (exc: Exception) {

        }
    }

    inline fun <reified RES> get(path: String, crossinline callback: (RES?)->Unit) {
        val thread = Thread {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
            val url = URL("https://${mainActivity.getString(R.string.backend_server_ip)}${path}")

            val connection = url.openConnection() as HttpsURLConnection
            connection.hostnameVerifier = dummyVerifier

            connection.setRequestProperty("sessionToken", mainActivity.getUserIdentity()?.sessionToken)

            var result: RES? = null

            connection.connect()

            if(connection.responseCode == 200) {
                val responseInputStream = connection.inputStream
                val responseBodyReader = InputStreamReader(responseInputStream, "UTF-8")
                var responseBodyString = ""

                var data = responseBodyReader.read()
                while(data != -1) {
                    responseBodyString += data.toChar()
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
            val urlString = "https://${mainActivity.getString(R.string.backend_server_ip)}${path}"
            val url = URL(urlString)

            val connection = url.openConnection() as HttpsURLConnection
            connection.hostnameVerifier = dummyVerifier
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")

            val jsonBody: String = Klaxon().toJsonString(body)

            connection.setRequestProperty("sessionToken", mainActivity.getUserIdentity()?.sessionToken)

            connection.doOutput = true

            connection.connect()

            connection.outputStream.write(jsonBody.toByteArray())
            connection.outputStream.close()

            var result: RES? = null

            if(connection.responseCode == 200) {
                val responseInputStream = connection.inputStream
                val responseBodyReader = InputStreamReader(responseInputStream, "UTF-8")
                var responseBodyString = ""

                var data = responseBodyReader.read()
                while(data != -1) {
                    responseBodyString += data.toChar()
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