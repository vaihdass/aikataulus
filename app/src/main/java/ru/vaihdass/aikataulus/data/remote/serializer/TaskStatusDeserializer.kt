package ru.vaihdass.aikataulus.data.remote.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.vaihdass.aikataulus.data.remote.util.TaskStatus
import java.lang.reflect.Type

class TaskStatusDeserializer : JsonDeserializer<TaskStatus> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): TaskStatus {
        val statusId = json.asString
        return when (statusId) {
            "needsAction" -> TaskStatus.NOT_COMPLETED
            "completed" -> TaskStatus.DONE
            else -> TaskStatus.NOT_COMPLETED
        }
    }
}