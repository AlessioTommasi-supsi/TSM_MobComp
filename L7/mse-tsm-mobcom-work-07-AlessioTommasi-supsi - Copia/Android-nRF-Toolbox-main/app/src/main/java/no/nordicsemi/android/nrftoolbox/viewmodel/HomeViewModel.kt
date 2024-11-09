package no.nordicsemi.android.nrftoolbox.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.android.analytics.AppAnalytics
import no.nordicsemi.android.analytics.ProfileOpenEvent
import no.nordicsemi.android.cgms.repository.CGMRepository
import no.nordicsemi.android.common.logger.LoggerLauncher
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.csc.repository.CSCRepository
import no.nordicsemi.android.hrs.service.HRSRepository
import no.nordicsemi.android.hts.repository.HTSRepository
import no.nordicsemi.android.nrftoolbox.repository.ActivitySignals
import no.nordicsemi.android.nrftoolbox.view.HomeViewState
import no.nordicsemi.android.prx.repository.PRXRepository
import no.nordicsemi.android.rscs.repository.RSCSRepository
import no.nordicsemi.android.uart.repository.UARTRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val navigationManager: Navigator,
    private val activitySignals: ActivitySignals,
    cgmRepository: CGMRepository,
    cscRepository: CSCRepository,
    hrsRepository: HRSRepository,
    htsRepository: HTSRepository,
    prxRepository: PRXRepository,
    rscsRepository: RSCSRepository,
    uartRepository: UARTRepository,
    private val analytics: AppAnalytics
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    init {
        cgmRepository.isRunning.onEach {
            _state.value = _state.value.copy(isCGMModuleRunning = it)
        }.launchIn(viewModelScope)

        cscRepository.isRunning.onEach {
            _state.value = _state.value.copy(isCSCModuleRunning = it)
        }.launchIn(viewModelScope)

        hrsRepository.isRunning.onEach {
            _state.value = _state.value.copy(isHRSModuleRunning = it)
        }.launchIn(viewModelScope)

        htsRepository.isRunning.onEach {
            _state.value = _state.value.copy(isHTSModuleRunning = it)
        }.launchIn(viewModelScope)

        prxRepository.isRunning.onEach {
            _state.value = _state.value.copy(isPRXModuleRunning = it)
        }.launchIn(viewModelScope)

        rscsRepository.isRunning.onEach {
            _state.value = _state.value.copy(isRSCSModuleRunning = it)
        }.launchIn(viewModelScope)

        uartRepository.isRunning.onEach {
            _state.value = _state.value.copy(isUARTModuleRunning = it)
        }.launchIn(viewModelScope)

        // Gestisci lo stato del nuovo servizio direttamente nel ViewModel
        _state.value = _state.value.copy(true)
    }

    fun openProfile(destination: DestinationId<Unit, Unit>) {
        navigationManager.navigateTo(destination)
    }

    fun openLogger() {
        LoggerLauncher.launch(context)
    }

    fun logEvent(event: ProfileOpenEvent) {
        analytics.logEvent(event)
    }

    // Funzioni per gestire lo stato del nuovo servizio
    fun startNewService() {
        _state.value = _state.value.copy( true)
    }

    fun stopNewService() {
        _state.value = _state.value.copy(true)
    }
}