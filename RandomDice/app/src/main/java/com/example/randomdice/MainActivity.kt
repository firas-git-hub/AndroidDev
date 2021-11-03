package com.example.randomdice

import Model.RandomDiceService
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val fineLocationPermissionRQ = 101
    val coarseLocationPermissionRQ = 102

    private var images = arrayOf<ImageView>()
    private lateinit var resultsButton: Button
    private lateinit var changePPImageView: ImageView
    private lateinit var goToMapActButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        images = arrayOf(
            findViewById(R.id.diceImage1),
            findViewById(R.id.diceImage2),
            findViewById(R.id.diceImage3),
            findViewById(R.id.diceImage4)
        )
        buttonBinding()
        if(GalCamActivity.selectedImg.imgDrawable != null){
            changePPImageView.setImageDrawable(GalCamActivity.selectedImg.imgDrawable)
        }
    }

    fun buttonBinding(){
        goToMapActButton = findViewById(R.id.goToMapActBut)
        goToMapActButton.setOnClickListener {
            ChkPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "Fine location", fineLocationPermissionRQ)
            ChkPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION, "Coarse location", coarseLocationPermissionRQ)
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        resultsButton = findViewById(R.id.goToResultsBtn)
        resultsButton.setOnClickListener {
            val intent = Intent(this, DiceResultsDisplayActivity::class.java)
            startActivity(intent)
        }

        changePPImageView = findViewById<ImageView>(R.id.ProfilePicture)
        changePPImageView.setOnClickListener {
            val intent = Intent(this, GalCamActivity::class.java)
            startActivity(intent)
        }
    }

    private var oneByOneRollIndex = 1

    fun saveDiceResults(result: String) {
        /*Results.results.add(DiceResult(result + "\r\n"))*/
        //Calling the POST HTTP request
        RandomDiceService.postResults(result)
    }

    fun setDiceImage(index: Int, diceNum: Int = -1) {
        if (diceNum == -1) {

            for (i in 0..(index - 1)) {
                images[i].setImageResource(R.drawable.dice_unknown)
            }
            if (index < 4) {
                for (i in index..3) {
                    images[i].setImageResource(R.drawable.locked)
                }
            }
        } else {
            if (diceNum == 1)
                images[index - 1].setImageResource(R.drawable.dice_nb_1)
            if (diceNum == 2)
                images[index - 1].setImageResource(R.drawable.dice_nb_2)
            if (diceNum == 3)
                images[index - 1].setImageResource(R.drawable.dice_nb_3)
            if (diceNum == 4)
                images[index - 1].setImageResource(R.drawable.dice_nb_4)
            if (diceNum == 5)
                images[index - 1].setImageResource(R.drawable.dice_nb_5)
            if (diceNum == 6)
                images[index - 1].setImageResource(R.drawable.dice_nb_6)
        }
    }

    fun rollAllDice(sender: View) {
        val resultText = findViewById<TextView>(R.id.diceResultText)
        val nbOfDice = findViewById<TextView>(R.id.numbOfDiceText).text.toString().toInt()

        resultText.setText(" |#| ")
        for (index in 1..nbOfDice) {
            val randomNum = Random.nextInt(1, 6)
            resultText.setText(resultText.text.toString() + "Roll" + index.toString() + ": " + randomNum.toString() + " |#| ")
            setDiceImage(index, randomNum)
        }
        saveDiceResults(resultText.text.toString())
    }

    fun rollDiceOneByOne(sender: View) {
        val resultText = findViewById<TextView>(R.id.diceResultText)
        val nbOfDice = findViewById<TextView>(R.id.numbOfDiceText).text.toString().toInt()
        val randomNum = Random.nextInt(1, 6)
        var textRes = ""

        if (oneByOneRollIndex == 1) {
            setDiceImage(nbOfDice)
            resultText.setText("")
            textRes =
                " |#| Roll" + oneByOneRollIndex.toString() + ": " + randomNum.toString() + " |#| "
            resultText.setText(textRes)
            setDiceImage(oneByOneRollIndex, randomNum)
            if (oneByOneRollIndex != nbOfDice)
                oneByOneRollIndex++
            else
                saveDiceResults(textRes)
        } else if (oneByOneRollIndex < nbOfDice) {
            textRes =
                resultText.text.toString() + "Roll" + oneByOneRollIndex.toString() + ": " + randomNum.toString() + " |#| "
            resultText.setText(textRes)
            setDiceImage(oneByOneRollIndex, randomNum)
            if (oneByOneRollIndex != nbOfDice)
                oneByOneRollIndex++
        } else if (oneByOneRollIndex == nbOfDice) {
            textRes =
                resultText.text.toString() + "Roll" + oneByOneRollIndex.toString() + ": " + randomNum.toString() + " |#| "
            resultText.setText(textRes)
            setDiceImage(oneByOneRollIndex, randomNum)
            oneByOneRollIndex = 1
            saveDiceResults(resultText.text.toString())
        }
    }

    fun addDice(sender: View) {
        val initialNumTextView = findViewById<TextView>(R.id.numbOfDiceText)
        val initialNum = initialNumTextView.text.toString().toInt()
        val plusButt = findViewById<Button>(R.id.plusDice)
        val minusButt = findViewById<Button>(R.id.minusDice)

        if (initialNum < 4) {
            minusButt.isEnabled = true
            initialNumTextView.setText((initialNum + 1).toString())

            if (initialNumTextView.text.toString().toInt() == 4)
                plusButt.isEnabled = false
        }

        setDiceImage(initialNumTextView.text.toString().toInt())
    }

    fun removeDice(sender: View) {
        val initialNumTextView = findViewById<TextView>(R.id.numbOfDiceText)
        val initialNum = initialNumTextView.text.toString().toInt()
        val minusButt = findViewById<Button>(R.id.minusDice)
        val plusButt = findViewById<Button>(R.id.plusDice)

        if (initialNum > 1) {
            plusButt.isEnabled = true
            initialNumTextView.setText((initialNum - 1).toString())

            if (initialNumTextView.text.toString().toInt() == 1)
                minusButt.isEnabled = false
        }

        setDiceImage(initialNumTextView.text.toString().toInt())
    }

    private fun ChkPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        applicationContext,
                        "$name permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (requestCode) {
            fineLocationPermissionRQ -> innerCheck("Fine location")
            coarseLocationPermissionRQ -> innerCheck("Coarse location")
        }
    }
}