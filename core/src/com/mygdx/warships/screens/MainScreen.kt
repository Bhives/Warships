package com.mygdx.warships.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.warships.game.Assets
import com.mygdx.warships.game.Warships
import com.mygdx.warships.game.Warships.Companion.BUTTON_MARGIN_RATIO
import com.mygdx.warships.game.Warships.Companion.BUTTON_Y_RATIO

class MainScreen(private val game: Warships) : Screen {

    private lateinit var playButton: Button
    private lateinit var exitButton: Button
    private lateinit var stage: Stage

    override fun show() {
        Assets.loadCommonAssets()
        stage = Stage()
        Gdx.input.inputProcessor = stage
        drawButtons()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(Warships.COLOR_R, Warships.COLOR_G, Warships.COLOR_B, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
    }

    private fun drawButtons() {
        playButton = TextButton(PLAY, Assets.skin)
        val buttonX =
            Gdx.graphics.width / BUTTON_MARGIN_RATIO - playButton.width / BUTTON_MARGIN_RATIO
        playButton.apply {
            setPosition(buttonX, Gdx.graphics.height / BUTTON_MARGIN_RATIO)
            addListener(object : ClickListener() {

                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.screen = WarshipsScreen(game)
                }
            })
        }
        stage.addActor(playButton)

        exitButton = TextButton(EXIT, Assets.skin)
        exitButton.apply {
            setPosition(
                buttonX,
                Gdx.graphics.height / BUTTON_MARGIN_RATIO - playButton.height * BUTTON_Y_RATIO
            )
            addListener(object : ClickListener() {

                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    Gdx.app.exit()
                }
            })
        }
        stage.addActor(exitButton)
    }

    companion object {
        private const val PLAY = "Play"
        private const val EXIT = "Exit"
    }
}