// import {  } from 'react-native';
import { request, PERMISSIONS, RESULTS } from "react-native-permissions";

const checkAndRequestPermissions = async () => {
    const granted = await request(
        PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
        {
          title: 'Example App',
          message: 'Example App would like to access your location ',
        }
      ).catch();
    
      return granted === RESULTS.GRANTED;
}

module.exports = {
    checkAndRequestPermissions
}