package com.example.spandemo.reply

import com.chad.library.adapter.base.entity.node.BaseNode

data class Comment(
    var title: String = "",
    var content: String = "",
    var user: String = "",
    override var childNode: MutableList<BaseNode>? = null
): BaseNode()

data class Reply(
    var content: String = "",
    var user: String = "",
    override val childNode: MutableList<BaseNode>? = null,
    var parent: BaseNode
    ) :BaseNode()

data class ReplyMore(
    var content: String = "", override val childNode: MutableList<BaseNode>? = null,
    var parent: BaseNode
): BaseNode()