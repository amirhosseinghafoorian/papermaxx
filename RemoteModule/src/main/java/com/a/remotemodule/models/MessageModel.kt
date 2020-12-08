package com.a.remotemodule.models

data class MessageModel (
    var id : String ,
    var text : String ,
    var type : MessageType ,
    var url : String? =null ,
    var time : String? = null
)