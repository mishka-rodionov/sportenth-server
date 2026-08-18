package com.competra.data.response.orienteering

import com.google.gson.annotations.SerializedName

data class OrganizerResponse(
    @SerializedName("organizerId") val organizerId: Long,
    @SerializedName("competitionId") val competitionId: String,
    @SerializedName("userId") val userId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("role") val role: String,
    @SerializedName("contactEmail") val contactEmail: String?,
    @SerializedName("contactPhone") val contactPhone: String?,
    @SerializedName("updatedAt") val updatedAt: Long = 0L
)
