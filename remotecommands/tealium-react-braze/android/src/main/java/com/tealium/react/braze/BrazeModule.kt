package com.tealium.react.braze

import android.app.Application
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.braze.BrazeRemoteCommand
import java.util.*

open class BrazePackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return Arrays.asList<NativeModule>(BrazeModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class BrazeModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerRemoteCommandFactory(BrazeRemoteCommandFactory())
    }

    private inner class BrazeRemoteCommandFactory : RemoteCommandFactory {
        override val name = "BrazeRemoteCommand"

        override fun create(): RemoteCommand {
            return BrazeRemoteCommand(reactContext.applicationContext as Application)
        }
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        private const val MODULE_NAME = "TealiumReactBraze"
    }
}
