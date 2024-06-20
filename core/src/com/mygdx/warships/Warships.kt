package com.mygdx.warships

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.warships.Assets.battleshipTexture
import com.mygdx.warships.Assets.carrierTexture
import com.mygdx.warships.Assets.cruiserTexture
import com.mygdx.warships.Assets.destroyerTexture
import com.mygdx.warships.model.Coordinates
import com.mygdx.warships.model.Ship
import java.util.Random

class Warships : ApplicationAdapter() {

    private lateinit var camera: OrthographicCamera
    private lateinit var font: BitmapFont
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var destroyerSprite: Sprite
    private lateinit var cruiserSprite: Sprite
    private lateinit var battleshipSprite: Sprite
    private lateinit var carrierSprite: Sprite
    private lateinit var field: ShapeRenderer
    private var startPoint = 0f
    private var cellSize = 0f
    private var fieldLength = 0f

    private val ships = mutableListOf<Ship>()

    private val filledCells = mutableSetOf<Coordinates>()

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        field = ShapeRenderer()
        font = BitmapFont()
        spriteBatch = SpriteBatch()

        Assets.load()

        destroyerSprite = Sprite(destroyerTexture)
        cruiserSprite = Sprite(cruiserTexture)
        battleshipSprite = Sprite(battleshipTexture)
        carrierSprite = Sprite(carrierTexture)

        positionShips()
    }

    override fun render() {
        Gdx.gl.glClearColor(0.27f, 0.47f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()

        field.setProjectionMatrix(camera.combined)
        spriteBatch.setProjectionMatrix(camera.combined)

        renderGrid()
        ships.forEach { ship ->
            renderShip(ship)
        }
    }

    private fun positionShips() {
        //destroyers
        for (i in 0 until 4) {
            positionShip(
                getRandomPosition(),
                getRandomPosition(),
                DESTROYER_SIZE,
                isHorizontal(),
                destroyerSprite
            )
        }

        //cruisers
        for (i in 0 until 3) {
            positionShip(
                getRandomPosition(),
                getRandomPosition(),
                CRUISER_SIZE,
                isHorizontal(),
                cruiserSprite
            )
        }

        //battleships
        for (i in 0 until 2) {
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
        field.begin(ShapeRenderer.ShapeType.Line)

        field.color = Color.WHITE

        fieldLength = Gdx.graphics.height / GRID_HEIGHT_RATIO
        cellSize = fieldLength / GRID_SIZE
        val screenMargin = Gdx.graphics.height / GRID_MARGIN_RATIO
        startPoint = (Gdx.graphics.height - screenMargin)

        for (i in cellSize.toInt()..fieldLength.toInt() step cellSize.toInt()) {
            field.line(i.toFloat(), startPoint, i.toFloat(), fieldLength)
            field.line(startPoint, i.toFloat(), fieldLength, i.toFloat())
        }

        destroyerSprite.setSize(cellSize, cellSize)
        cruiserSprite.setSize(cellSize, cellSize * 2)
        battleshipSprite.setSize(cellSize, cellSize * 3)
        carrierSprite.setSize(cellSize, cellSize * 4)

        field.end()
    }

    private fun renderShip(ship: Ship) {
        spriteBatch.begin()

        ship.apply {
            sprite.rotation = if (isHorizontal) {
                sprite.setOrigin(sprite.width / 2, sprite.width / 2)
                ROTATION
            } else {
                0f
            }
            sprite.setPosition(
                getCellCoordinate(coordinates.col),
                getCellCoordinate(coordinates.row)
            )
            sprite.draw(spriteBatch)
        }

        spriteBatch.end()
    }

    private fun getCellCoordinate(coordinate: Int): Float {
        return coordinate * cellSize
    }

    private fun getRandomPosition(): Int {
        return Random().nextInt(MAX_COORDINATE - 1) + 1
    }

    private fun isHorizontal(): Boolean {
        return Random().nextBoolean()
    }

    override fun dispose() {
        field.dispose()
        Assets.dispose()
    }

    companion object {
        private const val GRID_SIZE = 11
        private const val GRID_HEIGHT_RATIO = 1.2f
        private const val GRID_MARGIN_RATIO = 1.1f
        private const val MAX_COORDINATE = 10
        private const val ROTATION = 270f

        private const val DESTROYER_SIZE = 1
        private const val CRUISER_SIZE = 2
        private const val BATTLESHIP_SIZE = 3
        private const val CARRIER_SIZE = 4
    }
}
