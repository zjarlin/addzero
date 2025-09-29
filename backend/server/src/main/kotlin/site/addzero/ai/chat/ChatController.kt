package site.addzero.ai.chat

import site.addzero.ai.config.gettooldef
import io.swagger.v3.oas.annotations.Operation
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.tool.definition.ToolDefinition
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import reactor.core.publisher.Flux
import java.io.IOException

@RestController
@RequestMapping("/chat")
class ChatController(
//    private val vectorStore: VectorStore,
//    private val chatmodel :ChatModel,
    private val chatClient: ChatClient,
    private val chatModels: List<ChatModel>,
//    private val methodToolCallback: List<MethodToolCallback>,

    private val methodToolCallbackProvider: MethodToolCallbackProvider,
//    private val imageModel: ImageModel,
) {
//    methodToolCallbackProvider

    @GetMapping("gettools")
    @Operation(summary = "获取所有工具定义")
    fun gettools(): List<ToolDefinition> {
        val gettooldef = gettooldef()
        return gettooldef
    }

    @GetMapping(value = ["/ask"], produces = [MediaType
        .TEXT_EVENT_STREAM_VALUE])
    fun ask(text: String): SseEmitter {
        val emitter = SseEmitter(Long.MAX_VALUE)

        try {
            val stream: Flux<String> = chatClient.prompt().user {
                it.text(text)
            }.stream().content()

            stream.subscribe(
                { data ->
                    try {
                        emitter.send(SseEmitter.event().name("message").data(data))
                    } catch (e: IOException) {
                        emitter.completeWithError(e)
                    }
                },
                { error ->
                    emitter.completeWithError(error)
                },
                {
                    emitter.complete()
                }
            )
        } catch (e: Exception) {
            emitter.completeWithError(e)
        }

        return emitter
    }
}
