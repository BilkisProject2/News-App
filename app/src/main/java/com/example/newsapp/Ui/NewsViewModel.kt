package com.example.newsapp.Ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.newsapp.Model.Article
import com.example.newsapp.Model.NewsResponse
import com.example.newsapp.Resposritory.NewsRepository
import com.example.newsapp.Util.Resources
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import java.util.Locale.IsoCountryCode

class NewsViewModel(app: Application, val newsRepository: NewsRepository) : AndroidViewModel(app) {

    val headlines: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var headlinePage = 1
    var headlineResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    init {
        getHeadLine("us")
    }

    fun getHeadLine(countryCode: String)= viewModelScope.launch {
        headlinesInternet(countryCode)
    }

    fun searchnews(seachquery: String)= viewModelScope.launch {
       searchNewsInternet(seachquery)
    }

    private fun handleHeadlinesResponses(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinePage++
                if (headlineResponse == null) {
                    headlineResponse = resultResponse
                } else {
                    val oldArticle = headlineResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticle?.addAll(newArticles)
                }
                return Resources.Success(headlineResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (headlineResponse == null || newSearchQuery!= oldSearchQuery) {
                    searchNewsPage=1
                    oldSearchQuery=newSearchQuery
                    headlineResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticle = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticle?.addAll(newArticles)
                }
                return Resources.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    fun addToFavouites(article: Article)=viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getFavouitesNews()=newsRepository.getFavNews()


    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun internetConnection(context:Context):Boolean
    {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }

            }?:false
        }
    }

    private suspend fun headlinesInternet(countryCode: String){
        headlines.postValue(Resources.Loading())
        try {
        if (internetConnection(this.getApplication())){
            val response=newsRepository.getHeadlines(countryCode,headlinePage)
            headlines.postValue(handleHeadlinesResponses(response))
        }else{
            headlines.postValue(Resources.Error("NO INTERNET CONNECTION"))
        }
        }catch (t:Throwable){
            when(t){
                is IOException ->headlines.postValue(Resources.Error("Unable to connect"))
                else -> headlines.postValue(Resources.Error("No signal"))
            }

        }
    }

    private suspend fun searchNewsInternet(searchQuery: String){
        newSearchQuery = searchQuery
        searchNews.postValue(Resources.Loading())
        try {
            if (internetConnection(this.getApplication())){
                val response=newsRepository.searchNews(searchQuery,searchNewsPage )
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resources.Error("NO INTERNET CONNECTION"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException ->searchNews.postValue(Resources.Error("Unable to connect"))
                else -> searchNews.postValue(Resources.Error("No signal"))
            }

        }
    }
}