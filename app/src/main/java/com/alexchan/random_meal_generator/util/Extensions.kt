package com.alexchan.random_meal_generator.util

inline fun <T, R> T?.guard(block: () -> R): T {
    if (this == null) {
        block()
        throw IllegalArgumentException("guard block must return from enclosing function")
    }

    return this
}
