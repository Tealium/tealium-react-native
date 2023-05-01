package com.tealium.react.appsflyer

import android.app.Application
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.appsflyer.AppsFlyerRemoteCommand

class AppsFlyerPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(AppsFlyerModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}

class AppsFlyerModule(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  @ReactMethod
  fun initialize(devKey: String?) {
    reactContext.getNativeModule(TealiumReact::class.java)
      ?.registerRemoteCommandFactory(AppsFlyerRemoteCommandFactory(devKey))
  }

  private inner class AppsFlyerRemoteCommandFactory(devKey: String?) : RemoteCommandFactory {
    override val name = "AppsFlyerRemoteCommand"
    private val appsFlyerRemoteCommand =
      AppsFlyerRemoteCommand(reactApplicationContext.applicationContext as Application, devKey)

    override fun create(): RemoteCommand {
      return appsFlyerRemoteCommand
    }
  }

  override fun getName(): String {
    return MODULE_NAME
  }

  companion object {
    const val MODULE_NAME = "TealiumReactAppsFlyer"
  }
}
