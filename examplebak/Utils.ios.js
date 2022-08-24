// import {  } from 'react-native';
import { request, PERMISSIONS, RESULTS } from "react-native-permissions";

const checkAndRequestPermissions = async () => {
    const granted = await request(
        PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
        {
          title: 'TealiumDemoApp',
          message: 'TeailiumDemoApp would like access to your location ',
        }
      ).catch();
    
      return granted === RESULTS.GRANTED;
}

module.exports = {
    checkAndRequestPermissions
}