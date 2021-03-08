# Change Log

## 2.X

[Full documentation](https://docs.tealium.com/platforms/react-native/install/)

- 2.0.3
  - Bug fixes
	    - `safeGet` extension methods added in Kotlin to check key safety on earlier RN versions (< 0.63.3)
	    - React module imports added to bridging header in order to accomodate RN versions (< 0.63.3)
	  - SDK dependency and `package.json` increments
- 2.0.2
  - Bug fix
	    - Removed deinit in `TealiumReactNative.swift` that was clearing event names
- 2.0.1
  - Bug fixes
	    - Added all `.ts` files in the package
	    - Updated syntax and source path in `.podspec`
- 2.0.0
	- Initial release. Updated the module to use the Kotlin and Swift libraries.

## 1.X

- 1.1.0
  - Added ability to enable ad identifier (ADID/IDFA) collection in the `initializeCustom` method
  - Improved multiple instance handling
- 1.0.10
  - Added Collect Dispatch URL to the `initializeCustom` method
  - Enabled `null` in TypeScript definition for parameters that accept it
  - Fixed TypeScript definition for Remote Command methods
- 1.0.9
  - Bug fix
    - Removed duplicate default keywords from Typescript definitions file
- 1.0.8
  - Added support for [Remote Commands](https://docs.tealium.com/platforms/remote-commands/)
  - Underlying Tealium libraries updated to versions 5.7.0 (Android) and 5.6.6 (iOS) 
  - Added TypeScript typings. Thanks, [@jbristol-trav](https://github.com/jbristol-trav)!
- 1.0.7
  - Support for ReactNative Autolinking in 0.60 and above
  - Better support for nested event data
  - Underlying Tealium libraries updated to versions 5.5.5 (Android) and 5.6.3 (iOS)
  - Bug Fixes
    - Corrected method signature for `Tealium.getPersistentData(key, callback)`
    - Build errors in iOS relating to Vdata/Event enums have been fixed
    - Fixed crashes on some iPad versions
- 1.0.6
- 1.0.5
  - Migration away from rnpm
- 1.0.4
- 1.0.3
  - Bug fix
    - Handling of possible null Activity from ReactApplicationContext
    - Additional null checks on Consent Manager methods
  - Sample App
    - Gradle Android versions updated to latest
- 1.0.2
  - Bug fix
    - Fixed public trackEvent to call trackEvent on Tealium instance
- 1.0.1
  - Additional public-facing methods of Tealium iOS and Tealium Android exposed
    - Consent Management
    - VisitorID
    - Multiple instances of Tealium
    - Configuration options
- 1.0.0
  - Initial release
    - Tealium and TealiumLifecycle support
 