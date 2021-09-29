package com.example.randomdice

import Model.DiceResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiceResultsDisplayActivity : AppCompatActivity(), DiceAdapter.onClickListener{

    private lateinit var diceRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_results_display)

        val intent = intent
        diceRecyclerView = findViewById<RecyclerView>(R.id.diceResultsRecyclerView) as RecyclerView
        diceRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        diceRecyclerView.adapter = DiceAdapter()

        findViewById<Button>(R.id.deleteAllBtn).setOnClickListener {
            Results.results.clear()
            diceRecyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onClick(position: Int) {
        TODO("Not yet implemented")
    }
}