package com.tealium.react.facebook

import android.app.Application
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.facebook.FacebookRemoteCommand
import java.util.*

open class FacebookPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(FacebookModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class FacebookModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    
    @ReactMethod
    fun initialize() {
        val factory = FacebookRemoteCommandFactory()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerRemoteCommandFactory(factory)
    }

    private inner class FacebookRemoteCommandFactory() : RemoteCommandFactory {
        override val name = "FacebookRemoteCommand"

        override fun create(): RemoteCommand {
            return FacebookRemoteCommand(
                reactContext.applicationContext as Application,
            )
        }
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        private const val MODULE_NAME = "TealiumReactFacebook"
    }
}
