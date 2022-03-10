package com.example.spandemo.reply

import android.util.Log
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.spandemo.R


class CommentAdapter(data:MutableList<Comment>, moreClickListener: (View, Comment) -> Unit): BaseNodeAdapter() {
    init {
        addFullSpanNodeProvider(CommentProvider(COMMENT_TYPE, R.layout.reply_item_layout))
        addNodeProvider(ReplyProvider(REPLY_TYPE, R.layout.comment_item_layout))
        addNodeProvider(ReplyMoreProvider(REPLY_MORE_TYPE, R.layout.reply_more_item_layout, moreClickListener))
    }
    companion object {
        const val COMMENT_TYPE = 0
        const val REPLY_TYPE = 1
        const val REPLY_MORE_TYPE = 2
    }
    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when(data[position]) {
            is Comment -> COMMENT_TYPE
            is Reply -> REPLY_TYPE
            is ReplyMore -> REPLY_MORE_TYPE
            else -> -1
        }
    }

    override fun convert(holder: BaseViewHolder, item: BaseNode) {
        super.convert(holder, item)
    }

    class CommentProvider(override val itemViewType: Int, override val layoutId: Int) : BaseNodeProvider() {
        override fun convert(helper: BaseViewHolder, item: BaseNode) {
            (helper.itemView as? TextView)?.text = (item as Comment).content
        }

        override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
            super.onClick(helper, view, data, position)
        }
    }

    class ReplyProvider(override val itemViewType: Int, override val layoutId: Int) : BaseNodeProvider() {
        override fun convert(helper: BaseViewHolder, item: BaseNode) {
            (helper.itemView as? TextView)?.text = (item as Reply).content
        }

        override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
            super.onClick(helper, view, data, position)
        }
    }

    class ReplyMoreProvider(override val itemViewType: Int, override val layoutId: Int,
                            private val clickListener: (View, Comment) -> Unit) : BaseNodeProvider() {
        override fun convert(helper: BaseViewHolder, item: BaseNode) {
            helper.getViewOrNull<TextView>(R.id.reply_more)?.text = (item as ReplyMore).content
        }

        override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
            super.onClick(helper, view, data, position)
            clickListener.invoke(view, (data as ReplyMore).parent as Comment)
        }

    }

}
