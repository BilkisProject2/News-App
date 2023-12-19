package com.example.newsapp.Ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.Ui.NewsActivity
import com.example.newsapp.Ui.NewsViewModel
import com.example.newsapp.databinding.FragmentArticleBinding


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
   val args:ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentArticleBinding.bind(view)

        newsViewModel =(activity as NewsActivity).newsViewModel
        val article= args.article

        binding.Webview.apply {
            webViewClient = WebViewClient()
            article.url.let {
                loadUrl(it!!)
            }
        }

        binding.floatingActionButton2.setOnClickListener {
            newsViewModel.addToFavouites(article)
        }







    }




 

}