package com.tealium.react.adjust

import android.app.Application
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.adjust.AdjustRemoteCommand

open class AdjustPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(AdjustModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}

class AdjustModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  override fun initialize() {
    super.initialize()
    reactContext.getNativeModule(TealiumReact::class.java)?.registerRemoteCommandFactory(AdjustRemoteCommandFactory())
  }

  private inner class AdjustRemoteCommandFactory : RemoteCommandFactory {
    override val name = "AdjustRemoteCommand"

    override fun create(): RemoteCommand {
      return AdjustRemoteCommand(reactContext.applicationContext as Application)
    }
  }

  override fun getName(): String {
    return MODULE_NAME
  }

  companion object {
    private const val MODULE_NAME = "TealiumReactAdjust"
  }
}