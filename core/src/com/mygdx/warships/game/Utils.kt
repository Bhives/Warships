package com.mygdx.warships.game

import com.mygdx.warships.model.Coordinates

fun Set<Coordinates>.isTakenOrNear(x: Int, y: Int): Boolean {
    return (contains(Coordinates(x, y)) ||
            contains(Coordinates(x + 1, y)) ||
            contains(Coordinates(x - 1, y)) ||
            contains(Coordinates(x, y + 1)) ||
            contains(Coordinates(x, y - 1))) ||
            contains(Coordinates(x + 1, y + 1)) ||
            contains(Coordinates(x - 1, y - 1)) ||
            contains(Coordinates(x + 1, y - 1)) ||
            contains(Coordinates(x - 1, y + 1))
}