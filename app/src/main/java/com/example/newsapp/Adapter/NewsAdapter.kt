package com.example.newsapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.Model.Article
import com.example.newsapp.R

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_sample, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        val articleImage = holder.itemView.findViewById<ImageView>(R.id.iarticalmageView)
        val articleSource = holder.itemView.findViewById<TextView>(R.id.articalsource)
        val articleTitle = holder.itemView.findViewById<TextView>(R.id.articaltitle)
        val articleDescription = holder.itemView.findViewById<TextView>(R.id.articaldescription)
        val articleDateTime = holder.itemView.findViewById<TextView>(R.id.articaldateandtime)

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleSource.text = article.source?.name
            articleTitle.text=article.title
            articleDescription.text = article.description
            articleDateTime.text = article.publishedAt

            setOnClickListener{
                onItemClickListener?.let {
                    it(article)
                }
            }

        }
    }

    fun setOnItemClickListener(listener:(Article) -> Unit){
        onItemClickListener=listener
    }

}