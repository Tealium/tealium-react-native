import React, { Component } from 'react';
import { Platform, Button, StyleSheet, Text, TextInput, View, TouchableOpacity, Alert, ScrollView, SafeAreaView } from 'react-native';
import Tealium from 'tealium-react-native';
import { TealiumConfig, TealiumView, TealiumEvent, ConsentCategories, Dispatchers, Collectors, ConsentPolicy, Expiry, ConsentExpiry, TimeUnit, ConsentStatus, TealiumEnvironment, RemoteCommand } from 'tealium-react-native/common';

export default class App extends Component < {} > {

    componentDidMount() {
        let config: TealiumConfig = { account: 'tealiummobile', profile: 'demo', environment: TealiumEnvironment.dev, dispatchers: [Dispatchers.Collect, Dispatchers.TagManagement, Dispatchers.RemoteCommands], collectors: [Collectors.AppData, Collectors.DeviceData, Collectors.Lifecycle, Collectors.Connectivity], consentLoggingEnabled: true, consentExpiry: {'time': 10, 'unit': 'minutes' }, consentPolicy: ConsentPolicy.gdpr, batchingEnabled: false, visitorServiceEnabled: true, useRemoteLibrarySettings: false };
        Tealium.initialize(config, success => {
                    if (!success) {
                        console.log("Tealium not initialized")
                        return
                    }
                    Tealium.setConsentStatus(ConsentStatus.consented)
                    Tealium.addRemoteCommand("hello", payload => {
                        console.log('hello remote command');
                        console.log(JSON.stringify(payload));
                    });
                });
        Tealium.setVisitorServiceListener(profile => {
            console.log("audiences: ");
            console.log(JSON.stringify(profile["audiences"]));
            console.log("tallies: ")
            console.log(JSON.stringify(profile["tallies"]));
            console.log("currentVisit: ")
            console.log(JSON.stringify(profile["currentVisit"]));
        });
        Tealium.setConsentExpiryListener(() => {
            console.log("Consent Expired");
        });
    }

    trackEvent() {
        let event = new TealiumEvent('Test Event', {'event_name': 'test'});
        Tealium.track(event);
    }

    trackView() {
        let view = new TealiumView('Test View', {'view_name': 'test'});
        Tealium.track(view);
    }

    optIn() {
        Tealium.setConsentStatus(ConsentStatus.consented);
    }

    setRandomConsentCategories() {
        let randomCategories = 
            [ConsentCategories.affiliates,
            ConsentCategories.analytics,
            ConsentCategories.bigData,
            ConsentCategories.cdp,
            ConsentCategories.cookieMatch,
            ConsentCategories.crm,
            ConsentCategories.displayAds,
            ConsentCategories.email,
            ConsentCategories.engagement,
            ConsentCategories.misc,
            ConsentCategories.mobile,
            ConsentCategories.monitoring,
            ConsentCategories.personalization,
            ConsentCategories.social]
                      .map((a) => ({ sort: Math.random(), value: a }))
                      .sort((a, b) => a.sort - b.sort)
                      .map((a) => a.value)
                      .slice(0, 3);
        Tealium.setConsentCategories(randomCategories);
    }

    optOut() {
        Tealium.setConsentStatus(ConsentStatus.notConsented);
    }

    getConsentStatus() {
        Tealium.getConsentStatus(status => {
            console.log("Consent status: " + status)
        })
    }

    getConsentCategories() {
        Tealium.getConsentCategories(categories => {
            console.log("Consent categories: " + categories)
        })
    }

    resetConsent() {
        Tealium.setConsentStatus(ConsentStatus.unknown);
    }

    addData() {
        var data = new Map();
        data['test_session_data'] = 'test'
        data['my_test_value'] = 1;
        Tealium.addData(data, Expiry.session);
    }

    getData() {
        Tealium.getData('test_session_data', value => {
            console.log("test_session_data: " + value)
        })
    }

    removeData() {
        Tealium.removeData(['test_session_data']);
        Tealium.removeData(['my_test_value']);
    }

    addRemoteCommand() {
        Tealium.addRemoteCommand('example', payload => {
            console.log('example remote command');
            console.log(JSON.stringify(payload));
        });
    }

    removeRemoteCommand() {
       Tealium.removeRemoteCommand('hello');
       Tealium.removeRemoteCommand('example');
    }

    getVisitorId() {
        Tealium.getVisitorId(value => {
            console.log("Visitor id: " + value)
            Alert.alert("Visitor Id: ", value, [{ text: "OK", style: "cancel" }])
        });
    }

    terminate() {
        Tealium.terminateInstance();
    }


    render() {
        return (
        <SafeAreaView style={styles.container}>
        <Text style={styles.sectionTitle}> Tealium React Native </Text>
        <View style={{ flexDirection: 'row' }}>
        <ScrollView style={styles.scrollView}>
        <Trace />
        <View style = {styles.inputContainer}>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.trackEvent}>
            <Text style={styles.textStyle}>TRACK EVENT</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.trackView}>
            <Text style={styles.textStyle}>TRACK VIEW</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.optIn}>
            <Text style={styles.textStyle}>OPT IN</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.optOut}>
            <Text style={styles.textStyle}>OPT OUT</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.getConsentStatus}>
            <Text style={styles.textStyle}>GET CONSENT STATUS</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.getConsentCategories}>
            <Text style={styles.textStyle}>GET CONSENT CATEGORIES</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.resetConsent}>
            <Text style={styles.textStyle}>RESET CONSENT</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.setRandomConsentCategories}>
            <Text style={styles.textStyle}>SET CONSENT CATEGORIES</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.addData}>
            <Text style={styles.textStyle}>ADD DATA</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.getData}>
            <Text style={styles.textStyle}>GET DATA</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.removeData}>
            <Text style={styles.textStyle}>REMOVE DATA</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.addRemoteCommand}>
            <Text style={styles.textStyle}>ADD REMOTE COMMAND</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.removeRemoteCommand}>
            <Text style={styles.textStyle}>REMOVE REMOTE COMMAND</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.getVisitorId}>
            <Text style={styles.textStyle}>GET VISITOR ID</Text>
        </TouchableOpacity>
        <View style={styles.space} />
        <TouchableOpacity style={styles.buttonContainer} onPress={this.terminate}>
            <Text style={styles.textStyle}>DISABLE TEALIUM</Text>
        </TouchableOpacity>
        </View>
        </ScrollView>
        </View>
      </SafeAreaView>
        );
    }

    componentWillUnmount() {
        Tealium.removeListeners();
    }

}

class Trace extends Component {
   state = {
      traceId: ''
   }
   handleTraceId = (text) => {
      this.setState({ traceId: text })
   }
   joinTrace = (id) => {
      if (id !== '') {
            Alert.alert('Trace started with ID: ' + id);
            Tealium.joinTrace(id);
            Tealium.track(new TealiumEvent("Start Trace"));
        }
   }
   render() {
      return (
         <View style = {styles.inputContainer}>
            <TextInput style = {styles.input}
               textAlign={'center'}
               underlineColorAndroid = "transparent"
               placeholder = "ENTER TRACE ID"
               placeholderTextColor = "#007CC1"
               autoCapitalize = "none"
               onChangeText = {this.handleTraceId}/>
            <TouchableOpacity style={styles.buttonContainer} onPress={() => this.joinTrace(this.state.traceId)}>
                <Text style={styles.textStyle}>START TRACE</Text>
            </TouchableOpacity>
            <View style={styles.space} />
            <TouchableOpacity style={styles.buttonContainer} onPress={this.leaveTrace}>
                <Text style={styles.textStyle}>LEAVE TRACE</Text>
            </TouchableOpacity>
         </View>
      )
   }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
        paddingTop: Platform.OS === 'android' ? 25 : 0
    },
    inputContainer: {
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    sectionTitle: {
        fontSize: 24,
        fontWeight: '600',
        color: '#000000',
    },
    buttonContainer: {
        justifyContent: 'center',
        alignItems: 'center',
        width: 270,
        height: 60,
        backgroundColor: '#007CC1',
        borderRadius: 10,
        padding: 10,
        shadowColor: '#000000',
        shadowOffset: {
            width: 0,
            height: 3
        },
        shadowRadius: 10,
        shadowOpacity: 0.25
    },
    textStyle: {
        color: '#fff',
        textAlign: 'center'
    },
    input: {
      marginTop: 20,
      marginBottom: 20,
      width: 270,
      height:60,
      borderColor: '#007CC1',
      borderWidth: 1,
      borderRadius: 10
   },
    space: {
        width: 20,
        height: 20,
    },
    scrollView: {
        flex: 1,
        margin: 20
    }
});