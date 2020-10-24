package com.android.wpr.application

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.wpr.application.network.DataResult
import com.android.wpr.application.network.FeedDataErrorCode
import com.android.wpr.application.util.gone
import com.android.wpr.application.util.visible
import com.android.wpr.application.viewmodel.MainActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var errorInfo: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AppRecyclerViewAdapter

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.progress_bar)
        errorInfo = findViewById(R.id.error_info)
        setSupportActionBar(toolbar)
        val mainActivityViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            MainActivityViewModel::class.java
        )

        swipeRefreshLayout=findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).apply {
            setOnRefreshListener {
                isRefreshing=true
                mainActivityViewModel.refreshData(false)

            }
        }
        recyclerView=findViewById<RecyclerView>(R.id.recycler_view).apply {
            viewAdapter = AppRecyclerViewAdapter()
            adapter=viewAdapter
        }

        mainActivityViewModel.feedResponseLiveData.observe(this, Observer {
            val result = it ?: return@Observer
            swipeRefreshLayout.isRefreshing=false
            when (result) {

                is DataResult.Success -> {
                    title = result.data.title
                    viewAdapter.updateItem(result.data.rows)
                    errorInfo.text =""
                    errorInfo.gone()

                }
                // error handling
                is DataResult.Error -> {
                    viewAdapter.updateItem(arrayListOf())
                    errorInfo.visible()
                    when (result.errorCode) {
                        FeedDataErrorCode.DATA_ERROR -> errorInfo.text =
                            getString(R.string.no_data_found)
                        FeedDataErrorCode.API_ERROR -> errorInfo.text =
                            getString(R.string.api_error)
                        FeedDataErrorCode.INTERNET_CONNECTION_ERROR -> errorInfo.text =
                            getString(R.string.internet_connection_error)

                    }

                }

            }
        })

        mainActivityViewModel.loadingStatus.observe(this, Observer {
            when (it) {
                true -> {
                    errorInfo.text = ""
                    errorInfo.gone()
                    progressBar.visible()
                }
                false -> progressBar.gone()

            }
        })

        // app will try to fetch data if last fetch was not success due to no network connection and network connection is available
        mainActivityViewModel.networkConnectionLiveData.observe(this, Observer {
            when (it) {
                true -> {
                    Log.e("wp", "connected")
                    when(val dataResult = mainActivityViewModel.feedResponseLiveData.value){
                        is DataResult.Error -> {
                            when (dataResult.errorCode) {
                                FeedDataErrorCode.INTERNET_CONNECTION_ERROR ->{
                                    mainActivityViewModel.refreshData(true)
                                }

                            }
                        }

                    }
                }
                false -> {
                    Log.e("wp", "not connected")
                }

            }
        })

    }
}