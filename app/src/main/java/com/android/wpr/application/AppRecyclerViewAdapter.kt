package com.android.wpr.application

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.wpr.application.model.data.FeedItem
import com.android.wpr.application.util.loadImage
import kotlinx.android.synthetic.main.item_feed.view.*


class AppRecyclerViewAdapter : RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder>() {

    private var feedItemList: List<FeedItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    fun updateItem(eventList: List<FeedItem>) {
        this.feedItemList = eventList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = feedItemList[position]
        holder.onBind(item)


    }

    override fun getItemCount(): Int = feedItemList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun onBind(item:FeedItem){
            itemView.apply {
                tvTitle.text = item.title
                tvDescription.text = item.description
                image.loadImage(item.imageHref)
            }

        }
    }
}