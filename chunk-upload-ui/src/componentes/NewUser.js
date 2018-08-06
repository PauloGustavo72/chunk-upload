import React, {Component}  from 'react';
import {browserHistory} from  'react-router';
import axios from 'axios';

export default class NewUser extends Component{

    create(event){
        event.preventDefault();

        axios({
            method: 'post',
            url: 'http://localhost:8080/api/newAccount',
            data: JSON.stringify({username:this.login.value, password:this.senha.value}),
            headers: {
                'Content-type' : 'application/json'
            }
        }).then(response => {
            if(response.status === 200) {
                browserHistory.push('/');
            }
        }).catch(error => {
            this.setState({msg: error.message});
        })
    }

    render(){
        return(
            <div>
                <form onSubmit={this.create.bind(this)}>                    
                    <input type="text" ref={(input) => this.login = input} placeholder="Login"/>
                    <input type="password" ref={(input) => this.senha = input} placeholder="Password"/>
                    <input type="submit" value="Create"/>
                </form>
            </div>
        )
    }

}