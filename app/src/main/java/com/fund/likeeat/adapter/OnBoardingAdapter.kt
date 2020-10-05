package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.widget.OnBoardingItem
import kotlinx.android.synthetic.main.item_container_onboarding.view.*

class OnBoardingAdapter(val onBoardingItems: List<OnBoardingItem>): RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    var startButtonClickListener: OnStartButtonClickListener? = null

    class OnBoardingViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = view.onboarding_title
        val description = view.onboarding_description
        val mainImage = view.onboarding_main_image
        val mapTopImage = view.onboarding_map_top_image
        val mapOwnerImage = view.onboarding_map_owner
        val startButton = view.action_start
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_onboarding, parent, false))
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val item = onBoardingItems[position]

        holder.title.text = item.title
        holder.description.text = item.description
        holder.mainImage.setImageResource(item.mainImage)

        when(position) {
            0 -> {
                holder.mapTopImage.visibility = View.GONE
                holder.mapOwnerImage.visibility = View.GONE
                holder.startButton.setBackgroundResource(R.drawable.item_fill_black05_round)
            }
            1 -> {
                holder.mapTopImage.visibility = View.VISIBLE
                holder.mapOwnerImage.visibility = View.GONE
                holder.startButton.setBackgroundResource(R.drawable.item_fill_black05_round)
            }
            2 -> {
                holder.mapTopImage.visibility = View.GONE
                holder.mapOwnerImage.visibility = View.VISIBLE
                holder.startButton.setBackgroundResource(R.drawable.item_fill_black01_round)

                startButtonClickListener?.let { li ->
                    holder.startButton.setOnClickListener { li.onClick() }
                }
            }
        }
    }

    fun setOnStartButtonClickListener(li: OnStartButtonClickListener) {
        startButtonClickListener = li
    }
}

interface OnStartButtonClickListener {
    fun onClick()
}