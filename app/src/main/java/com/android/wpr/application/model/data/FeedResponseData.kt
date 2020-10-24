package com.android.wpr.application.model.data

data class FeedResponseData(
    var rows: List<FeedItem>,
    val title: String?
)
