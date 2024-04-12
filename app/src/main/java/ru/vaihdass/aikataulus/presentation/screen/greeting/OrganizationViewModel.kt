package ru.vaihdass.aikataulus.presentation.screen.greeting

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.GreetingUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.OrganizationUiModel
import javax.inject.Inject

class OrganizationViewModel @Inject constructor(
    private val greetingUseCase: GreetingUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {
    var selectedOrganization: OrganizationUiModel? = null
    private val _organizationsFlow = MutableStateFlow<List<OrganizationUiModel>>(emptyList())
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private var _organizationsAdapter: ArrayAdapter<OrganizationUiModel>? = null

    val organizationsFlow
        get() = _organizationsFlow.asStateFlow()
    val errorFlow: SharedFlow<String?>
        get() = _errorFlow.asSharedFlow()
    val organizationsAdapter: ArrayAdapter<OrganizationUiModel>
        get() = _organizationsAdapter ?: throw IllegalStateException("Adapter should be initialized")

    init {
        fetchOrganizations()
    }

    fun initAdapter(ctx: Context) {
        if (_organizationsAdapter != null) return

        _organizationsAdapter = ArrayAdapter(
            ctx,
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
    }

    fun fetchOrganizations() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                greetingUseCase.getOrganizations()
            }.onSuccess { organizations ->
                _organizationsFlow.value = organizations
            }.onFailure { throwable ->
                _errorFlow.emit(throwable.message)
            }
        }
    }
}