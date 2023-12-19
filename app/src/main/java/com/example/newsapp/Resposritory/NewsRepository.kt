package com.example.newsapp.Resposritory

import androidx.room.Query
import com.example.newsapp.API.RetrofitInstances
import com.example.newsapp.Model.Article
import com.example.newsapp.RoomDatabase.ArticleDatabase

class NewsRepository(var db:ArticleDatabase) {

    suspend fun getHeadlines(countryCode:String, pageNumber: Int) = RetrofitInstances.api.getHeadlines(countryCode, pageNumber)
    suspend fun searchNews(searchQuery: String,pageNumber: Int)= RetrofitInstances.api.searchForNews(searchQuery, pageNumber)
suspend fun insert(article: Article)=db.getArticleDao().insert(article)
fun getFavNews()= db.getArticleDao().getAllArticle()
suspend fun deleteArticle(article: Article)= db.getArticleDao().deleteArticle(article)
}