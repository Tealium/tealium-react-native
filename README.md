## Tealium React Native

This repository contains the necessary assets for exposing Tealium's native [Android](https://community.tealiumiq.com/t5/Tealium-for-Android/tkb-p/android-documentation) and [iOS](https://community.tealiumiq.com/t5/Tealium-for-iOS/tkb-p/ios-documentation) mobile libraries to the JavaScript code in your React Native project. This includes code for exposing the native APIs for both platforms through React Native's bridging system, and a basic helper class in JavaScript to create a single cross platform API.

Because including the respective libraries for Android and iOS means working with platform specific code, you will need access to your native build environments. For example you will need to "[eject](https://github.com/react-community/create-react-native-app/blob/master/EJECTING.md)" if you're using [Create React Native App](https://github.com/react-community/create-react-native-app) and haven't done so already. Including the mobile libraries works no differently than in any other Android or iOS project. Therefore any non React Native specific documentation in the [Tealium Learning Community](https://community.tealiumiq.com/) regarding the mobile libraries is still relevant, and at times you may be directed to it from our React Native documentation. For this reason the mobile libraries themselves aren't distributed in this repository, and are meant to be acquired through the usual methods described for [Android](https://community.tealiumiq.com/t5/Tealium-for-Android/Adding-Tealium-to-Your-Android-App/ta-p/16846) and [iOS](https://community.tealiumiq.com/t5/Tealium-for-iOS/Adding-Tealium-to-Your-iOS-App/ta-p/16327).

You can also find a sample application demonstrating how everything is put together and how the API gets called in JavaScript, with instructions in the Tealium Learning Community.

## Documentation
For full documentation, please see the Tealium Learning Community:

[Tealium React Native Documentation on TLC](https://community.tealiumiq.com/t5/Mobile-Libraries/Tealium-for-React-Native/ta-p/22449)

## License

Use of this software is subject to the terms and conditions of the license agreement contained in the file titled "LICENSE.txt".  Please read the license before downloading or using any of the files contained in this repository. By downloading or using any of these files, you are agreeing to be bound by and comply with the license agreement.

 
---
Copyright (C) 2012-2018, Tealium Inc.