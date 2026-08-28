package com.competra.data.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExtractAgeFromTitleTest {

    @Test
    fun `extracts age from standard notation`() {
        assertEquals(21, extractAgeFromTitle("М21"))
    }

    @Test
    fun `extracts age from title with plus sign`() {
        assertEquals(60, extractAgeFromTitle("M60+"))
    }

    @Test
    fun `extracts first number when multiple are present`() {
        assertEquals(17, extractAgeFromTitle("Ж17-1"))
    }

    @Test
    fun `returns null when title has no digits`() {
        assertNull(extractAgeFromTitle("Открытая группа"))
    }
}
