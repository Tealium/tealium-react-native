# tealium-react-appsflyer

## Getting started

To install the package into your React Native app:
`$ yarn add tealium-react-appsflyer`

### Mostly automatic installation

For iOS only,
`$ cd ios && pod install && cd ..`

Setup the Appsflyer dependencies according to the standard guides for [iOS](https://help.appsflyer.com/en/article/get-started-ios-sdk) and [Android](https://help.appsflyer.com/en/article/get-started-android-sdk) to get all required config into you project.

## Usage
```javascript
import AppsflyerRemoteCommand from 'tealium-react-appsflyer';

AppsflyerRemoteCommand.initialize();

let config = TealiumConfig {
    // ...
    remoteCommands: [{
        id: AppsflyerRemoteCommand.name,
        // Optional - path to local JSON mappings
        // path: "appsflyer.json"
        // Optional - path to remote JSON mappings
        // url: "https://some.domain.com/appsflyer.json"
    }]
}
```
