package com.competra.data.response.orienteering

data class ParticipantGroupDetailResponse(
    val groupId: Long,
    val title: String,
    val maxParticipants: Int?,
    val registeredCount: Int,
    val distanceId: Long? = null,
    val distanceName: String? = null,
    val distanceLengthMeters: Int? = null,
    val distanceClimbMeters: Int? = null,
    val distanceControlsCount: Int? = null,
    val distanceDescription: String? = null
)

data class CompetitionDetailResponse(
    val id: String,
    val legacyId: Long? = null,
    val title: String,
    val startDate: Long,
    val endDate: Long?,
    val kindOfSport: String,
    val description: String?,
    val address: String?,
    val mainOrganizerId: String?,
    val organizingClubId: String? = null,
    val organizerFirstName: String? = null,
    val organizerLastName: String? = null,
    val organizerMiddleName: String? = null,
    val coordinates: CoordinatesResponse?,
    val status: String,
    val startTime: Long? = null,
    /** "FORWARD" / "BY_CHOICE" / "MARKING" — направление ориентирования. */
    val direction: String = "FORWARD",
    val registrationStart: Long?,
    val registrationEnd: Long?,
    val maxParticipants: Int?,
    val feeAmount: Double?,
    val feeCurrency: String?,
    val imageUrl: String? = null,
    val coverCropX: Double? = null,
    val coverCropY: Double? = null,
    val coverCropWidth: Double? = null,
    val coverCropHeight: Double? = null,
    val regulationUrl: String?,
    val mapUrl: String?,
    val resultsUrl: String? = null,
    val contactPhone: String?,
    val contactEmail: String?,
    val website: String?,
    val resultsStatus: String,
    val timeZoneId: String,
    val participantGroups: List<ParticipantGroupDetailResponse>,
    val isUserRegistered: Boolean = false,
    /** Тестовое соревнование: видно только владельцу, исключено из публичной ленты. */
    val isTest: Boolean = false,
    val updatedAt: Long = 0L
)
