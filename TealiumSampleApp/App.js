import React, { Component } from 'react';
import Tealium from 'tealium-react-native';
import { Alert, AppRegistry, Button, StyleSheet, View, Text, ScrollView } from 'react-native';

/*
 * All tests go here. Each will show as a button in the app labeled with the title, and run
 * the "run" callback on press.
 */
let allTests = [
  {
    title: "Track Event",
    run: () => {
      try {
        Tealium.trackEvent("second_verify", {
          "title": "test_event",
          "event_title": "test_event",
          "event_name": "second_verify",
          "testkey": "testval",
          "anotherkey": "anotherval",
          "test_int": 1
        });
        Tealium.trackEventForInstanceName("instance-2", "test_event_2");
        Tealium.getUserConsentStatusForInstanceName("instance-2", function(userConsentStatus) {
           console.log("consent status 'instance-2': " + userConsentStatus);
       });
      } catch(err) {
        Alert.alert(`Issue tracking event: ${err}`);
      }
    }
  },
  {
    title: "Track View",
    run: () => {
      try {
        Tealium.trackView("test_view", {
          "title": "test_view",
          "event_title": "test_view",
          "testkey": "testval",
          "anotherkey": "anotherval"
        });
        Tealium.trackViewForInstanceName("instance-2", "test_view_2");
      } catch(err) {
        Alert.alert(`Issue tracking view: ${err}`);
      }
    }
  },
  {
    title: "Set Volatile Data",
    run: () => {
      try {
        Tealium.setVolatileData({
          "volatile_var": "volatile_val",
          "volatile_var2": "volatile_val2"
        });
        Tealium.setVolatileDataForInstanceName("instance-2", {"foo" : "bar"});
      } catch(err) {
        Alert.alert(`Issue setting volatile data: ${err}`);
      }
    }
  },
  {
    title: "Set Persistent Data",
    run: () => {
      try {
        Tealium.setPersistentData({
          "persistent_var": "persistent_val",
          "persistent_var2": "persistent_val2"
        });
        Tealium.setPersistentDataForInstanceName("instance-2", {"persistent_key2" : "persistent_val2"});
      } catch(err) {
        Alert.alert(`Issue setting persistent data: ${err}`);
      }
    }
  },
  {
    title: "Remove Volatile Data",
    run: () => {
      try {
        Tealium.removeVolatileData([
          "volatile_var",
          "volatile_var2"
        ]);
        Tealium.removeVolatileDataForInstanceName("instance-2", ["foo"]);
      } catch(err) {
        Alert.alert(`Issue removing volatile data: ${err}`);
      }
    }
  },
  {
    title: "Remove Persistent Data",
    run: () => {
      try {
        Tealium.removePersistentData([
          "persistent_var",
          "persistent_var2"
        ]);
  
        Tealium.getVisitorID(function (visitorID) {
          console.log("visitorID: " + visitorID);
        });
        Tealium.getVisitorIDForInstanceName("instance-2", function(visitorID) {
          console.log("visitorID: " + visitorID);
        });

        Tealium.setVolatileDataForInstanceName("instance-2", {"foo": "bar"});
        Tealium.trackViewForInstanceName("instance-2", "instance_2_view");
        Tealium.removeVolatileDataForInstanceName("instance-2", ["foo"]);
        Tealium.trackViewForInstanceName("instance-2", "instance_2_event");

        Tealium.getVolatileData("foo", function(value) {
            console.log("get volatile: " + value);
        });

        Tealium.getVolatileDataForInstanceName("instance-2", "foo", function(value) {
          console.log("value for instance-2 " + value);
        });
        
        Tealium.removePersistentDataForInstanceName("instance-2", ["persistent_key2"]);

        Tealium.getPersistentDataForInstanceName("instance-2", "persistent_key2", function(value) {
          console.log("persistent data: " + value);
        });
      } catch(err) {
        Alert.alert(`Issue removing persistent data: ${err}`);
      }
    }
  },
  {
    title: "Consented",
    run: () => {
      try {
        // Main
        Tealium.setUserConsentStatus(1);
        Tealium.getUserConsentStatus(function (userConsentStatus) {
          console.log("consent status 'main': " + userConsentStatus);
        });
        Tealium.getUserConsentCategories(function (consentCategories) {
          console.log("categories 'main': " + consentCategories);
        });
        Tealium.isConsentLoggingEnabled(function (enabled) {
          console.log("consent logging enabled 'main': " + enabled);
        });
        Tealium.setConsentLoggingEnabled(true);
        Tealium.isConsentLoggingEnabled(function (enabled) {
          console.log("consent logging enabled 'main': " + enabled);
        });

        // Instance-2
        Tealium.setUserConsentStatusForInstanceName("instance-2", 1);
        Tealium.getUserConsentStatusForInstanceName("instance-2", function (userConsentStatus) {
          console.log("consent status 'instance-2': " + userConsentStatus);
        });
        Tealium.getUserConsentCategoriesForInstanceName("instance-2", function (consentCategories) {
          console.log("consent categories 'instance-2': " + consentCategories);
        });
        Tealium.isConsentLoggingEnabledForInstanceName("instance-2", function (enabled) {
          console.log("consent logging enabled 'instance-2': " + enabled);
        });
        Tealium.setConsentLoggingEnabledForInstanceName("instance-2", true);
        Tealium.isConsentLoggingEnabledForInstanceName("instance-2", function (enabled) {
          console.log("consent logging enabled 'instance-2': " + enabled);
        });
      } catch(err) {
        Alert.alert(`Issue setting user consent status: ${err}`);
      }
    }
  },
  {
    title: "Not Consented",
    run: () => {
      try {
        // Main
        Tealium.setUserConsentStatus(2);
        Tealium.getUserConsentStatus(function (userConsentStatus) {
          console.log("consent status 'main': " + userConsentStatus);
        });
        Tealium.getUserConsentCategories(function (consentCategories) {
          console.log("categories 'main': " + consentCategories);
        });

        // Instance-2
        Tealium.setUserConsentStatusForInstanceName("instance-2", 2);
        Tealium.getUserConsentStatusForInstanceName("instance-2", function (userConsentStatus) {
          console.log("consent status 'instance-2': " + userConsentStatus);
        });
      } catch(err) {
        Alert.alert(`Issue rsetting user consent status: ${err}`);
      }
    }
  },
  {
    title: "Partial Consent",
    run: () => {
      try {
        // Main
        Tealium.setUserConsentCategories(["email", "personalization"]);
        Tealium.getUserConsentCategories(function (consentCategories) {
          console.log("categories 'main': " + consentCategories);
        });
        // Instance-2
        Tealium.setUserConsentCategoriesForInstanceName("instance-2", ["analytics", "big_data"]);
        Tealium.getUserConsentCategoriesForInstanceName("instance-2", function (consentCategories) {
          console.log("categories 'instance-2': " + consentCategories);
        });
      } catch(err) {
        Alert.alert(`Issue rsetting user consent status: ${err}`);
      }
    }
  },
  {
    title: "Reset Consent",
    run: () => {
      try {
        // Main
        Tealium.resetUserConsentPreferences();
        Tealium.getUserConsentStatus(function (userConsentStatus) {
          console.log("consent status 'main': " + userConsentStatus);
        });
        Tealium.getUserConsentCategories(function (consentCategories) {
          console.log("categories 'main': " + consentCategories);
        });

        // Instance-2
        Tealium.resetUserConsentPreferencesForInstanceName("instance-2");
        Tealium.getUserConsentStatusForInstanceName("instance-2", function (userConsentStatus) {
          console.log("consent status 'instance-2': " + userConsentStatus);
        });
        Tealium.getUserConsentCategoriesForInstanceName("instance-2", function (consentCategories) {
          console.log("consent categories 'instance-2': " + consentCategories);
        });
      } catch(err) {
        Alert.alert(`Issue rsetting user consent status: ${err}`);
      }
    }
  },
    {
    title: "Add Remote Command",
    run: () => {
      try {
        // Main
        Tealium.addRemoteCommand("test_command", "Hello remote command", function(payload) {
          console.log("test_command data: ")
          console.log(JSON.stringify(payload))
        });

        // Need to send an event to get results back
        Tealium.trackEvent("test_event", {
          "title": "test_event",
          "event_title": "test_event",
          "testkey": "testval",
          "anotherkey": "anotherval",
          "event_name": "second_verify",
          "instance": "MAIN"
        });

        // Instance-2
        Tealium.addRemoteCommandForInstanceName("instance-2", "test_command2", "Hello remote command 2", function(payload) {
          console.log("test_command2 data: ")
          console.log(JSON.stringify(payload))
        });

        // Need to send an event to get results back
        Tealium.trackEventForInstanceName("instance-2", "test_event_2", {"instance": "instance-2"});

        // HTTP Command
        Tealium.addRemoteCommand("display", "Hello remote command", function(payload) {
          console.log("display data: ")
          console.log(JSON.stringify(payload))
          Alert.alert(`display data: ${JSON.stringify(payload)}`)
        });

        // Need to send an event to get results back
        Tealium.trackEvent("display_data", {
          "title": "display_data_event",
          "event_title": "display_data_test_event",
          "testkey": "testval",
          "anotherkey": "anotherval",
          "instance": "MAIN"
        });

      } catch(err) {
        Alert.alert(`Issue adding remote command: ${err}`);
        console.log(err)
      }
    }
  },
  {
    title: "Remove Remote Command",
    run: () => {
      try {
        // Main
        Tealium.removeRemoteCommand("test_command");

        // Instance-2
        Tealium.removeRemoteCommandForInstanceName("instance-2", "test_command2");

        // HTTP Command
        Tealium.removeRemoteCommand("display");

      } catch(err) {
        Alert.alert(`Issue removing remote command: ${err}`);
        console.log(err)
      }
    }
  }
];

/*
 * Component for rendering a test. Props include "title", "count", and "callback".
 * Title is used for labeling the button.
 * Count allows the component to display how many times the test has been ran.
 * Callback is what to run when the button is pressed. Besides the test code,
 * it will also increment the count.
 */
class Test extends Component {
  render() {
    return (
      <View style={this.props.count > 0 ? styles.buttonContainerPressed : styles.buttonContainerUnpressed}>
        <Button
          onPress={this.props.callback}
          title={`${this.props.title} (${this.props.count})`}
          color={this.props.count > 0 ? buttonStyles.pressed.color : buttonStyles.unpressed.color}
        />
      </View>
    );
  }
}

/*
 * This is the main app component. It uses the "allTests" array to render a
 * list of buttons for running the tests, and also keeps basic state for
 * tracking how many times each test has ran. Additional functionality includes
 * displaying an app title, and both a reset button for resetting state and a
 * run all button for running each test.
 */
export default class App extends React.Component {
  constructor(props) {
    super(props);

    // For each item in the "allTests" array, copy into this component's state initializing count to 0
    this.state = {tests: allTests.map(test => ({title: test.title, run: test.run, count: 0}))};

    // Basic Tealium instance
    // Tealium.initialize(
    //   'tealiummobile', 'react-native', 'qa',
    //   'your-ios-datasource', 'your-android-datasource'
    // );

    // Tealium.initializeWithConsentManager(
    //   'tealiummobile', 'react-native', 'qa',
    //   'your-ios-datasource', 'your-android-datasource'
    // );

    // Multiton example
    Tealium.initializeCustom(
      'tealiummobile', 'react-native', 'qa',
      'your-ios-datasource', 'your-android-datasource', 'instance-2',
      true, null, null, "https://some.other.server.com/endpoint", true
    );
  }

  /*
   * Run the test with a particular title by calling its run function and incrementing count.
   */
  run(title) {
    // Must set the state with a new object, so even if a test doesn't run
    // it must be coppied into the new state.
    let newTestStates = this.state.tests.map(test => {
      if(test.title == title) { // if this is the correct test to run
        test.run(this.tealium); // Run test
        // Copy to new state with count incremented
        return {title: test.title, run: test.run, count: test.count + 1};
      } else {
        // Don't run test or increment count, just copy to new state.
        return test;
      }
    });

    // update state
    this.setState({tests: newTestStates});
  }

  /*
   * For each test, run and increment the count.
   */
  runAll() {
    let newTestStates = this.state.tests.map(test => {
      test.run(this.tealium); // Run test
      // Copy to new state with count incremented
      return {title: test.title, run: test.run, count: test.count + 1};
    });

    // update state
    this.setState({tests: newTestStates});
  }

  /*
   * Set the count for each test to 0.
   */
  reset() {
    this.setState({tests: allTests.map(test => ({title: test.title, run: test.run, count: 0}))});
  }

  render() {
    return (
      <View style={styles.appContainer}>
        {/* App title */}
        <Text style={styles.title}>Tealium React Native Testing App</Text>
        <View style={{flexDirection: 'row', justifyContent: 'space-between'}}>
          {/* Reset Button */}
          <View style={styles.buttonContainerNeutral}>
            <Button
              onPress={() => this.reset()}
              title="Reset"
              color={buttonStyles.neutral.color}
            />
          </View>
          {/* Run All Button */}
          <View style={styles.buttonContainerNeutral}>
            <Button
              onPress={() => this.runAll()}
              title="Run All"
              color={buttonStyles.neutral.color}
            />
          </View>
        </View>
        {/* Test Buttons */}
        <ScrollView>
          {this.state.tests.map(test =>
            <Test
              key={test.title}
              title={test.title}
              callback={() => this.run(test.title)}
              count={test.count}
            />
          )}
        </ScrollView>
      </View>
    );
  }
}

let buttonStyles = {
  unpressed: {
    color: "#4060f0"
  },
  pressed: {
    color: "#b040b0"
  },
  neutral: {
    color: "#707070"
  }
}

const styles = StyleSheet.create({
  appContainer: {
   flex: 1,
   backgroundColor: '#cccccc',
   paddingTop: 50
  },
  buttonContainerUnpressed: {
    margin: 10,
    borderColor: "#aaaaaa",
    borderWidth: 1,
    borderRadius: 10,
    backgroundColor: "#abb0c8",
    flexDirection: 'column'
  },
  buttonContainerPressed: {
    margin: 10,
    borderColor: "#aaaaaa",
    borderWidth: 1,
    borderRadius: 10,
    backgroundColor: "#bdabbd",
    flexDirection: 'column'
  },
  buttonContainerNeutral: {
    margin: 10,
    borderColor: "#aaaaaa",
    borderWidth: 1,
    borderRadius: 10,
    backgroundColor: "#c0c0c0",
    flexDirection: 'column',
    width: 100
  },
  title: {
    color: "#404040",
    textAlign: "center",
    fontSize: 30
  }
})