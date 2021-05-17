# tealium-react-braze

## Getting started

To install the package into your React Native app:
`$ yarn add tealium-react-braze`

### Mostly automatic installation

On Android projects you will need to add the Braze maven repository to your top level `build.gradle` file: 
```groovy
allprojects {
    repositories {
        // ..
        maven {
            url "https://appboy.github.io/appboy-android-sdk/sdk"
        }
        // ..
    }
}
```

For iOS only you will need to install the CocoaPods dependencies:

```bash 
$ cd ios && pod install && cd ..
```


## Usage
```javascript
import BrazeRemoteCommand from 'tealium-react-braze';

/*
    Optional - configuration
*/
// BrazeRemoteCommand.setSessionHandlingEnabled(false)
// BrazeRemoteCommand.setRegisterInAppMessenger(false)
// BrazeRemoteCommand.setSessionHandlingBlacklist(["<fully qualified class name>"])
// BrazeRemoteCommand.setInAppMessengerBlacklist(["<fully qualified class name>"])

let config = TealiumConfig {
    // ...
    remoteCommands: [{
        id: BrazeRemoteCommand.name,
        // Optional - path to local JSON mappings
        // path: "braze.json"
        // Optional - path to remote JSON mappings
        // url: "https://some.domain.com/braze.json"
    }]
}
```

For additional information, see the full [documentation](https://docs.tealium.com/platforms/remote-commands/integrations/braze/)