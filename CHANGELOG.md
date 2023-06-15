# Change Log

## 2.X

[Full documentation](https://docs.tealium.com/platforms/react-native/install/)

- 2.3.1
  - Android-Kotlin dependencies updated including severa fixes listed below.
    - Core 1.5.3, Visitor Service 1.2.0 and Lifecycle 1.2.0
    - Fix: Some event sending delayed by Visitor Service updates
    - Fix: Lifecycle negative values
    - Fix: ModuleManager crashes caused by concurrent modification

- 2.3.0
  - Visitor Switching support
  - Kotlin/Swift SDK dependencies updated

- 2.2.2
  - Added React 18 to supported react versions for all packages
  - All remote commands and location module updated to support React 18
  - Upgraded Kotlin version to 1.6.0

- 2.2.1
  - Kotlin dependency upgraded to 1.4.1
  - Swift dependency upgraded to 2.6.4
  - Added overrideCollectProfile and sessionCountingEnabled keys to the config
  - Added gatherTrackData method to tealium instance

- 2.2.0
  - Support added up to React Native 0.67.1 + gradle 7
  - Support for additional Optional Modules, as well as packaged Remote Commands
  - Tealium Kotlin 1.3.0 and Tealium Swift 2.6.0 dependency increases.
  - Location Module 1.0.0 added
  - Note. for iOS, the podspec has been split so you should now add `pod "tealium-react-native-swift", :path => '../node_modules/tealium-react-native/tealium-react-native-swift.podspec'` to your `Podfile`.

- 2.1.3
  - Kotlin Dependency updates
    - Support for `Expiry.UntilRestart`
    - `TimeCollector` now added by default in-line with Swift implementation
    - Better `customVisitorId` support

- 2.1.2
  - Android support for customVisitorId
  - Added support to get session ID - use getSessionId()

- 2.1.1
  - Android launch event fix + tests
  - Tealium Android dependencies updated to the latest
  - iOS VisitorProfile arraysOfStrings update
  - Update tealium-react-native.podspec to support 0.64+ and Hermes
  - Update the index.d.ts from this PR
  - Update example app

- 2.1.0
  - New optional callback parameter for the initialize() to support methods that should be called once the Tealium instance is ready.
  - New remoteCommands property on the TealiumConfig object to allow passing RemoteCommands at config time.

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
 