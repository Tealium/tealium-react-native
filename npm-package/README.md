# tealium-react-native

This package provides a native module for React Native apps giving access to Tealium's native libraries for Android and iOS, exposed through a common JavaScript api. More information can be found in the [Tealium Learning Community document](https://community.tealiumiq.com/t5/Mobile-Libraries/Tealium-for-React-Native/ta-p/22449).

## Installation

In the root of your React Native project, run the following:

```console
npm install tealium-react-native
react-native link
```

The first command will download the `tealium-react-native` package and install it to your project.

The second command will link the native dependencies within the module to your main React Native app so that it can build against the module.
