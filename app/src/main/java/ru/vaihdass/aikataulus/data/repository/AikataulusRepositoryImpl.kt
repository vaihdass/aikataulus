package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.data.remote.api.AikataulusApi
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import javax.inject.Inject

class AikataulusRepositoryImpl @Inject constructor(
    private val aikataulusApi: AikataulusApi,
) : AikataulusRepository {

}