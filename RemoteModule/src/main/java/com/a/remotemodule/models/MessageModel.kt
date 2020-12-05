package com.a.remotemodule.models

data class MessageModel (
    var id : String ,
    var text : String ,
    var type : MessageType ,
    var time : String? = null
)