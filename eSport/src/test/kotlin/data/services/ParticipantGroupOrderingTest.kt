package com.competra.data.services

import com.competra.data.response.orienteering.ParticipantGroupDetailResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class ParticipantGroupOrderingTest {

    private fun group(groupId: Long, title: String, gender: String? = null) = ParticipantGroupDetailResponse(
        groupId = groupId,
        title = title,
        gender = gender,
        maxParticipants = null,
        registeredCount = 0,
    )

    private fun sort(groups: List<ParticipantGroupDetailResponse>): List<String> = groups
        .sortedWith(
            compareBy<ParticipantGroupDetailResponse> { participantGroupSortPriority(it.title, it.gender) }
                .thenBy(nullsLast()) { extractAgeFromTitle(it.title) }
        )
        .map { it.title }

    @Test
    fun `groups are ordered male ascending then female ascending then others`() {
        val groups = listOf(
            group(1, "Ж35"),
            group(2, "Открытая"),
            group(3, "М21"),
            group(4, "Ж17"),
            group(5, "М17"),
            group(6, "М60+"),
        )
        assertEquals(listOf("М17", "М21", "М60+", "Ж17", "Ж35", "Открытая"), sort(groups))
    }

    @Test
    fun `explicit gender flag overrides title-based age bucket assignment`() {
        // Женская по флагу, хотя название начинается на "М" — не должна попасть в мужской блок.
        val groups = listOf(
            group(1, "М-ветераны", gender = "F"),
            group(2, "Ж21"),
        )
        assertEquals(listOf("Ж21", "М-ветераны"), sort(groups))
    }

    @Test
    fun `groups without a number stay in original relative order at the end of their gender block`() {
        val groups = listOf(
            group(1, "Мужчины (без возраста)"),
            group(2, "М21"),
            group(3, "Мужская группа Б"),
            group(4, "М17"),
        )
        assertEquals(
            listOf("М17", "М21", "Мужчины (без возраста)", "Мужская группа Б"),
            sort(groups)
        )
    }
}
