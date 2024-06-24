package com.mygdx.warships.game

import com.badlogic.gdx.Game
import com.mygdx.warships.screens.MainScreen

class Warships : Game() {

    override fun create() {
        this.setScreen(MainScreen(this))
    }

    override fun render() {
        super.render()
    }

    companion object {
        const val COLOR_R = 0.27f
        const val COLOR_G = 0.47f
        const val COLOR_B = 1f

        const val BUTTON_MARGIN_RATIO = 2f
        const val BUTTON_Y_RATIO = 1.5f
    }
}