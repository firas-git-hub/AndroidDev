package com.example.randomdice

import Model.DiceResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiceAdapter(val results: ArrayList<DiceResult>) : RecyclerView.Adapter<DiceAdapter.ViewHolder>() {

    class ViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView){
        val diceImageView = resultView.findViewById<ImageView>(R.id.diceImageDisplay)
        val diceResultText = resultView.findViewById<TextView>(R.id.diceRollResultDisplay)
        //val deleteButton = resultView.findViewById<Button>(R.id.deleteDiceResultRecord)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.dice_results_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result: DiceResult = results[position]
        holder.diceImageView.setImageResource(R.drawable.dice_unknown)
        holder.diceResultText.setText(result.roll)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun DeleteAll(){
        results.removeAll(results)
    }
}