/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component, Fragment} from 'react';
import {Platform, ScrollView, StyleSheet, Text, View, Image, Button, ActivityIndicator} from 'react-native';
import axios from "axios";
import HyperLink from "./HyperLink";

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
      'Double tap R on your keyboard to reload,\n' +
      'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {

  state = {
    isLoad: false,
    data: [],
    since: 0
  };

  componentDidMount() {
    this.loadUsers();
  }

  loadUsers = () => {
    this.setState({isLoad: true});
    axios.get("https://api.github.com/users?since="+this.state.since)
        .then(r => {
          this.setState({isLoad: false, data: this.state.data.concat(r.data), since: r.data[29].id})
        })
        .catch(err => {
          this.setState({isLoad: false});
        })
  };

  render() {
    const {data, isLoad} = this.state;
    return (
        <View style={styles.container}>
          <Text style={styles.header}>Github users list</Text>
          <ScrollView>
            {data.map((value) => (
                <View key={value.login} style={styles.listItem}>
                  <Image source={{uri: value.avatar_url}} style={{width: 50, height: 50, marginRight: 5}}/>
                  <View style={styles.descriptionColumn}>
                    <Text><Text style={styles.boldText}>id:</Text> {value.id}</Text>
                    <Text><Text style={styles.boldText}>login:</Text> {value.login}</Text>
                    <Text><Text style={styles.boldText}>url:</Text> <HyperLink url={value.url} title={value.url}/></Text>
                  </View>
                </View>
            ))}
            {!isLoad?(
                <Button title="LOAD MORE" onPress={this.loadUsers} style={styles.button}/>
            ):(
                <View style={{flex: 1, justifyContent: "center"}}><ActivityIndicator size={"large"}/></View>
            )}
          </ScrollView>
        </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF',
    margin: 10
  },
  boldText: {
    fontWeight: "600"
  },
  header: {
    fontSize: 20,
    fontWeight: "600"
  },
  listItem: {
    flex: 1,
    flexDirection: "row",
    margin: 5

  },
  descriptionColumn: {
    flex: 1,
    flexDirection: "column",
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  button: {
    height: 10,
    width: 100
  }
});
