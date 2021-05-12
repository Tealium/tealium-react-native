package com.tealium.react.firebase

import android.app.Application
import com.facebook.react.bridge.ReactApplicationContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.firebase.FirebaseRemoteCommand
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
class FirebaseModuleTests {

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
        mockkStatic(FirebaseAnalytics::class)
        every { FirebaseAnalytics.getInstance(any()) } returns mockk()
    }

    @Test
    fun initialize_RegistersFactory() {
        FirebaseModule(mockReactContext).initialize()

        verify {
            mockTealiumReact.registerRemoteCommandFactory(any())
        }
    }

    @Test
    fun factory_CreatesFirebaseRemoteCommandInstance() {
        val factorySlot = CapturingSlot<RemoteCommandFactory>()
        every { mockTealiumReact.registerRemoteCommandFactory(capture(factorySlot)) } returns Unit

        FirebaseModule(mockReactContext).initialize()
        val firebaseCommand = factorySlot.captured.create()
        assertTrue(firebaseCommand is FirebaseRemoteCommand)
    }
}