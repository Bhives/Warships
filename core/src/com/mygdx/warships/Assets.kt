package com.mygdx.warships

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.ui.Skin

object Assets {
    lateinit var waterTexture: Texture
    lateinit var destroyerTexture: Texture
    lateinit var cruiserTexture: Texture
    lateinit var battleshipTexture: Texture
    lateinit var carrierTexture: Texture
    lateinit var skin: Skin
    lateinit var shipHdTexture: Texture
    lateinit var waterShader: ShaderProgram
    lateinit var shipsShader: ShaderProgram

    fun load() {
        waterTexture = Texture(Gdx.files.internal(WATER_TEXTURE))
        destroyerTexture = Texture(Gdx.files.internal(DESTROYER_TEXTURE))
        cruiserTexture = Texture(Gdx.files.internal(CRUISER_TEXTURE))
        battleshipTexture = Texture(Gdx.files.internal(BATTLESHIP_TEXTURE))
        carrierTexture = Texture(Gdx.files.internal(CARRIER_TEXTURE))
        skin = Skin(Gdx.files.internal(SKIN_PATH))
        shipHdTexture = Texture(Gdx.files.internal(SHIP_MASK_TEXTURE))
        val vertexShader = Gdx.files.internal(VERTEX_SHADER)
        val waterFragmentShader = Gdx.files.internal(WATER_FRAGMENT_SHADER)
        val shipsFragmentShader = Gdx.files.internal(SHIPS_FRAGMENT_SHADER)
        ShaderProgram.pedantic = false
        waterShader = ShaderProgram(vertexShader, waterFragmentShader)
        shipsShader = ShaderProgram(vertexShader, shipsFragmentShader)
    }

    fun dispose() {
        waterTexture.dispose()
        destroyerTexture.dispose()
        cruiserTexture.dispose()
        battleshipTexture.dispose()
        carrierTexture.dispose()
        skin.dispose()
        shipHdTexture.dispose()
        shipsShader.dispose()
    }

    private const val WATER_TEXTURE = "textures/water.png"
    private const val DESTROYER_TEXTURE = "textures/destroyer.png"
    private const val CRUISER_TEXTURE = "textures/cruiser.png"
    private const val BATTLESHIP_TEXTURE = "textures/battleship.png"
    private const val CARRIER_TEXTURE = "textures/carrier.png"
    private const val SKIN_PATH = "skin/glassy-ui.json"
    private const val SHIP_MASK_TEXTURE = "textures/ship-hd.png"
    private const val VERTEX_SHADER = "shaders/vertex.glsl"
    private const val WATER_FRAGMENT_SHADER = "shaders/water-fragment.glsl"
    private const val SHIPS_FRAGMENT_SHADER = "shaders/ships-fragment.glsl"
}