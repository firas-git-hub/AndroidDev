package com.example.randomdice

import Model.DiceResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var images = arrayOf<ImageView>()
    private var results = arrayListOf<DiceResult>()
    private lateinit var resultsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        images = arrayOf<ImageView>(findViewById<ImageView>(R.id.diceImage1),
                                    findViewById<ImageView>(R.id.diceImage2),
                                    findViewById<ImageView>(R.id.diceImage3),
                                    findViewById<ImageView>(R.id.diceImage4))

        resultsButton = findViewById(R.id.goToResultsBtn)
        resultsButton.setOnClickListener{
            val intent = Intent(this, DiceResultsDisplayActivity::class.java)
            intent.putExtra("diceResArr", results)
            startActivity(intent)
        }
    }

    private var oneByOneRollindex = 1

    fun saveDiceResults(result: String){
        results.add(DiceResult(result + "\r\n"))
    }

    fun setDiceImage(index: Int, diceNum: Int = -1){
        if(diceNum == -1){

            for (i in 0..(index-1)){
                images[i].setImageResource(R.drawable.dice_unknown)
            }
            if(index < 4){
                for(i in index..3){
                    images[i].setImageResource(R.drawable.locked)
                }
            }
        }

        else {
            if(diceNum == 1)
                images[index-1].setImageResource(R.drawable.dice_nb_1)
            if(diceNum == 2)
                images[index-1].setImageResource(R.drawable.dice_nb_2)
            if(diceNum == 3)
                images[index-1].setImageResource(R.drawable.dice_nb_3)
            if(diceNum == 4)
                images[index-1].setImageResource(R.drawable.dice_nb_4)
            if(diceNum == 5)
                images[index-1].setImageResource(R.drawable.dice_nb_5)
            if(diceNum == 6)
                images[index-1].setImageResource(R.drawable.dice_nb_6)
        }
    }

    fun rollAllDice(sender: View){
        var resultText = findViewById<TextView>(R.id.diceResultText)
        var nbOfDice = findViewById<TextView>(R.id.numbOfDiceText).text.toString().toInt()

        resultText.setText(" |#| ")
        for (index in 1..nbOfDice){
            var randomNum = (1..6).random()
            resultText.setText(resultText.text.toString() + "Roll" + index.toString() + ": " + randomNum.toString() + " |#| ")
            setDiceImage(index, randomNum)
        }
        saveDiceResults(resultText.text.toString())
    }

    fun rollDiceOneByOne(sender: View){
        var resultText = findViewById<TextView>(R.id.diceResultText)
        var nbOfDice = findViewById<TextView>(R.id.numbOfDiceText).text.toString().toInt()
        var randomNum = (1..6).random()
        var textRes = ""

        if(oneByOneRollindex == 1){
            setDiceImage(nbOfDice)
            resultText.setText("")
            textRes = " |#| Roll" + oneByOneRollindex.toString() + ": " + randomNum.toString() + " |#| "
            resultText.setText(textRes)
            setDiceImage(oneByOneRollindex, randomNum)
            if(oneByOneRollindex != nbOfDice)
                oneByOneRollindex++

            else
                saveDiceResults(textRes)
        }

        else if(oneByOneRollindex < nbOfDice){
            textRes = resultText.text.toString() + "Roll" + oneByOneRollindex.toString() + ": " + randomNum.toString() + " |#| "
            resultText.setText(textRes)
            setDiceImage(oneByOneRollindex, randomNum)
            if(oneByOneRollindex != nbOfDice)
                oneByOneRollindex++
        }

        else if(oneByOneRollindex == nbOfDice){
            textRes = resultText.text.toString() + "Roll" + oneByOneRollindex.toString() + ": " + randomNum.toString() + " |#| "
            resultText.setText(textRes)
            setDiceImage(oneByOneRollindex, randomNum)
            oneByOneRollindex = 1
            saveDiceResults(resultText.text.toString())
        }
    }

    fun addDice(sender: View){
        var initialNumTextView = findViewById<TextView>(R.id.numbOfDiceText)
        var initialNum = initialNumTextView.text.toString().toInt()
        var plusButt = findViewById<Button>(R.id.plusDice)
        var minusButt = findViewById<Button>(R.id.minusDice)

        if (initialNum < 4) {
            minusButt.isEnabled = true
            initialNumTextView.setText((initialNum + 1).toString())

            if(initialNumTextView.text.toString().toInt() == 4)
                plusButt.isEnabled = false
        }

        setDiceImage(initialNumTextView.text.toString().toInt())
    }

    fun removeDice(sender: View){
        var initialNumTextView = findViewById<TextView>(R.id.numbOfDiceText)
        var initialNum = initialNumTextView.text.toString().toInt()
        var minusButt = findViewById<Button>(R.id.minusDice)
        var plusButt = findViewById<Button>(R.id.plusDice)

        if (initialNum > 1) {
            plusButt.isEnabled = true
            initialNumTextView.setText((initialNum - 1).toString())

            if(initialNumTextView.text.toString().toInt() == 1)
                minusButt.isEnabled = false
        }

        setDiceImage(initialNumTextView.text.toString().toInt())
    }
}