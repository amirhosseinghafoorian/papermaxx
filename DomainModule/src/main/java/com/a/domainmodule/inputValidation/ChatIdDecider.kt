package com.a.domainmodule.inputValidation

class ChatIdDecider {

    fun decideChatIdFormat(senderId: String, receiverId: String): String {
        var senderScore = 0
        var receiverScore = 0
        repeat(senderId.length) {
            senderScore += scoreSingleChar(senderId[it])
        }
        repeat(receiverId.length) {
            receiverScore += scoreSingleChar(receiverId[it])
        }
        return if (senderScore > receiverScore) {
            "$senderId:$receiverId"
        } else
            "$receiverId:$senderId"
    }

    private fun scoreSingleChar(char: Char): Int {
        return char.toInt()
    }
}