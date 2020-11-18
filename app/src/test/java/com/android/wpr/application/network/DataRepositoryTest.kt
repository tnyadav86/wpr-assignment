package com.android.wpr.application.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.wpr.application.model.DataRepository
import com.android.wpr.application.model.RemoteDataSource
import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.util.TestCoroutineRule
import com.android.wpr.application.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {

    private val remoteDataSource = mock(RemoteDataSource::class.java)

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun fetchDataWithNetworkConnection() = testCoroutineRule.testDispatcher.runBlockingTest {
        Mockito.doReturn(DataResult.Success(FeedResponseData(title = "", rows = arrayListOf())))
            .`when`(remoteDataSource)
            .fetchData()
        val dataRepository =
            DataRepository(remoteDataSource, testCoroutineRule.testDispatcherProvider)
        val observer = mock<Observer<DataResult<FeedResponseData>>>()

        dataRepository.dataResult.observeForever(observer)
        dataRepository.fetchData(true)
        Mockito.verify(remoteDataSource).fetchData()
        Mockito.verify(observer)
            .onChanged(DataResult.Loading())
        Mockito.verify(observer)
            .onChanged(DataResult.Success(FeedResponseData(title = "", rows = arrayListOf())))
        dataRepository.dataResult.removeObserver(observer)

    }

    @Test
    fun fetchDataWithoutNetworkConnection() = testCoroutineRule.testDispatcher.runBlockingTest {
        val dataRepository =
            DataRepository(remoteDataSource, testCoroutineRule.testDispatcherProvider)
        val observer = mock<Observer<DataResult<FeedResponseData>>>()
        dataRepository.dataResult.observeForever(observer)
        dataRepository.fetchData(false)
        Mockito.verify(observer)
            .onChanged(DataResult.Error(FeedDataErrorCode.INTERNET_CONNECTION_ERROR))
        dataRepository.dataResult.removeObserver(observer)

    }
}