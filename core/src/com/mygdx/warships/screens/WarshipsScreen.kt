package com.mygdx.warships.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.warships.game.Assets
import com.mygdx.warships.game.Assets.loadWarshipsAssets
import com.mygdx.warships.game.Assets.skin
import com.mygdx.warships.game.InputController
import com.mygdx.warships.game.Warships
import com.mygdx.warships.game.Warships.Companion.COLOR_B
import com.mygdx.warships.game.Warships.Companion.COLOR_G
import com.mygdx.warships.game.Warships.Companion.COLOR_R
import com.mygdx.warships.model.Coordinates
import com.mygdx.warships.model.Ship
import java.util.Random

class WarshipsScreen(private val game: Warships) : Screen {

    private lateinit var camera: OrthographicCamera
    private lateinit var font: BitmapFont
    private lateinit var gridSpriteBatch: SpriteBatch
    private lateinit var textSpriteBatch: SpriteBatch
    private lateinit var shipsSpriteBatch: SpriteBatch
    private lateinit var destroyerSprite: Sprite
    private lateinit var cruiserSprite: Sprite
    private lateinit var battleshipSprite: Sprite
    private lateinit var carrierSprite: Sprite
    private lateinit var grid: ShapeRenderer

    private lateinit var stage: Stage
    private lateinit var autoButton: Button
    private lateinit var backButton: Button

    private var cellSize = 0f
    private var fieldLength = 0f
    private val coordinatePoints = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

    private val ships = mutableListOf<Ship>()

    private val filledCells = mutableSetOf<Coordinates>()

    private lateinit var gridBounds: Rectangle

    override fun show() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        loadWarshipsAssets()

        grid = ShapeRenderer()
        font = BitmapFont()
        gridSpriteBatch = SpriteBatch()
        textSpriteBatch = SpriteBatch()
        shipsSpriteBatch = SpriteBatch()
        grid.setProjectionMatrix(camera.combined)
        gridSpriteBatch.setProjectionMatrix(camera.combined)
        textSpriteBatch.setProjectionMatrix(camera.combined)
        shipsSpriteBatch.setProjectionMatrix(camera.combined)

        stage = Stage()
        Gdx.input.inputProcessor = stage

        grid.color = Color.WHITE

        fieldLength = Gdx.graphics.height / GRID_HEIGHT_RATIO
        cellSize = fieldLength / GRID_SIZE

        font.apply {
            region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            data.setScale(cellSize / FONT_SCALE_RATIO)
        }

        gridBounds = Rectangle(cellSize, cellSize, cellSize * GRID_SIZE, cellSize * MAX_COORDINATE)

        setupSprites()
        positionShips()
        drawButtons()

        Gdx.input.inputProcessor = InputMultiplexer(InputController(), stage)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(COLOR_R, COLOR_G, COLOR_B, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        renderGrid()
        ships.forEach { ship ->
            renderShip(ship)
        }
        onGridTouch()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        grid.dispose()
        Assets.dispose()
        stage.dispose()
        gridSpriteBatch.dispose()
        shipsSpriteBatch.dispose()
        textSpriteBatch.dispose()
    }

    private fun setupSprites() {
        destroyerSprite = Sprite(Assets.destroyerTexture)
        cruiserSprite = Sprite(Assets.cruiserTexture)
        battleshipSprite = Sprite(Assets.battleshipTexture)
        carrierSprite = Sprite(Assets.carrierTexture)
        destroyerSprite.setSize(cellSize, cellSize)
        cruiserSprite.setSize(cellSize, cellSize * CRUISER_SIZE)
        battleshipSprite.setSize(cellSize, cellSize * BATTLESHIP_SIZE)
        carrierSprite.setSize(cellSize, cellSize * CARRIER_SIZE)
    }

    private fun drawButtons() {
        autoButton = TextButton(AUTO_TEXT, skin)
        val buttonX =
            Gdx.graphics.width / BUTTON_HORIZONTAL_MARGIN_RATIO - autoButton.width / BUTTON_MARGIN_RATIO
        autoButton.apply {
            setPosition(
                buttonX,
                Gdx.graphics.height / BUTTON_MARGIN_RATIO
            )
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    positionShips()
                }
            })

        }
        stage.addActor(autoButton)

        backButton = TextButton(BACK_TEXT, skin)
        backButton.apply {
            setPosition(
                buttonX,
                Gdx.graphics.height / BUTTON_MARGIN_RATIO - autoButton.height * Warships.BUTTON_Y_RATIO
            )
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.screen = MainScreen(game)
                }
            })

        }
        stage.addActor(backButton)
    }

    private fun positionShips() {
        ships.clear()
        filledCells.clear()
        //destroyers
        for (i in 0 until DESTROYERS_NUMBER) {
            positionShip(
                getRandomPosition(),
                getRandomPosition(),
                DESTROYER_SIZE,
                isHorizontal(),
                destroyerSprite
            )
        }

        //cruisers
        for (i in 0 until CRUISERS_NUMBER) {
            positionShip(
                getRandomPosition(),
                getRandomPosition(),
                CRUISER_SIZE,
                isHorizontal(),
                cruiserSprite
            )
        }

        //battleships
        for (i in 0 until BATTLESHIPS_NUMBER) {
            positionShip(
                getRandomPosition(),
                getRandomPosition(),
                BATTLESHIP_SIZE,
                isHorizontal(),
                battleshipSprite
            )
        }

        //carrier
        positionShip(
            getRandomPosition(),
            getRandomPosition(),
            CARRIER_SIZE,
            isHorizontal(),
            carrierSprite
        )
    }

    private fun positionShip(
        startCol: Int,
        startRow: Int,
        length: Int,
        isHorizontal: Boolean,
        sprite: Sprite
    ): Boolean {
        if (isHorizontal) {
            for (i in 0 until length) {
                if (startCol + length > GRID_SIZE) {
                    return positionShip(
                        getRandomPosition(),
                        getRandomPosition(),
                        length,
                        isHorizontal(),
                        sprite
                    )
                }
                if (filledCells.contains(Coordinates(startCol + i, startRow))) {
                    return positionShip(
                        getRandomPosition(),
                        getRandomPosition(),
                        length,
                        isHorizontal(),
                        sprite
                    )
                }
            }
            for (i in 0 until length) {
                val cellCoordinates = Coordinates(startCol + i, startRow)
                filledCells.add(cellCoordinates)
            }
        } else {
            for (i in 0 until length) {
                if (startRow + length > GRID_SIZE) {
                    return positionShip(
                        getRandomPosition(),
                        getRandomPosition(),
                        length,
                        isHorizontal(),
                        sprite
                    )
                }
                if (filledCells.contains(Coordinates(startCol, startRow + i))) {
                    return positionShip(
                        getRandomPosition(),
                        getRandomPosition(),
                        length,
                        isHorizontal(),
                        sprite
                    )
                }
            }
            for (i in 0 until length) {
                val cellCoordinates = Coordinates(startCol, startRow + i)
                filledCells.add(cellCoordinates)
            }
        }
        ships.add(Ship(Coordinates(startCol, startRow), length, isHorizontal, sprite))
        return true
    }

    private fun renderGrid() {
        grid.begin(ShapeRenderer.ShapeType.Line)
        gridSpriteBatch.begin()

        for (i in MIN_COORDINATE..GRID_SIZE) {
            //vertical lines
            grid.line(cellSize * i, cellSize, cellSize * i, fieldLength)
            //horizontal lines
            grid.line(cellSize, cellSize * i, fieldLength, cellSize * i)
        }

        //water
        for (x in MIN_COORDINATE..MAX_COORDINATE) {
            for (y in MIN_COORDINATE..MAX_COORDINATE) {
                gridSpriteBatch.draw(
                    Assets.waterTexture,
                    cellSize * x,
                    cellSize * y,
                    cellSize,
                    cellSize
                )
            }
        }

        gridSpriteBatch.end()
        grid.end()

        //coordinate labels
        textSpriteBatch.begin()
        for (i in MIN_COORDINATE..MAX_COORDINATE) {
            font.draw(
                textSpriteBatch,
                coordinatePoints[i - 1],
                cellSize / TEXT_SIZE_RATIO,
                cellSize * (i + 1)
            )
            font.draw(textSpriteBatch, i.toString(), cellSize * i, fieldLength + cellSize)
        }
        textSpriteBatch.end()
    }

    private fun renderShip(ship: Ship) {
        shipsSpriteBatch.begin()

        ship.apply {
            sprite.rotation = if (isHorizontal) {
                sprite.setOrigin(sprite.width / SHIP_SIZE_RATIO, sprite.width / SHIP_SIZE_RATIO)
                ROTATION
            } else {
                0f
            }
            sprite.setPosition(
                getCellCoordinate(coordinates.x),
                getCellCoordinate(coordinates.y)
            )
            sprite.draw(shipsSpriteBatch)
        }

        shipsSpriteBatch.end()
    }

    private fun setupShaders(x: Float, y: Float) {
        if (!Assets.waterShader.isCompiled) {
            Gdx.app.log(LOG_SHADER, Assets.waterShader.log)
            Gdx.app.exit()
        }
        if (!Assets.shipsShader.isCompiled) {
            Gdx.app.log(LOG_SHADER, Assets.shipsShader.log)
            Gdx.app.exit()
        }

        Assets.waterShader.apply {
            gridSpriteBatch.shader = this
            bind()
            setUniformf(UNIFORM_CIRCLE_CENTER, x, y)
            setUniformf(UNIFORM_CIRCLE_RADIUS, MASK_SIZE)
            setUniformf(UNIFORM_MASK_COLOR, Color.NAVY)
        }

        Assets.shipsShader.apply {
            shipsSpriteBatch.shader = this
            bind()
            setUniformf(UNIFORM_CIRCLE_CENTER, x, y)
            setUniformf(UNIFORM_CIRCLE_RADIUS, MASK_SIZE)
            setUniformi(UNIFORM_TEXTURE, 0)
            setUniformi(UNIFORM_MASK_TEXTURE, 1)
        }

        Assets.shipHdTexture.apply {
            bind(1)
            bind(0)
        }
    }

    private fun onGridTouch() {
        if (Gdx.input.isTouched) {
            if (gridBounds.contains(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))) {
                val touchPos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(touchPos)
                setupShaders(touchPos.x, touchPos.y)
            }
        }
    }

    private fun getCellCoordinate(coordinate: Int): Float {
        return coordinate * cellSize
    }

    private fun getRandomPosition(): Int {
        return Random().nextInt(MAX_COORDINATE - MIN_COORDINATE) + MIN_COORDINATE
    }

    private fun isHorizontal(): Boolean {
        return Random().nextBoolean()
    }

    companion object {
        private const val GRID_SIZE = 11
        private const val MIN_COORDINATE = 1
        private const val MAX_COORDINATE = 10
        private const val GRID_HEIGHT_RATIO = 1.1f
        private const val ROTATION = 270f

        private const val FONT_SCALE_RATIO = 17

        private const val BUTTON_HORIZONTAL_MARGIN_RATIO = 1.5f
        private const val BUTTON_MARGIN_RATIO = 2f

        private const val DESTROYERS_NUMBER = 4
        private const val CRUISERS_NUMBER = 3
        private const val BATTLESHIPS_NUMBER = 2
        private const val DESTROYER_SIZE = 1
        private const val CRUISER_SIZE = 2
        private const val BATTLESHIP_SIZE = 3
        private const val CARRIER_SIZE = 4
        private const val SHIP_SIZE_RATIO = 2

        private const val TEXT_SIZE_RATIO = 4

        private const val AUTO_TEXT = "Auto"
        private const val BACK_TEXT = "Back"

        private const val LOG_SHADER = "Shader"
        private const val MASK_SIZE = 900 / 4f
        private const val UNIFORM_CIRCLE_CENTER = "u_circleCenter"
        private const val UNIFORM_CIRCLE_RADIUS = "u_circleRadius"
        private const val UNIFORM_MASK_COLOR = "u_maskColor"
        private const val UNIFORM_TEXTURE = "u_texture"
        private const val UNIFORM_MASK_TEXTURE = "u_maskTexture"
    }
}