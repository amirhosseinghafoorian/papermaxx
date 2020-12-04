package com.a.papermaxx.model

import com.a.papermaxx.general.MessageType

data class MessageModel(
    var id: String,
    var text: String,
    var type: MessageType,
    var time: String? = null
)