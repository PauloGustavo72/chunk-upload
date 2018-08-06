import React from 'react';
import ReactDOM from 'react-dom';
import Home from './componentes/Home';
import Login from './componentes/Login';
import NewUser from './componentes/NewUser';
// import './css/reset.css';
// import './css/login.css';
import {Router,Route,browserHistory} from 'react-router';


function checkAuthenticated(nextState, replace) {
    if (localStorage.getItem('auth-token') === null) {
        replace('/?msg=You need be logged to access!');
    }
}

ReactDOM.render(
    (
        <Router history={browserHistory}>
            <Route path='/' component={Login} />
            <Route path='/newUser' component={NewUser} />
            <Route path='/home(/:login)' component={Home} onEnter={checkAuthenticated}/>
        </Router>    
    ), document.getElementById('root')
);
