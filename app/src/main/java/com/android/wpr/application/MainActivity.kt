package com.android.wpr.application

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.wpr.application.network.DataResult
import com.android.wpr.application.network.FeedDataErrorCode
import com.android.wpr.application.util.gone
import com.android.wpr.application.util.visible
import com.android.wpr.application.viewmodel.MainActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var viewAdapter: AppRecyclerViewAdapter

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val mainActivityViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            MainActivityViewModel::class.java
        )

        swipeRefreshLayout.apply {
            setOnRefreshListener {
                isRefreshing=true
                mainActivityViewModel.fetchData()

            }
        }
        recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            viewAdapter = AppRecyclerViewAdapter().also {
                adapter=it
            }
        }

        mainActivityViewModel.feedResponseLiveData.observe(this, Observer {
            val result = it ?: return@Observer

            when (result) {
                is DataResult.Loading -> {
                    errorInfo.apply {
                        text = ""
                        gone()
                    }
                    if (!swipeRefreshLayout.isRefreshing) {
                        //No need to show progressbar in middle as swipeRefreshLayout already showing loader
                        progressBar.apply {
                            visible()
                        }
                    }

                }

                is DataResult.Success -> {
                    progressBar.apply {
                        gone()
                    }
                    swipeRefreshLayout.apply {
                        isRefreshing = false
                    }
                    title=result.data.title
                    viewAdapter.apply {
                        updateItem(result.data.rows)
                    }

                }
                // error handling
                is DataResult.Error -> {
                    progressBar.apply {
                        gone()
                    }
                    swipeRefreshLayout.apply {
                        isRefreshing = false
                    }
                    //Show error on UI
                    val errorMessage: String = when (result.errorCode) {
                        FeedDataErrorCode.DATA_ERROR -> getString(R.string.no_data_found)
                        FeedDataErrorCode.API_ERROR -> getString(R.string.api_error)
                        FeedDataErrorCode.INTERNET_CONNECTION_ERROR -> getString(R.string.internet_connection_error)


                    }
                    if (viewAdapter.itemCount == 0) {
                        // if list is empty then showing error message in text view. i.e. user can miss toast message
                        errorInfo.apply {
                            text = errorMessage
                            visible()
                        }
                    } else {
                        // if list has data then showing error message in Toast.
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }


                }

            }
        })
        mainActivityViewModel.fetchData()

        // app will try to fetch data if last fetch was not success due to no network connection and network connection is available
        mainActivityViewModel.networkConnectionLiveData.observe(this, {
            when (it) {
                true -> {
                    Log.e("wp", "connected")
                    when(val dataResult = mainActivityViewModel.feedResponseLiveData.value){
                        is DataResult.Error -> {
                            when (dataResult.errorCode) {
                                FeedDataErrorCode.INTERNET_CONNECTION_ERROR ->{
                                    mainActivityViewModel.fetchData()
                                }
                                else -> {}
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