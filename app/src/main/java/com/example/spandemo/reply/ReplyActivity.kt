package com.example.spandemo.reply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.example.spandemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.math.max

class ReplyActivity : AppCompatActivity() {
    val data = mutableListOf<Comment>()

    private val commentAdapter by lazy {
        CommentAdapter(data) { view, comment ->
            add(comment)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        var rv: RecyclerView?=null
        findViewById<RecyclerView>(R.id.rv_comment)?.run {
            adapter = commentAdapter
            rv = this
            layoutManager = LinearLayoutManager(context)
        }
        data.addAll(MutableList(10) {
            Comment(content = "置顶---$it")
        })
        data.addAll(generaData(20))

        commentAdapter.setList(data)

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.IO) {
                delay(5000)
            }

            withContext(Dispatchers.Main) {
                val size = commentAdapter.data.size
                val removeList = commentAdapter.data.subList(10, commentAdapter.itemCount)
                val removeSize = removeList.size

                commentAdapter.data.removeAll(removeList)
//            val addList = generaData(5)
//            commentAdapter.notifyDataSetChanged()
                commentAdapter.notifyItemRangeChanged(10, removeSize)
                            commentAdapter.addData(generaData(5))
//
//            rv?.scrollToPosition(10)
            }
        }

    }

    private fun add(comment: Comment) {
        lifecycleScope.launchWhenStarted {
//            delay(2000)
            val newData = MutableList<BaseNode>(5) {index ->
                Reply(content = "第 ${(comment.childNode?.size?:0) + index} 条回复", parent = comment)
            }
            newData.add(ReplyMore(parent = comment, content = "加载更多"))
            comment.childNode?.last()?.let {
                commentAdapter.nodeRemoveData(comment, it)
            }
            commentAdapter.nodeAddData(comment, comment.childNode?.size?:0, newData)
        }
    }

    private fun generaData(count: Int) : MutableList<Comment> {
        val comments = mutableListOf<Comment>()
        repeat(count) {commentIndex->
            val comment = Comment(content = "第 $commentIndex 条评论：")
            comment.childNode = mutableListOf()

            repeat(8) {
                val reply = Reply(content = "第 $it 条回复", parent = comment)
                comment.childNode?.add(reply)
            }
            comment.childNode?.add(ReplyMore(parent = comment, content = "加载更多"))
            comments.add(comment)
        }
        return comments
    }
}