//package ai
//
//import cn.hutool.http.body.RequestBody
//import java.awt.PageAttributes.MediaType
//
//
//object QiNiuApi {
//    fun djaoisjd(): Unit {
//        val client: OkHttpClient = OkHttpClient().newBuilder()
//            .build()
//        val mediaType: MediaType? = MediaType.parse("application/json")
//        val body: RequestBody? = RequestBody.create(mediaType, "{\n        \"messages\": [{\"role\": \"user\", \"content\": \"七牛云提供 GPU 云产品能用于哪些场景？\"}],\n        \"model\": \"deepseek-v3\",\n        \"stream\": true\n    }")
//        val request: Request? = Builder()
//            .url("https://api.qnaigc.com/v1/chat/completions")
//            .method("POST", body)
//            .addHeader("Authorization", "Bearer sk-4fdb5c11bbc5c7dce2bd35b0c336f4b36c36d94342641ba9802b3652510edf4f")
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response: Response? = client.newCall(request).execute()
//
//    }
//}