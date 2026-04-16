package com.example.mindfulinha.entity

import java.util.Date

class Diary (public val idx: Int, public val contents: String, public val createdAt: Date) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Diary) return false

        if (idx != other.idx) return false
        if (contents != other.contents) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idx
        result = 31 * result + contents.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}