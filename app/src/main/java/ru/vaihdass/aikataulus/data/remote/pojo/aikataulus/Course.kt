package ru.vaihdass.aikataulus.data.remote.pojo.aikataulus

import com.google.gson.annotations.SerializedName

data class Course (
    var id: Int,
    var name: String,
    @SerializedName("organization_id") var organizationId: Int,
)