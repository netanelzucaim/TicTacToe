package com.example.tictactoe

import com.example.tictactoe.R.drawable.*
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Arrays


class GameActivity : AppCompatActivity() {
    var gameActive: Boolean = true

    // Player representation
    // 0 - X
    // 1 - O
    var activePlayer: Int = 0
    var gameState: IntArray = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    // State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    // put all win positions in a 2D array
    var winPositions: Array<IntArray> = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
    var counter: Int = 0


    // this function will be called every time a
    // players tap in an empty box of the grid
    fun playerTap(view: View) {
        val img = view as ImageView
        val tappedImage = img.tag.toString().toInt()

        // game reset function will be called
        // if someone wins or the boxes are full
        if (!gameActive) {

            gameReset()
            //Reset the counter
            counter = 0
        }

        // if the tapped image is empty
        if (gameState[tappedImage] === 2) {
            // increase the counter
            // after every tap
            counter++

            // check if its the last box
            if (counter == 9) {
                // reset the game
                gameActive = false
            }

            // mark this position
            gameState[tappedImage] = activePlayer

            // this will give a motion`
            // effect to the image
            img.translationY = -1000f

            // change the active player
            // from 0 to 1 or 1 to 0
            if (activePlayer == 0) {
                // set the image of x
                img.setImageResource(R.drawable.x)
                activePlayer = 1
                val status = findViewById<TextView>(R.id.status)

                // change the status
                status.text = "O's Turn - Tap to play"
            } else {
                // set the image of o
                img.setImageResource(R.drawable.circle)
                activePlayer = 0
                val status = findViewById<TextView>(R.id.status)

                // change the status
                status.text = "X's Turn - Tap to play"
            }
            img.animate().translationYBy(1000f).setDuration(300)
        }
        var flag = 0
        // Check if any player has won if counter is > 4 as min 5 taps are
        // required to declare a winner
        if (counter > 4) {
            for (winPosition in winPositions) {
                if (gameState[winPosition[0]] === gameState[winPosition[1]] && gameState[winPosition[1]] === gameState[winPosition[2]] && gameState[winPosition[0]] !== 2) {
                    flag = 1

                    // Somebody has won! - Find out who!
                    var winnerStr: String

                    // game reset function be called
                    gameActive = false
                    if (gameState[winPosition[0]] === 0) {
                        winnerStr = "X has won"
                    } else {
                        winnerStr = "O has won"
                    }
                    // Update the status bar for winner announcement
                    val status = findViewById<TextView>(R.id.status)
                    status.text = winnerStr
                }
            }
            // set the status if the match draw
            if (counter == 9 && flag == 0) {
                val status = findViewById<TextView>(R.id.status)
                status.text = "Match Draw"
            }
        }
    }

    // reset the game
    fun gameReset() {
        gameActive = true
        activePlayer = 0

        //set all position to Null
        Arrays.fill(gameState, 2)

        // remove all the images from the boxes inside the grid
        for (i in 1..9) {
            val resID = resources.getIdentifier("block$i", "id", packageName)
            val img = findViewById<ImageView>(resID)
            img.setImageResource(0)
        }

        val status = findViewById<TextView>(R.id.status)
        status.text = "X's Turn - Tap to play"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var rematchButton = findViewById<Button>(R.id.rematch)
        rematchButton.setOnClickListener {
            gameReset()
            //Reset the counter
            counter = 0
        }
        for (i in 1..9) {
            val resID = resources.getIdentifier("block$i", "id", packageName)
            findViewById<ImageView>(resID).setOnClickListener { playerTap(it) }
        }
    }
}