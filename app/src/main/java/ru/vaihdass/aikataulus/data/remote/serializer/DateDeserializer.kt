package ru.vaihdass.aikataulus.data.remote.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import timber.log.Timber
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateDeserializer : JsonDeserializer<Date> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date? {
        if (json.isJsonNull) {
            return null
        }

        val dateString = json.asString
        return parseDate(dateString)
    }

    private fun parseDate(dateString: String): Date? {
        try {
            // Default unix timestamp (with ms)
            val timestamp = dateString.toLong()
            return Date(timestamp)
        } catch (e: NumberFormatException) {
            // RFC 3339 timestamp
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                return dateFormat.parse(dateString)
            } catch (e: ParseException) {
                Timber.tag("AikataulusDateSerializer")
                    .e(e, "Exception due parsing date: $dateString")
                return null
            }
        }
    }
}