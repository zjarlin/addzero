package com.addzero.kmp.demo

import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route

import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor


@Composable
@Route("组件示例", "富文本")
fun RichTextDemo() {
    val richTextState = rememberRichTextState()
// Add link after selection.
    val html = "<p><b>Compose Rich Editor</b></p>"
//    richTextState.setHtml(html)

    //设置markdown
    richTextState.setMarkdown(html)

    // Add link after selection.
//    richTextState.addLink(
//        text = "Compose Rich Editor",
//        url = "https://github.com/MohamedRejeb/Compose-Rich-Editor"
//    )

    // Toggle code span.

    RichTextEditor(
        state = richTextState,
    )
    richTextState.toggleCodeSpan()

}

