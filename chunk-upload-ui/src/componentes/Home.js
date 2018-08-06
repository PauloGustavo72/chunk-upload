import React, { Component } from 'react';
import SplitFile from "js-split-file/react-native";
import ListUploads from './ListUploads.js';
import axios from 'axios';

export default class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {file:{}, buff:{}, uploads:[]}
    }

    componentWillMount(){
        this.refreshUploads();
    }

    handleChange(event) {
        let reader = new FileReader();
        reader.readAsArrayBuffer(event.target.files[0]);
        this.setState({file: event.target.files[0], buff: reader})
    }

    onClick(){
        let _1MB = 1024 * 1024,
            splitFile = new SplitFile(),
            binary = new Uint8Array(this.state.buff.result),
            blocks = splitFile.split(binary, this.state.file.name, _1MB);
            
        blocks.forEach(block => {
            let piece = splitFile.decodeBlock(block),
                form = new FormData();

            form.append('file', new Blob([piece.binary]));
            form.append('name', this.state.file.name);
            form.append('index', piece.currentBlockIndex);
            form.append('totalParts', piece.totalBlockCount);
            form.append('totalFileSize', piece.totalBytesCount);

            axios({
                method: 'post',
                url: 'http://localhost:8080/api/upload',
                headers: {
                    'Content-Type': 'multipart/form-data', 
                    'X-AUTH-TOKEN': localStorage.getItem('auth-token')},
                data: form
            }).then((response) => {
                this.refreshUploads();
            }).catch(error => {
                console.error(error);
                this.setState({msg:error.message})
            })
            this.refreshUploads();
        });
    }

    refreshUploads(){
        axios({
            method: 'get',
            url: 'http://localhost:8080/api/list',
            headers: {
                'X-AUTH-TOKEN': localStorage.getItem('auth-token')
            }
        }).then((response) => {
            this.setState({uploads: response.data})
        })
    }

    render () {
        return (
            <div>
                <input type="file" ref={(input) => this.file = input} onChange={this.handleChange.bind(this) } />
                <button onClick={this.onClick.bind(this)}>Enviar</button>
                <ListUploads uploads={this.state.uploads} />
            </div>
        );
    }
}