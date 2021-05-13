package com.tealium.react.braze

import android.app.Application
import com.facebook.react.bridge.ReactApplicationContext
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.braze.BrazeRemoteCommand
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21, 28])
class TealiumReactBrazeModuleTests {

    @RelaxedMockK
    lateinit var mockApplication: Application

    @RelaxedMockK
    lateinit var mockReactContext: ReactApplicationContext

    @RelaxedMockK
    lateinit var mockTealiumReact: TealiumReact

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockReactContext.getNativeModule(TealiumReact::class.java) } returns mockTealiumReact
        every { mockReactContext.applicationContext } returns mockApplication
    }

    @Test
    fun initialize_RegistersFactory() {
        TealiumReactBrazeModule(mockReactContext).initialize()

        verify {
            mockTealiumReact.registerRemoteCommandFactory(any())
        }
    }

    @Test
    fun factory_CreatesBrazeRemoteCommandInstance() {
        val factorySlot = CapturingSlot<RemoteCommandFactory>()
        every { mockTealiumReact.registerRemoteCommandFactory(capture(factorySlot)) } returns Unit

        TealiumReactBrazeModule(mockReactContext).initialize()
        val brazeCommand = factorySlot.captured.create()
        assertTrue(brazeCommand is BrazeRemoteCommand)
    }
}