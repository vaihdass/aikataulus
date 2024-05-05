package ru.vaihdass.aikataulus.data.remote.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.vaihdass.aikataulus.base.Constants
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateSerializer : JsonSerializer<Date> {
    private val dateFormatter = SimpleDateFormat(Constants.GOOGLE_TASKS_DATE_PATTERN, Locale.getDefault())

    init {
        dateFormatter.apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    override fun serialize(
        src: Date, typeOfSrc: Type, context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(dateFormatter.format(src))
    }
}