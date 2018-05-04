package com.skysurf.kryoxem.randomcolorgame

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import java.util.*

class MenuActivity : AppCompatActivity(), ColorPickerDialogListener {
    private val TIMER: Long = 4000

    private lateinit var mButtonNew : Button
    private lateinit var mButtonOCP : Button
    private lateinit var mButtonValidate : Button
    private lateinit var mTitle : TextView
    private lateinit var mBackground : View
    private lateinit var mResult : View
    private lateinit var mResultExpected : View
    private lateinit var mResultGiven : View

    private var isPlaying = false

    private var colorPicked: Int = Color.BLACK
    private var color: Int = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        mButtonNew = findViewById(R.id.activity_menu_new)
        mButtonOCP = findViewById(R.id.activity_menu_open_colorpicker)
        mButtonValidate = findViewById(R.id.activity_menu_validate)
        mTitle = findViewById(R.id.activity_menu_title)
        mBackground = findViewById(R.id.activity_menu_background)
        mResult = findViewById(R.id.activity_menu_result)
        mResultExpected = findViewById(R.id.activity_menu_result_expected)
        mResultGiven = findViewById(R.id.activity_menu_result_given)

        update()
    }

    override fun onDialogDismissed(dialogId: Int) {
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        setBackgroundColor(color)
        colorPicked = color
    }

    fun newGame(view: View) {
        val randomColor = randomColor()
        setBackgroundColor(randomColor)
        color = randomColor

        val handler = Handler()
        handler.postDelayed({
            beginGame()
        }, TIMER)
    }

    fun validate(view: View) {
        endGame()
    }

    fun openColorPicker(view: View) {
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setColor(colorPicked)
                .show(this)
    }

    private fun update() {
        setBackgroundColor(Color.WHITE)
        if (isPlaying) {
            mButtonOCP.isEnabled = true
            mButtonOCP.visibility = View.VISIBLE
            mButtonValidate.isEnabled = true
            mTitle.text = getString(R.string.menu_prompt)
            mResult.visibility = View.INVISIBLE
        } else {
            mButtonOCP.visibility = View.GONE
            mButtonValidate.isEnabled = false
            mButtonOCP.isEnabled = false
            mTitle.text = getString(R.string.menu_remember)
            mResult.visibility = View.VISIBLE
        }
    }

    private fun beginGame() {
        mBackground.setBackgroundColor(Color.WHITE)
        isPlaying = true
        update()
    }

    private fun endGame() {
        Toast.makeText(this, "Diff: ${calculateDiffColors(color, colorPicked)}", Toast.LENGTH_LONG).show()
        mResultExpected.setBackgroundColor(color)
        mResultGiven.setBackgroundColor(colorPicked)
        isPlaying = false
        update()
        colorPicked = Color.BLACK
    }

    private fun calculateDiffColors(color1: Int, color2: Int): Double {
        val c1r = Color.red(color1)
        val c1g = Color.green(color1)
        val c1b = Color.blue(color1)

        val c2r = Color.red(color2)
        val c2g = Color.green(color2)
        val c2b = Color.blue(color2)

        Log.d("Machin", "Color 1: ${c1r}R${c1g}G${c1b}B, Color 2: ${c2r}R${c2g}G${c2b}B")

        val rmean = (c1r + c2r) / 2
        val r = c1r - c2r
        val g = c1g - c2g
        val b = c1b - c2b
        val weightR = 2 + rmean / 256
        val weightG = 4.0
        val weightB = 2 + (255 - rmean) / 256
        return Math.sqrt(weightR * r.toDouble() * r.toDouble() + weightG * g.toDouble() * g.toDouble() + weightB * b.toDouble() * b.toDouble())
    }

    private fun setBackgroundColor(color: Int) {
        mBackground.setBackgroundColor(color)
    }

    private fun randomColor(): Int {
        val r = Random()
        return Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256))
    }
}
