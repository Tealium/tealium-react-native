package com.tealium.react.firebase

import android.app.Application
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.firebase.FirebaseRemoteCommand
import java.util.*

open class FirebasePackage2 : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return Arrays.asList<NativeModule>(FirebaseModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class FirebaseModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerRemoteCommandFactory(FirebaseRemoteCommandFactory())
    }

    private inner class FirebaseRemoteCommandFactory : RemoteCommandFactory {
        override val name = "FirebaseRemoteCommand"

        override fun create(): RemoteCommand {
            return FirebaseRemoteCommand(reactContext.applicationContext as Application)
        }
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        private const val MODULE_NAME = "TealiumReactFirebase"
    }
}