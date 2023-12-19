package com.example.newsapp.Ui.Fragment

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract
import android.provider.SyncStateContract.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.Ui.NewsActivity
import com.example.newsapp.Ui.NewsViewModel
import com.example.newsapp.Util.Contants
import com.example.newsapp.Util.Resources
import com.example.newsapp.databinding.FragmentHeadlinesBinding


class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlineError: CardView
    lateinit var binding: FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)

        itemHeadlineError = view.findViewById(R.id.itemheadlinerror)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = inflater.inflate(R.layout.item_error, null)
        retryButton = view.findViewById(R.id.imageView2)
        errorText = view.findViewById(R.id.textView)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupHeadlineRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment, bundle)
        }

        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success<*> -> {
                    hideErrorMessage()
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults / Contants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.headlinePage == totalPage
                        if (isLastPage) {
                            binding.recyclerView.setPadding(0, 0, 0, 0)
                        }

                    }
                }

                is Resources.Error<*> -> {
                    hideProgressBar()
                    response.message.let { message ->
                        Toast.makeText(activity, "SORRY ERROR :$message", Toast.LENGTH_LONG).show()
                        if (message != null) {
                            showErrorMessage(message)
                        }
                    }

                }

                is Resources.Loading<*> -> {
                    ShowProgressBar()
                }
            }
        })

        retryButton.setOnClickListener {
            newsViewModel.getHeadLine("us")
        }

    }

    var isError = false
    var isLoding = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationprogressbar.visibility = View.INVISIBLE
        isLoding = false
    }

    private fun ShowProgressBar() {
        binding.paginationprogressbar.visibility = View.VISIBLE
        isLoding = true
    }

    private fun hideErrorMessage() {
        itemHeadlineError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemHeadlineError.visibility = View.VISIBLE
        errorText.setText(message)
        isError = true
    }

    val scrollLisnter = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManger = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManger.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManger.childCount
            val totalItemCount = layoutManger.itemCount
            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoding && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeging = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Contants.QUERY_PAGE_SIZE
            val shouldPaginated =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeging && isTotalMoreThanVisible && isScrolling
            if (shouldPaginated) {
                newsViewModel.getHeadLine("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupHeadlineRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlinesFragment.scrollLisnter)
        }


    }

}