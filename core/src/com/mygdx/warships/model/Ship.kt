package com.mygdx.warships.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

data class Ship(
    val coordinates: Coordinates,
    val length: Int,
    val isHorizontal: Boolean,
    val sprite: Sprite
)
