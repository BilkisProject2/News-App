package com.example.newsapp.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.Resposritory.NewsRepository
import com.example.newsapp.RoomDatabase.ArticleDatabase
import com.example.newsapp.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding:ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var newsrespository = NewsRepository(ArticleDatabase(this))
        var NewsViewModelProviderFactory = NewsViewModelProviderFactory(application, newsrespository)
        newsViewModel = ViewModelProvider(this,NewsViewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.BottomnvigationView.setupWithNavController(navController)


    }
}