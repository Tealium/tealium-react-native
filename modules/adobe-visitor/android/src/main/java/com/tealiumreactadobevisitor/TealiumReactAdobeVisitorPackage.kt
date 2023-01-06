package com.tealiumreactadobevisitor

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class TealiumReactAdobeVisitorPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(TealiumReactAdobeVisitorModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}

class TealiumReactAdobeVisitor(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext),
  OptionalModule {

  }
