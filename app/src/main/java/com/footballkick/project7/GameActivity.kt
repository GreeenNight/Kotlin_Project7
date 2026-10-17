package com.footballkick.project7

import android.annotation.SuppressLint
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Half.toFloat
import android.util.Log
import android.view.Display
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
import kotlin.properties.Delegates
import kotlin.random.Random
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class GameActivity : AppCompatActivity() {
    private lateinit var mixedCards:List<String>
    private lateinit var drawableMixedCards:MutableList<Int>
    private lateinit var drawablePcCards:MutableList<Int>
    private var cardIndex by Delegates.notNull<Int>()
    private var drawnCards by Delegates.notNull<Int>()
    private var playerHandPoints by Delegates.notNull<Int>()
    private var pcHandPoints by Delegates.notNull<Int>()
    private var isWin by Delegates.notNull<Boolean>()
    private lateinit var textView:TextView
    private lateinit var textView2:TextView
    private lateinit var textView6:TextView
    private lateinit var button:Button
    private lateinit var button2:Button
    private lateinit var button3:Button
    private lateinit var middleCard:ImageView
    private lateinit var card2:ImageView
    private lateinit var card3:ImageView
    private lateinit var pcMiddleCard:ImageView
    private lateinit var pcCard2:ImageView
    private lateinit var pcCard3:ImageView
    private var positionOnceLeft by Delegates.notNull<Float>()
    private var positionTwiceLeft by Delegates.notNull<Float>()
    private var positionOnceRight by Delegates.notNull<Float>()
    private var positionTwiceRight by Delegates.notNull<Float>()
    private var middle by Delegates.notNull<Float>()
    private var x by Delegates.notNull<Float>()
    private var pcY by Delegates.notNull<Float>()
    private var playerY by Delegates.notNull<Float>()
    private var bet by Delegates.notNull<Int>()
    private var playerPoints by Delegates.notNull<Int>()
    private var pcPoints by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        val cantDrawText: TextView = findViewById(R.id.cantDraw)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        val textView3:TextView = findViewById(R.id.textView3)
        val textView4:TextView = findViewById(R.id.textView4)
        textView6 = findViewById(R.id.textView6)

        middleCard=findViewById(R.id.card1)
        card2=findViewById(R.id.card2)
        card3=findViewById(R.id.card3)
        pcMiddleCard=findViewById(R.id.pcCard1)
        pcCard2=findViewById(R.id.pcCard2)
        pcCard3=findViewById(R.id.pcCard3)
        val radio_group:RadioGroup=findViewById(R.id.bet_radio_group)

        val defaultDisplay =
            DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)
        val displayContext = createDisplayContext(defaultDisplay!!)

        val displayWidth = displayContext.resources.displayMetrics.widthPixels
        val displayHeight = displayContext.resources.displayMetrics.heightPixels

        val cards=mutableListOf("2c","2d","2h","2s","3c","3d","3h","3s","4c","4d","4h","4s",
            "5c","5d","5h","5s","6c","6d","6h","6s","7c","7d","7h","7s","8c","8d","8h","8s","9c",
            "9d","9h","9s","10c","10d","10h","10s","11c","11d","11h","11s","12c","12d","12h","12s",
            "13c","13d","13h","13s","14c","14d","14h","14s")

        var bank=0
        bet=10
        pcPoints=1000
        playerPoints=500
        isWin=true
        drawnCards=0
        pcHandPoints=0
        playerHandPoints=0
        cardIndex=0

        mixedCards = cards.shuffled()
        drawableMixedCards=MutableList(52){ 0 }
        drawablePcCards=MutableList(3){ 0 }


        Handler(Looper.getMainLooper()).postDelayed({
            x=middleCard.x
            pcY=pcMiddleCard.y
            playerY=middleCard.y
            positionOnceLeft=((displayWidth*0.38)-(pcMiddleCard.width/2)).toFloat()
            positionTwiceLeft=((displayWidth*0.28)-(pcMiddleCard.width/2)).toFloat()
            positionOnceRight=((displayWidth*0.62)-(pcMiddleCard.width/2)).toFloat()
            positionTwiceRight=((displayWidth*0.72)-(pcMiddleCard.width/2)).toFloat()
            middle=((displayWidth*0.5)-(pcMiddleCard.width/2)).toFloat()
            button2.isClickable=false

        }, 40)

        button.setOnClickListener {

                button2.isClickable = true
                textView.visibility = View.VISIBLE
            var id: Int = radio_group.checkedRadioButtonId
            val radio: RadioButton = findViewById(id)


            if (drawnCards==0)
            bet=radio.text.toString().toInt()
            bank=bet*2
            textView6.visibility=View.VISIBLE
            textView6.text="Bank: $bank"

                if (playerPoints - bet >= 0 && pcPoints - bet >= 0) {
                    if (drawnCards == 0) {

                        playerPoints -= bet
                        textView3.text = "Your points: $playerPoints"
                        pcPoints -= bet
                        textView4.text = "PC points: $pcPoints"
                    }
                    if (drawnCards <= 2) {
                        draw()
                    } else {
                        cantDraw(cantDrawText)
                    }
                    drawnCards += 1
                } else {
                    if (drawnCards == 0) {
                        if (playerPoints - bet < 0)
                        bet = playerPoints
                        else if (pcPoints - bet < 0)
                            bet = pcPoints

                        bank=bet*2
                        textView6.visibility=View.VISIBLE
                        textView6.text = "Bank: $bank"
                        playerPoints -= bet
                        textView3.text = "Your points: $playerPoints"
                        pcPoints -= bet
                        textView4.text = "PC points: $pcPoints"
                    }
                    if (drawnCards <= 2) {
                        draw()
                    } else {
                        cantDraw(cantDrawText)
                    }
                    drawnCards += 1
                }
        }

        button2.setOnClickListener {
            compare()
            winLose(cantDrawText)
            if (isWin){
                playerPoints += bet*2
                textView3.text="Your points: $playerPoints"
                textView4.text="PC points: $pcPoints"
            } else {
                pcPoints += bet*2
                textView4.text="PC points: $pcPoints"
                textView3.text="Your points: $playerPoints"
            }
        }
//        radio_group.setOnCheckedChangeListener(
//            RadioGroup.OnCheckedChangeListener { group, checkedId ->
//                val radio: RadioButton = findViewById(checkedId)
////                Toast.makeText(applicationContext," On checked change :"+" ${radio.text}",
////                    Toast.LENGTH_SHORT).show()
//            })
    }
    fun draw(){
        when (mixedCards[cardIndex]) {
            "2c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_2
            "2d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_2
            "2h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_2
            "2s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_2
            "3c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_3
            "3d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_3
            "3h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_3
            "3s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_3
            "4c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_4
            "4d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_4
            "4h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_4
            "4s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_4
            "5c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_5
            "5d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_5
            "5h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_5
            "5s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_5
            "6c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_6
            "6d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_6
            "6h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_6
            "6s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_6
            "7c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_7
            "7d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_7
            "7h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_7
            "7s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_7
            "8c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_8
            "8d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_8
            "8h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_8
            "8s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_8
            "9c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_9
            "9d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_9
            "9h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_9
            "9s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_9
            "10c" -> drawableMixedCards[cardIndex]=R.drawable.of_clubs_10
            "10d" -> drawableMixedCards[cardIndex]=R.drawable.of_diamonds_10
            "10h" -> drawableMixedCards[cardIndex]=R.drawable.of_hearts_10
            "10s" -> drawableMixedCards[cardIndex]=R.drawable.of_spades_10
            "11c" -> drawableMixedCards[cardIndex]=R.drawable.jack_of_clubs
            "11d" -> drawableMixedCards[cardIndex]=R.drawable.jack_of_diamonds
            "11h" -> drawableMixedCards[cardIndex]=R.drawable.jack_of_hearts
            "11s" -> drawableMixedCards[cardIndex]=R.drawable.jack_of_spades
            "12c" -> drawableMixedCards[cardIndex]=R.drawable.queen_of_clubs
            "12d" -> drawableMixedCards[cardIndex]=R.drawable.queen_of_diamonds
            "12h" -> drawableMixedCards[cardIndex]=R.drawable.queen_of_hearts
            "12s" -> drawableMixedCards[cardIndex]=R.drawable.queen_of_spades
            "13c" -> drawableMixedCards[cardIndex]=R.drawable.king_of_clubs
            "13d" -> drawableMixedCards[cardIndex]=R.drawable.king_of_diamonds
            "13h" -> drawableMixedCards[cardIndex]=R.drawable.king_of_hearts
            "13s" -> drawableMixedCards[cardIndex]=R.drawable.king_of_spades
            "14c" -> drawableMixedCards[cardIndex]=R.drawable.ace_of_clubs
            "14d" -> drawableMixedCards[cardIndex]=R.drawable.ace_of_diamonds
            "14h" -> drawableMixedCards[cardIndex]=R.drawable.ace_of_hearts
            "14s" -> drawableMixedCards[cardIndex]=R.drawable.ace_of_spades
        }

        if(cardIndex%2==0){
            val playerCardPoints=mixedCards[cardIndex].dropLast(1).toInt()
            playerHandPoints += playerCardPoints
            textView.text="Your hand power: $playerHandPoints"


                    when (drawnCards) {
                        0 -> {
                            middleCard.setImageResource(drawableMixedCards[cardIndex])
                            middleCard.visibility = View.VISIBLE
                        }
                        1 -> {
                            middleCard.x = positionOnceLeft
                            card2.setImageResource(drawableMixedCards[cardIndex])
                            card2.visibility = View.VISIBLE
                            card2.x = positionOnceRight
                        }
                        2 -> {
                            middleCard.x = positionTwiceLeft
                            card2.x = middle
                            card3.setImageResource(drawableMixedCards[cardIndex])
                            card3.visibility = View.VISIBLE
                            card3.x = positionTwiceRight
                        }
                    }

            cardIndex+=1
            draw()
        }else{
            val pcCardPoints=mixedCards[cardIndex].dropLast(1).toInt()
            pcHandPoints+=pcCardPoints
            drawablePcCards[drawnCards]=drawableMixedCards[cardIndex]
            when(drawnCards){

                0->pcMiddleCard.visibility=View.VISIBLE
                1->{pcMiddleCard.x=positionOnceLeft
                    pcCard2.visibility=View.VISIBLE
                    pcCard2.x=positionOnceRight}
                2->{pcMiddleCard.x=positionTwiceLeft
                    pcCard2.x=middle
                    pcCard3.visibility=View.VISIBLE
                    pcCard3.x=positionTwiceRight}
            }
            cardIndex+=1
        }
    }
    fun cantDraw(textView: TextView){
        button.isClickable=false
        textView.visibility= View.VISIBLE
        textView.text="Can't draw more cards"
        textView.setBackgroundResource(R.color.red)

        Handler(Looper.getMainLooper()).postDelayed({
            textView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_out))
        }, 300)
        Handler(Looper.getMainLooper()).postDelayed({
            textView.visibility=View.INVISIBLE
            button.isClickable=true
        }, 700)
    }
    fun winLose(textView: TextView){
        if (playerHandPoints>pcHandPoints) {
            isWin = true
            textView.text="You won!"
            textView.setBackgroundResource(R.color.green)
        }
        else {

            isWin = false
            textView.text="You lost"
            textView.setBackgroundResource(R.color.red)
        }
        textView.visibility= View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            textView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_out))
        }, 600)
        Handler(Looper.getMainLooper()).postDelayed({
            textView.visibility=View.INVISIBLE
            button.isClickable=true
        }, 1000)
    }


    fun compare(){

        textView2.text="PC hand power: $pcHandPoints"
        textView2.visibility=View.VISIBLE
        pcMiddleCard.setImageResource(drawablePcCards[0])
        pcCard2.setImageResource(drawablePcCards[1])
        pcCard3.setImageResource(drawablePcCards[2])
        button.visibility=View.INVISIBLE
        button2.visibility=View.INVISIBLE
        button3.visibility=View.VISIBLE
        button3.setOnClickListener{
            if (playerPoints > 0 && pcPoints > 0) {
            button.visibility=View.VISIBLE
            button2.visibility=View.VISIBLE
            button3.visibility=View.GONE
            pcHandPoints=0
            playerHandPoints=0
            textView.text="Your hand power: $playerHandPoints"
            textView2.visibility=View.GONE
            middleCard.x=middle
            pcMiddleCard.visibility=View.INVISIBLE
            pcMiddleCard.setImageResource(R.drawable.card_back)
            pcMiddleCard.x=middle
            pcCard2.visibility=View.INVISIBLE
            pcCard2.setImageResource(R.drawable.card_back)
            pcCard3.visibility=View.INVISIBLE
            pcCard3.setImageResource(R.drawable.card_back)
            middleCard.visibility=View.INVISIBLE
            card2.visibility=View.INVISIBLE
            card3.visibility=View.INVISIBLE
            textView.visibility=View.GONE
            drawnCards=0

            cardIndex=0
            mixedCards=mixedCards.shuffled()
            button2.isClickable=false
                textView6.visibility=View.GONE
            }else{
                val dialogFragment = EndFragment()
                dialogFragment.show(supportFragmentManager, "EndFragment")
            }
        }
    }
}