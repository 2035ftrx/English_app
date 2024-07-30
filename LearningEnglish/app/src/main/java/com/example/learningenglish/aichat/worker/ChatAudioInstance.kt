package com.example.learningenglish.aichat.worker

import com.example.learningenglish.http.ai.AIApiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit


object ChatAudioInstance {

    private val audioPlayer = ChatAudioPlayer()
    private val token = MutableStateFlow<String?>("")

    val audioState = audioPlayer.state

    suspend fun refreshToken() {
        val aiApiRepository = AIApiRepository()
        val tokenResponse = aiApiRepository.audioToken()
        if (tokenResponse.isSuccess) {
            val orThrow = tokenResponse.getOrThrow()
            if (orThrow.isSuccess) {
                val aiTokenResponse = orThrow.data
                val accessToken = aiTokenResponse.access_token
                token.emit(accessToken)
            } else {
                delay(1000)
                refreshToken()
            }
        } else {
            delay(1000)
            refreshToken()
        }
    }


    suspend fun play(text: String) {
        token.value?.let {
            playChat(text, it)
        } ?: run {
            refreshToken()
            play(text)
        }
    }

    private fun playChat(text: String, token: String) {
        // https://github.com/Baidu-AIP/speech-demo/blob/master/rest-api-tts/java/src/com/baidu/speech/restapi/ttsdemo/TtsMain.java

        // 发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
        // 精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
        val per = 0
        // 语速，取值0-15，默认为5中语速
        val spd = 5
        // 音调，取值0-15，默认为5中语调
        val pit = 5
        // 音量，取值0-9，默认为5中音量
        val vol = 5
        // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
        val aue = 3

        val cuid = "1234567JAVA";
        val url = "https://tsn.baidu.com/text2audio" // 可以使用https
        // 此处2次urlencode， 确保特殊字符被正确编码
        var params = "tex=" + urlEncode(urlEncode(text))
        params += "&per=$per"
        params += "&spd=$spd"
        params += "&pit=$pit"
        params += "&vol=$vol"
        params += "&cuid=$cuid"
        params += "&tok=$token"
        params += "&aue=$aue"
        params += "&lan=zh&ctp=1"
        val finalUrl = "$url?$params"
        Timber.d(finalUrl) // 反馈请带上此url，浏览器上可以测试
        val format = getFormat(aue) // Assuming you have this function
//        val audioFilePath = downloadFile(finalUrl, format)
//        audioFilePath?.let { audioPlayer.play(it) }
        audioPlayer.play(finalUrl)
    }

    private fun urlEncode(str: String?): String? {
        try {
            return URLEncoder.encode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return str
    }

    private fun downloadFile(url: String, suffix: String): String? {
        val mediaType = "application/json; charset=utf-8".toMediaType() // Assuming JSON data

        // Prepare your request body (replace with your actual data)
        val json = "{   }"
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS) // Set connect timeout
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                when (response.code) {
                    401 -> Timber.d("Possibly incorrect appkey or appSecret")
                    else -> Timber.d("Error: unexpected response code ${response.code}")
                }
                Timber.d("Response headers: ${response.headers}")
            } else {
                val responseBody = response.body
                Timber.d(" content type : ${responseBody?.contentType()}")
                if (responseBody?.contentType().toString().contains("audio/")) {
                    responseBody?.let {
                        val bytes = responseBody.bytes()
                        val file = File.createTempFile("chatrecord", ".$suffix")
                        val fileOutputStream = FileOutputStream(file)
                        fileOutputStream.write(bytes)
                        fileOutputStream.close()
                        Timber.d("Audio file written to ${file.absolutePath}")
                        return file.absolutePath
                    }
                } else {
                    val responseString = responseBody?.string()
                    Timber.d(responseString) // Assuming response is an error message
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private fun getFormat(aue: Int): String {
        val formats = arrayOf("mp3", "pcm", "pcm", "wav")
        return formats[aue - 3]
    }
}