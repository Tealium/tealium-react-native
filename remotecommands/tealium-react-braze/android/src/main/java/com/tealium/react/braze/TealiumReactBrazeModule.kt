package com.tealium.react.braze

import android.app.Application
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.TealiumReact
import com.tealium.react.RemoteCommandFactory
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.braze.BrazeRemoteCommand
import java.util.*

class TealiumReactBrazePackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return Arrays.asList<NativeModule>(TealiumReactBrazeModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class TealiumReactBrazeModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var mSessionHandlingEnabled = true
    private var mRegisterInAppMessenger = true
    private val mSessionHandlingBlacklist: MutableSet<Class<*>> = mutableSetOf()
    private val mInAppMessengerBlacklist: MutableSet<Class<*>> = mutableSetOf()

    override fun initialize() {
        super.initialize()

        reactContext.getNativeModule(TealiumReact::class.java)?.registerRemoteCommandFactory(BrazeRemoteCommandFactory())
    }

    override fun getName(): String {
        return "TealiumReactBraze"
    }

    private inner class BrazeRemoteCommandFactory : RemoteCommandFactory {
        override val name: String
            get() = "BrazeRemoteCommand"

        override fun create(): RemoteCommand {
            return BrazeRemoteCommand(reactContext.applicationContext as Application,
                    mSessionHandlingEnabled, mSessionHandlingBlacklist, mRegisterInAppMessenger, mInAppMessengerBlacklist)
        }
    }

    @ReactMethod
    fun setSessionHandlingEnabled(enabled: Boolean) {
        mSessionHandlingEnabled = enabled
    }

    @ReactMethod
    fun setSessionHandlingBlacklist(classes: ReadableArray) {
        loadClassListInto(mSessionHandlingBlacklist, classes)
    }

    @ReactMethod
    fun setRegisterInAppMessenger(enabled: Boolean) {
        mRegisterInAppMessenger = enabled
    }

    @ReactMethod
    fun setInAppMessengerBlacklist(classes: ReadableArray) {
        loadClassListInto(mInAppMessengerBlacklist, classes)
    }

    private fun loadClassListInto(set: MutableSet<Class<*>>, classes: ReadableArray) {
        for (i in 0 until classes.size()) {
            try {
                val c = Class.forName(classes.getString(i)!!)
                set.add(c)
            } catch (ignored: ClassNotFoundException) {
            }
        }
    }
}