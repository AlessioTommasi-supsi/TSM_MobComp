package no.nordicsemi.android.nrftoolbox

import no.nordicsemi.android.bps.view.BPSScreen
import no.nordicsemi.android.cgms.view.CGMScreen
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.csc.view.CSCScreen
import no.nordicsemi.android.gls.main.view.GLSScreen
import no.nordicsemi.android.hrs.view.HRSScreen
import no.nordicsemi.android.hts.view.HTSScreen
import no.nordicsemi.android.nrftoolbox.view.HomeScreen
import no.nordicsemi.android.prx.view.PRXScreen
import no.nordicsemi.android.rscs.view.RSCSScreen
import no.nordicsemi.android.toolbox.scanner.ScannerDestination
import no.nordicsemi.android.uart.view.UARTScreen
import no.nordicsemi.android.nrftoolbox.view.NewServiceScreen

val HomeDestinationId = createSimpleDestination("home-destination")

val HomeDestinations = listOf(
    defineDestination(HomeDestinationId) { HomeScreen() },
    ScannerDestination
)

val NewServiceDestinationId = createSimpleDestination("new-service-destination")

val NewServiceDestination = defineDestination(NewServiceDestinationId) { NewServiceScreen() }

val CSCDestinationId = createSimpleDestination("csc-destination")
val HRSDestinationId = createSimpleDestination("hrs-destination")
val HTSDestinationId = createSimpleDestination("hts-destination")
val GLSDestinationId = createSimpleDestination("gls-destination")
val BPSDestinationId = createSimpleDestination("bps-destination")
val PRXDestinationId = createSimpleDestination("prx-destination")
val RSCSDestinationId = createSimpleDestination("rscs-destination")
val CGMSDestinationId = createSimpleDestination("cgms-destination")
val UARTDestinationId = createSimpleDestination("uart-destination")
val UARTDestinationId2 = createSimpleDestination("uart-destination2")

val ProfileDestinations = listOf(
    defineDestination(CSCDestinationId) { CSCScreen() },
    defineDestination(HRSDestinationId) { HRSScreen() },
    defineDestination(HTSDestinationId) { HTSScreen() },
    defineDestination(GLSDestinationId) { GLSScreen() },
    defineDestination(BPSDestinationId) { BPSScreen() },
    defineDestination(PRXDestinationId) { PRXScreen() },
    defineDestination(RSCSDestinationId) { RSCSScreen() },
    defineDestination(CGMSDestinationId) { CGMScreen() },
    defineDestination(UARTDestinationId) { UARTScreen() },
    defineDestination(UARTDestinationId2) { UARTScreen() },
    NewServiceDestination
)