package com.mygdx.warships

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

object Assets {
    lateinit var destroyerTexture: Texture
    lateinit var cruiserTexture: Texture
    lateinit var battleshipTexture: Texture
    lateinit var carrierTexture: Texture

    fun load() {
        destroyerTexture = Texture(Gdx.files.internal(DESTROYER_TEXTURE))
        cruiserTexture = Texture(Gdx.files.internal(CRUISER_TEXTURE))
        battleshipTexture = Texture(Gdx.files.internal(BATTLESHIP_TEXTURE))
        carrierTexture = Texture(Gdx.files.internal(CARRIER_TEXTURE))
    }

    fun dispose() {
        destroyerTexture.dispose()
        cruiserTexture.dispose()
        battleshipTexture.dispose()
        carrierTexture.dispose()
    }

    private const val DESTROYER_TEXTURE = "destroyer.png"
    private const val CRUISER_TEXTURE = "cruiser.png"
    private const val BATTLESHIP_TEXTURE = "battleship.png"
    private const val CARRIER_TEXTURE = "carrier.png"
}