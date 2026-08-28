package com.competra.data.services

import kotlin.test.Test
import kotlin.test.assertEquals

class NormalizeParticipantGroupGenderTest {

    @Test
    fun `M is normalized to MALE`() {
        assertEquals("MALE", normalizeParticipantGroupGender("M"))
    }

    @Test
    fun `F is normalized to FEMALE`() {
        assertEquals("FEMALE", normalizeParticipantGroupGender("F"))
    }

    @Test
    fun `null stays null`() {
        assertEquals(null, normalizeParticipantGroupGender(null))
    }

    @Test
    fun `unrecognized value passes through unchanged`() {
        assertEquals("MIXED", normalizeParticipantGroupGender("MIXED"))
    }
}
