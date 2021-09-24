package com.example.randomdice

import Model.DiceResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper

class DiceAdapter(private val results: ArrayList<DiceResult>) : RecyclerView.Adapter<DiceAdapter.ViewHolder>() {


    class ViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView){
        val diceImageView: ImageView = resultView.findViewById<ImageView>(R.id.diceImageDisplay)
        val diceResultText: TextView = resultView.findViewById<TextView>(R.id.diceRollResultDisplay)
        val deleteOneBtn: Button = resultView.findViewById<Button>(R.id.deleteOneResult)
        val binderHelper: ViewBinderHelper = ViewBinderHelper()
        val swipeRevealLayout: SwipeRevealLayout = resultView.findViewById<SwipeRevealLayout>(R.id.diceSwiperevealLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.dice_results_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result: DiceResult = results[position]
        holder.binderHelper.bind(holder.swipeRevealLayout, result.roll)
        holder.diceImageView.setImageResource(R.drawable.dice_unknown)
        holder.diceResultText.text = result.roll
        holder.deleteOneBtn.setOnClickListener {
            deleteResult(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    interface onClickListener {
        fun onClick(position: Int)
    }

    fun deleteResult(position: Int){
        results.removeAt(position)
        this.notifyDataSetChanged()
    }
}