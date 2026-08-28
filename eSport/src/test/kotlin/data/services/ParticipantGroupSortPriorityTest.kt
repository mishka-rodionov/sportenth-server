package com.competra.data.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParticipantGroupSortPriorityTest {

    @Test
    fun `explicit M gender takes priority over title`() {
        assertEquals(0, participantGroupSortPriority(title = "Женщины 21", gender = "M"))
    }

    @Test
    fun `explicit F gender takes priority over title`() {
        assertEquals(1, participantGroupSortPriority(title = "Мужчины 21", gender = "F"))
    }

    @Test
    fun `unknown explicit gender falls into others bucket`() {
        assertEquals(2, participantGroupSortPriority(title = "М21", gender = "MIXED"))
    }

    @Test
    fun `title starting with M letter is inferred as male when gender is missing`() {
        assertEquals(0, participantGroupSortPriority(title = "М21", gender = null))
    }

    @Test
    fun `title starting with lowercase m letter is inferred as male when gender is missing`() {
        assertEquals(0, participantGroupSortPriority(title = "м21", gender = null))
    }

    @Test
    fun `title starting with F letter is inferred as female when gender is missing`() {
        assertEquals(1, participantGroupSortPriority(title = "Ж35", gender = null))
    }

    @Test
    fun `title without recognizable prefix falls into others bucket`() {
        assertEquals(2, participantGroupSortPriority(title = "Открытая группа", gender = null))
    }

    @Test
    fun `mixed group title is not misclassified as male despite containing letter M mid-word`() {
        assertEquals(2, participantGroupSortPriority(title = "Смешанная", gender = null))
    }

    @Test
    fun `male groups sort before female groups sort before others`() {
        val groups = listOf(
            Triple("Открытая", null, 2),
            Triple("Ж21", null, 1),
            Triple("М21", null, 0),
        )
        val sorted = groups.sortedBy { (title, gender, _) -> participantGroupSortPriority(title, gender) }
        assertEquals(listOf("М21", "Ж21", "Открытая"), sorted.map { it.first })
        assertTrue(sorted.map { it.third } == listOf(0, 1, 2))
    }
}
