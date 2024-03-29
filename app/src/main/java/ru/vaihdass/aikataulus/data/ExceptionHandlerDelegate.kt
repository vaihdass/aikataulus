package ru.vaihdass.aikataulus.data

import retrofit2.HttpException
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.exception.BadRequestException
import ru.vaihdass.aikataulus.data.exception.NotFoundException
import ru.vaihdass.aikataulus.data.exception.TooManyRequestsException
import ru.vaihdass.aikataulus.data.exception.UnauthorizedException
import ru.vaihdass.aikataulus.data.exception.UnexpectedServerException
import ru.vaihdass.aikataulus.utils.ResManager
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor(
    private val resManager: ResManager,
) {
    fun handleException(e: Throwable): Throwable {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    400 -> {
                        BadRequestException(message = resManager.getString(R.string.exception_bad_request))
                    }

                    401 -> {
                        UnauthorizedException(message = resManager.getString(R.string.exception_not_authorized))
                    }

                    404 -> {
                        NotFoundException(message = resManager.getString(R.string.exception_not_found))
                    }

                    429 -> {
                        TooManyRequestsException(message = resManager.getString(R.string.exception_too_many_requests))
                    }

                    else -> {
                        if (e.code() >= 500) {
                            UnexpectedServerException(message = resManager.getString(R.string.exception_unexpected_server))
                        } else {
                            e
                        }
                    }
                }
            }

            else -> {
                e
            }
        }
    }
}