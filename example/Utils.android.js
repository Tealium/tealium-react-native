import { PermissionsAndroid } from 'react-native';

const checkAndRequestPermissions = async () => {
    try {
        const granted = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
            {
                'title': 'Example App',
                'message': 'Example App access to your location '
            })

        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
            console.log("Location permission is granted.")
        } else {
            console.log("location permission denied")
            alert("Location permission denied");
        }
        return granted;
    } catch (err) {
        console.warn(err)
        return false;
    }
}

module.exports = {
    checkAndRequestPermissions
}