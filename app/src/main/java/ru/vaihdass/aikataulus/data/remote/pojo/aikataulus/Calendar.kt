package ru.vaihdass.aikataulus.data.remote.pojo.aikataulus

import com.google.gson.annotations.SerializedName

data class Calendar (
    var id: Int,
    var url: String,
    var name: String,
    @SerializedName("course_id") var courseId: Int,
)