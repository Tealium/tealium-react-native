/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { Component, useState } from 'react';
import {
    Platform, Button, StyleSheet, Text, TextInput, View, TouchableOpacity, Alert, ScrollView,
    SafeAreaView
} from 'react-native';
import Tealium from 'tealium-react-native';
import TealiumLocation from 'tealium-react-native-location';
import TealiumAdobeVisitor from 'tealium-react-native-adobe-visitor';
import { TealiumLocationConfig, Accuracy, DesiredAccuracy } from 'tealium-react-native-location/common';
import { TealiumAdobeVisitorConfig } from 'tealium-react-native-adobevisitor/common';
import {
    TealiumConfig, TealiumView, TealiumEvent, ConsentCategories, Dispatchers, Collectors,
    ConsentPolicy, Expiry, ConsentExpiry, TimeUnit, ConsentStatus, TealiumEnvironment, RemoteCommand
} from
    'tealium-react-native/common';
import FirebaseRemoteCommand from 'tealium-react-firebase';
import BrazeRemoteCommand from 'tealium-react-braze';
import AdjustRemoteCommand from 'tealium-react-adjust';
import { AdjustConfig, AdjustEnvironemnt } from 'tealium-react-adjust/common';
import { checkAndRequestPermissions } from "./Utils"
import { AuthState } from 'tealium-react-native-adobe-visitor/common';

export default class App extends Component<{}> {

    componentDidMount() {
        let adobeVisitorConfig: TealiumAdobeVisitorConfig = {
            adobeVisitorOrgId: "<YOUR-ADOBE-ORG-ID>",
            adobeVisitorRetries: 1
        }
        
        let locationConfig: TealiumLocationConfig = {
            accuracy: Accuracy.high,
            desiredAccuracy: DesiredAccuracy.best,
            updateDistance: 150
        }

        let adjustConfig: AdjustConfig = {
            appToken: "someToken",
            environment: AdjustEnvironemnt.sandbox,
            allowSuppressLogLevel: false
        }

        TealiumAdobeVisitor.configure(adobeVisitorConfig)
        TealiumLocation.configure(locationConfig);
        FirebaseRemoteCommand.initialize();
        BrazeRemoteCommand.initialize();
        AdjustRemoteCommand.initialize(adjustConfig);
        let config: TealiumConfig = {
            account: 'tealiummobile',
            profile: 'demo',
            environment: TealiumEnvironment.dev,
            dispatchers: [
                Dispatchers.Collect,
                Dispatchers.TagManagement,
                Dispatchers.RemoteCommands
            ],
            collectors: [
                Collectors.AppData,
                Collectors.DeviceData,
                Collectors.Lifecycle,
                Collectors.Connectivity
            ],
            lifecycleAutotrackingEnabled: true,
            consentLoggingEnabled: true,
            consentExpiry: {
                'time': 10,
                'unit': 'days'
            },
            consentPolicy: ConsentPolicy.gdpr,
            batchingEnabled: false,
            visitorServiceEnabled: true,
            useRemoteLibrarySettings: false,
            remoteCommands: [{
                id: FirebaseRemoteCommand.name,
                path: "firebase.json"
            }, {
                id: BrazeRemoteCommand.name,
                path: 'braze.json'
            }, {
                id: AdjustRemoteCommand.name,
                path: 'adjust.json'
            }],
            visitorIdentityKey: DataLayer.UserIdentity
        };
        Tealium.initialize(config, success => {
            if (!success) {
                console.log("Tealium not initialized")
                return
            }
            console.log("Tealium initialized")
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
        Tealium.setVisitorIdListener(id => {
            console.log("Visitor Id Updated: " + id);
        });
        Tealium.setConsentExpiryListener(() => {
            console.log("Consent Expired");
        });
    }

    trackEvent() {
        let event = new TealiumEvent('Test Event', { [DataLayer.EventName]: 'test' });
        Tealium.track(event);
    }

    trackView() {
        let view = new TealiumView('Test View', { [DataLayer.ViewName]: 'test' });
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
        data[DataLayer.TestSessionData] = 'test'
        data[DataLayer.MyTestValue] = 1;
        Tealium.addData(data, Expiry.session);
    }

    getData() {
        Tealium.getData(DataLayer.TestSessionData, value => {
            console.log(`${DataLayer.TestSessionData}: ${value}`)
        })
    }

    gatherTrackData() {
        Tealium.gatherTrackData(value => {
            console.log("Track data: " + JSON.stringify(value))
        })
    }

    removeData() {
        Tealium.removeData([DataLayer.TestSessionData]);
        Tealium.removeData([DataLayer.MyTestValue]);
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

    resetVisitorId() {
        Tealium.resetVisitorId()
    }

    clearStoredVisitorIds() {
        Tealium.clearStoredVisitorIds()
    }

    getSessionId() {
        Tealium.getSessionId(value => {
            console.log("Session id: " + value)
            Alert.alert("Session Id: ", value, [{ text: "OK", style: "cancel" }])
        });
    }

    terminate() {
        Tealium.terminateInstance();
    }

    async startLocationTracking() {
        let result = await checkAndRequestPermissions()
        if (!result) return;

        TealiumLocation.startLocationTracking();
    }

    async stopLocationTracking() {
        let result = await checkAndRequestPermissions()
        if (!result) return;

        TealiumLocation.stopLocationTracking();
    }

    async getLastLocation() {
        let result = await checkAndRequestPermissions()
        if (!result) return;

        TealiumLocation.lastLocation((loc) => {
            if (loc) {
                Alert.alert(`Lat: ${loc.lat} | Lng: ${loc.lng}`)
            }
        });
    }

    linkExistingAdobeVisitor (id, providerId, authState) {
        if(authState == null) {
            TealiumAdobeVisitor.linkExistingEcidToKnownIdentifier(
                id, providerId, -1, value => {
                    console.log("AdobeVisitor Data: " + value.stringify)
                }
            );
        } else {
            TealiumAdobeVisitor.linkExistingEcidToKnownIdentifier(
                id, providerId, parseInt(authState), value => {
                    console.log("AdobeVisitor Data: " + JSON.stringify(value))
                }
            );
        }
    }

    getCurrentAdobeVisitor() {
        TealiumAdobeVisitor.getAdobeVisitor(value => {
            console.log("Current Adobe Visitor: " + JSON.stringify(value))
        });
    }

    decorateUrl() {
        TealiumAdobeVisitor.decorateUrl("https://tealium.com", value => {
            console.log("Decorated URL: " + value)
            Alert.alert("Decorated URL: ", value, [{ text: "OK", style: "cancel" }])
        });
    }

    resetAdobeVisitor() {
        TealiumAdobeVisitor.resetVisitor();
    }

    async getLastIdentity() {
        return new Promise(resolve => {
            Tealium.getData((data) => {
                resolve(data)
            });
        })
    }

    joinTrace = (id) => {
        if (id !== '') {
            Alert.alert('Trace started with ID: ' + id);
            Tealium.joinTrace(id);
            Tealium.track(new TealiumEvent("Start Trace"));
        }
    }

    leaveTrace = () => {
        Tealium.leaveTrace();
    }

    identify(identity) {
        if (identity) {
            Tealium.addData({
                [DataLayer.UserIdentity]: identity
            }, Expiry.forever)
        }
    }

    getButtonsForSection(sectionFilter) {
        let filter = sectionFilter || Sections.Misc

        return this.getButtonActions()
            .filter((action) => {
                return action.section === filter
                    || (action.section === undefined && filter === Sections.Misc)
            });
    }

    getButtonActions() {
        return [
            { section: Sections.Tracking, text: "TRACK VIEW", onPress: this.trackView },
            { section: Sections.Tracking, text: "TRACK EVENT", onPress: this.trackEvent },
            { section: Sections.Consent, text: "OPT IN", onPress: this.optIn },
            { section: Sections.Consent, text: "OPT OUT", onPress: this.optOut },
            { section: Sections.Consent, text: "GET CONSENT STATUS", onPress: this.getConsentStatus },
            { section: Sections.Consent, text: "GET CONSENT CATEGORIES", onPress: this.getConsentCategories },
            { section: Sections.Consent, text: "RESET CONSENT", onPress: this.resetConsent },
            { section: Sections.Consent, text: "SET CONSENT CATEGORIES", onPress: this.setRandomConsentCategories },
            { section: Sections.DataLayer, text: "ADD DATA", onPress: this.addData },
            { section: Sections.DataLayer, text: "GET DATA", onPress: this.getData },
            { section: Sections.DataLayer, text: "GATHER TRACK DATA", onPress: this.gatherTrackData },
            { section: Sections.DataLayer, text: "REMOVE DATA", onPress: this.removeData },
            { section: Sections.RemoteCommand, text: "ADD REMOTE COMMAND", onPress: this.addRemoteCommand },
            { section: Sections.RemoteCommand, text: "REMOVE REMOTE COMMAND", onPress: this.removeRemoteCommand },
            { section: Sections.Visitor, text: "GET VISITOR ID", onPress: this.getVisitorId },
            { section: Sections.Visitor, text: "RESET VISITOR ID", onPress: this.resetVisitorId },
            { section: Sections.Visitor, text: "CLEAR STORED VISITOR IDS", onPress: this.clearStoredVisitorIds },
            { section: Sections.Misc, text: "GET SESSION ID", onPress: this.getSessionId },
            { section: Sections.Misc, text: "DISABLE TEALIUM", onPress: this.terminate },
            { section: Sections.Location, text: "GET LOCATION", onPress: this.getLastLocation },
            { section: Sections.Location, text: "START TRACKING LOCATION", onPress: this.startLocationTracking },
            { section: Sections.Location, text: "STOP TRACKING LOCATION", onPress: this.stopLocationTracking },
            { section: Sections.AdobeVisitorService, text: "GET CURRENT ADOBE VISITOR", onPress: this.getCurrentAdobeVisitor },
            { section: Sections.AdobeVisitorService, text: "DECORATE URL", onPress: this.decorateUrl },
            { section: Sections.AdobeVisitorService, text: "RESET ADOBE VISITOR", onPress: this.resetAdobeVisitor },
        ]
    }

    render() {
        return (
            <SafeAreaView style={styles.container}>
                <Text style={styles.sectionTitle}> Tealium React Native </Text>
                <View style={{ flexDirection: 'row' }}>
                    <ScrollView style={styles.scrollView}>
                        <Section text={Sections.Tracking}>
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.Tracking)} />
                        </Section>
                        <Section text={Sections.Trace}>
                            <Trace joinTrace={this.joinTrace} leaveTrace={this.leaveTrace} />
                        </Section>
                        <Section text={Sections.Consent}>
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.Consent)} />
                        </Section>
                        <Section text={Sections.DataLayer}>
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.DataLayer)} />
                        </Section>
                        <Section text={Sections.RemoteCommand}>
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.RemoteCommand)} />
                        </Section>
                        <Section text={Sections.Visitor}>
                            <ActionTextField buttonText="Login" placeholder="Set Identity" action={this.identify} />
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.Visitor)} />
                        </Section>
                        <Section text={Sections.Location}>
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.Location)} />
                        </Section>
                        <Section text={Sections.AdobeVisitorService}>
                            <AdobeVisitor action={this.linkExistingAdobeVisitor} />
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.AdobeVisitorService)} />
                        </Section>
                        <Section text={Sections.Misc}>
                            <TealiumButtonList actions={this.getButtonsForSection(Sections.Misc)} />
                        </Section>
                    </ScrollView>
                </View>
            </SafeAreaView>
        );
    }

    componentWillUnmount() {
        Tealium.removeListeners();
    }

}

const Trace = (props) => {
    return (
        <View style={styles.inputContainer}>
            <ActionTextField placeholder="ENTER TRACE ID" buttonText="START TRACE" action={props.joinTrace} />
            <View style={styles.space} />
            <TealiumButton text="LEAVE TRACE" onPress={props.leaveTrace} />
        </View>
    )
}

const AdobeVisitor = (props) => {
    const [inputVisitorIdText, setVisitorIdText] = useState()
    const [inputDataProviderText, setDataProviderText] = useState()
    const [inputAuthStateText, setAuthStateText] = useState()
    return (
        <View style={styles.inputContainer}>
            <TextInput style={styles.input}
                textAlign={'center'}
                underlineColorAndroid="transparent"
                placeholder="ENTER KNOWN VISITOR ID"
                placeholderTextColor="#007CC1"
                autoCapitalize="none"
                onChangeText={(id) => setVisitorIdText(id)} />
            <TextInput style={styles.input}
                textAlign={'center'}
                underlineColorAndroid="transparent"
                placeholder= "ENTER DATA PROVIDER"
                placeholderTextColor="#007CC1"
                autoCapitalize="none"
                onChangeText={(dataProvider) => setDataProviderText(dataProvider)} />
            <TextInput style={styles.input}
                textAlign={'center'}
                underlineColorAndroid="transparent"
                placeholder= "(OPTIONAL) ENTER AUTH STATE" 
                placeholderTextColor="#007CC1"
                autoCapitalize="none"
                onChangeText={(authState) => setAuthStateText(authState)} />
            <TealiumButton text="LINK ADOBE VISITOR" onPress={() => {
                let id = inputVisitorIdText;
                let dataProvider = inputDataProviderText
                let authState = inputAuthStateText
                if (authState) {
                    props.action(id, dataProvider, authState)
                } else {
                    props.action(id, dataProvider, undefined)
                }
            }} />
        </View>
    )
}

const ActionTextField = (props) => {
    const [inputText, setInputText] = useState()

    return (
        <View style={styles.inputContainer}>
            <TextInput style={styles.input}
                textAlign={'center'}
                underlineColorAndroid="transparent"
                placeholder={props.placeholder}
                placeholderTextColor="#007CC1"
                autoCapitalize="none"
                onChangeText={(text) => setInputText(text)} />
            <TealiumButton text={props.buttonText} onPress={() => {
                let text = inputText;
                if (text) {
                    props.action(text)
                }
            }} />
        </View>
    )
}

const TealiumButtonList = (props) => {
    return (
        <View style={styles.inputContainer} key={props.actions.key}>
            {
                props.actions.map((object, i) => <View key={object.text}>
                    <View style={styles.space} />
                    <TealiumButton text={object.text} onPress={object.onPress} />
                </View>)
            }
        </View>
    )
}

const TealiumButton = (props) => {
    return (<TouchableOpacity style={styles.buttonContainer} onPress={props.onPress}>
        <Text style={styles.textStyle}>{props.text}</Text>
    </TouchableOpacity>)
}

const Section = (props) => {
    let color = props.color || "black";
    let text = props.text || "Section";

    return (<>
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <View style={{ flex: 1, height: 1, backgroundColor: color }} />
            <View>
                <Text style={{ width: 125, textAlign: 'center', fontSize: 24 }}>{text}</Text>
            </View>
            <View style={{ flex: 1, height: 1, backgroundColor: color }} />
        </View>
        {props.children}
        <View style={styles.space} />
    </>)
}

const DataLayer = {
    EventName: "event_name",
    MyTestValue: "my_test_value",
    TestSessionData: "test_session_data",
    UserIdentity: "user_identity",
    ViewName: "view_name",
}

const Sections = {
    Trace: "Trace",
    Tracking: "Tracking",
    Consent: "Consent",
    Visitor: "Visitor",
    Location: "Location",
    DataLayer: "DataLayer",
    RemoteCommand: "Remote Commands",
    AdobeVisitorService: "Adobe Visitor Service",
    Misc: "Misc"
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
        height: 60,
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
