import React, {Component} from 'react';
import {browserHistory} from  'react-router';
import axios from 'axios';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {msg: this.props.location.query.msg}
    }

    makeLogin(event) {
        event.preventDefault();

        axios({
            method: 'post',
            url: 'http://localhost:8080/api/public/login',
            data: JSON.stringify({login:this.login.value, password:this.senha.value}),
            headers: {
                'Content-type' : 'application/json'
            }
        }).then(response => {
            if(response.status === 200) {
                return response.data;                    
            } else {
                throw new Error('Cannot make login!');
            }
        }).then(token => {
            localStorage.setItem('auth-token', token);
            browserHistory.push('/home');
        }).catch(error => {
            let msg = error.message;
            if (error.response.status === 401)
                msg = "User not found!";
            this.setState({msg: msg});
        })
    }

    newUser(){
        browserHistory.push('/newUser');
    }

    render() {
        return(
            <div className="login-box">
                <h1>Chunk Upload</h1>
                <span>{this.state.msg}</span>
                <form onSubmit={this.makeLogin.bind(this)}>
                    <input type="text" ref={(input) => this.login = input} placeholder="Login"/>
                    <input type="password" ref={(input) => this.senha = input} placeholder="Password"/>
                    <input type="submit" value="login"/>
                </form>
                <button onClick={this.newUser.bind(this)}>New User</button>
            </div>
        );
    }

}