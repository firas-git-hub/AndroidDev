package com.example.randomdice

import Model.DiceResult
import Model.RandomDiceService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper

class DiceAdapter() : RecyclerView.Adapter<DiceAdapter.ViewHolder>() {


    class ViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView){
        val diceImageView: ImageView = resultView.findViewById<ImageView>(R.id.diceImageDisplay)
        val diceResultText: TextView = resultView.findViewById<TextView>(R.id.diceRollResultDisplay)
        val deleteOneBtn: Button = resultView.findViewById<Button>(R.id.deleteOneResult)
        val binderHelper: ViewBinderHelper = ViewBinderHelper()
        val swipeRevealLayout: SwipeRevealLayout = resultView.findViewById<SwipeRevealLayout>(R.id.diceSwiperevealLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dice_results_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binderHelper.bind(holder.swipeRevealLayout, Results.results[position].roll)
        holder.diceImageView.setImageResource(R.drawable.dice_unknown)
        holder.diceResultText.text = Results.results[position].roll
        holder.deleteOneBtn.setOnClickListener {
            deleteResult(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return Results.results.size
    }

    interface onClickListener {
        fun onClick(position: Int)
    }

    private fun deleteResult(position: Int){
        RandomDiceService.deleteResult(this, Results.results[position].id, position)
    }
}
object Results{
    var results: ArrayList<DiceResult> = ArrayList()
}