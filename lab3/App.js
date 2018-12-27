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
import SQLite from 'react-native-sqlite-storage';
import RNFetchBlob from 'rn-fetch-blob'

SQLite.DEBUG(true);
SQLite.enablePromise(true);
SQLite.enablePromise(false);

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
    isSaveToDb: false,
    throwInternet: false,
    data: [],
    since: 0
  };

  componentDidMount() {
  }

  loadUsers = () => {
    this.setState({isLoad: true});
    axios.get("https://api.github.com/users?since="+this.state.since)
        .then(r => {
          this.setState({isLoad: false, data: r.data, since: r.data[29].id})
        })
        .catch(err => {
          this.setState({isLoad: false});
        })
  };

  loadFromDb = () => {
    let db = SQLite.openDatabase({ name: 'mydb', createFromLocation: "~db.db"});
    db.transaction(tx => {
      tx.executeSql("select * from users", [], (tx, results) => {
        let len = results.rows.length;
        let data = [];
        for (let i = 0; i < len; i++) {
          let row = results.rows.item(i);
          data.push({id: row.id, login: row.login, url: row.url, avatar_url: "data:image/png;base64," + row.photo});
        }
        this.setState({data})
      });
    })
  };


  saveToDb = () => {
    const {data} = this.state;
    this.setState({isSaveToDb: true});
    let db = SQLite.openDatabase({ name: 'mydb', createFromLocation: "~db.db"});
    db.transaction(tx => {
      tx.executeSql("delete from users");
    });

    let i = 0;

    data.forEach((el) => {
      RNFetchBlob.fetch("GET", el.avatar_url).then((res) => {
        db.transaction(tx => {
          i++;
          tx.executeSql("insert into users(id, login, url, photo) values(" + el.id + ",\'" + el.login + "\',\'" + el.url + "\',\'" + res.base64() + "\')");
          if(i === 29) this.setState({isSaveToDb: false, data: [], throwInternet: false, since: 0})
        });
      });
    });

  };

  render() {
    const {data, isLoad, throwInternet} = this.state;
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
            {!isLoad ?(
              throwInternet && <View style={styles.button}><Button title="NEXT" onPress={this.loadUsers} style={styles.button}/></View>
            ):(
                <View style={{flex: 1, justifyContent: "center"}}><ActivityIndicator size={"large"}/></View>
            )}
            {this.state.isSaveToDb ? (
              <View style={{flex: 1, justifyContent: "center"}}><ActivityIndicator size={"large"}/></View>
            ) : (
              data.length > 0 && throwInternet && <View style={styles.button}><Button onPress={this.saveToDb} title={"SAVE TO DB"}/></View>
            )}

            {data.length === 0 && <View style={styles.button}><Button title={"LOAD FROM DB"} onPress={this.loadFromDb}/></View>}
            {!throwInternet && <View style={styles.button}><Button onPress={() => {this.setState({throwInternet: true}); this.loadUsers()}} title={"LOAD FROM GITHUB"}/></View>}
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
    flex: 1,
    alignSelf: "stretch",
    justifyContent: 'center',
    alignItems: 'stretch',
    margin: 5
  }
});
