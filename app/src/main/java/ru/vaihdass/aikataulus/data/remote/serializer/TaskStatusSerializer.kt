package ru.vaihdass.aikataulus.data.remote.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.vaihdass.aikataulus.data.remote.util.TaskStatus
import java.lang.reflect.Type

class TaskStatusSerializer : JsonSerializer<TaskStatus> {
    override fun serialize(
        src: TaskStatus, typeOfSrc: Type, context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            TaskStatus.NOT_COMPLETED -> JsonPrimitive("needsAction")
            TaskStatus.DONE -> JsonPrimitive("completed")
        }
    }
}