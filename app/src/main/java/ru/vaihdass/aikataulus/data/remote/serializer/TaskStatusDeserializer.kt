package ru.vaihdass.aikataulus.data.remote.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.vaihdass.aikataulus.data.remote.pojo.TaskStatus
import java.lang.reflect.Type

class TaskStatusDeserializer : JsonDeserializer<TaskStatus> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): TaskStatus {
        val statusId = json.asInt
        return when (statusId) {
            1 -> TaskStatus.NOT_COMPLETED
            2 -> TaskStatus.IN_PROGRESS
            3 -> TaskStatus.COMPLETED
            else -> TaskStatus.NOT_COMPLETED
        }
    }
}