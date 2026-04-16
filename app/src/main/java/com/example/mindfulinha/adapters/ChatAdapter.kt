package com.example.mindfulinha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindfulinha.R
import com.example.mindfulinha.models.Message

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text
        // 메시지가 사용자로부터 보낸 것인지에 따라 말풍선의 배경색을 변경
        if (message.isSentByUser) {
            holder.messageTextView.setBackgroundResource(R.drawable.sent_message_bubble)
            // 사용자 메시지는 오른쪽 정렬
            holder.messageTextView.layoutParams = (holder.messageTextView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginEnd = 0
                marginStart = 50
            }
        } else {
            holder.messageTextView.setBackgroundResource(R.drawable.received_message_bubble)
            // 상대방 메시지는 왼쪽 정렬
            holder.messageTextView.layoutParams = (holder.messageTextView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginStart = 0
                marginEnd = 50
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
