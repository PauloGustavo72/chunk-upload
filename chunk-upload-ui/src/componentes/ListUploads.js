import React, { Component } from 'react';
import axios from 'axios';
import FileSaver from 'file-saver/FileSaver';

export default class ListUploads extends Component {

    onClick(upload){
        axios({
            method: 'get',
            url: 'http://localhost:8080/api/download/' + upload.id,
            headers: {
                'X-AUTH-TOKEN': localStorage.getItem('auth-token')
            },
            responseType: 'blob'
        }).then((response) => {
            FileSaver.saveAs(new Blob([response.data]), upload.fileName);
        }).catch((error) => {
            console.log(error)
        });
    }

    formatDate(date){
        return date[1] + '/' + date[2] + '/' + date[0] + ' ' + date[3] + ':' + date[4] + ':' + date[5] + ':' + date[6];
    }

    render() {
        return(
            <div>
                <table border="1">
                    <tr>
                        <th>Username</th>
                        <th>File name</th>
                        <th>Status</th>
                        <th>Start of upload</th>
                        <th>End of upload</th>
                        <th>File size</th>
                        <th>Amount chunks</th>
                        <th></th>
                    </tr>
                {
                    this.props.uploads.map(upload => 
                        <tr key={upload.id}>
                            <td>{upload.user.username}</td>
                            <td>{upload.fileName}</td>
                            <td>{upload.status}</td>
                            <td>{this.formatDate(upload.startUpload)}</td>
                            <td>{this.formatDate(upload.endUpload)}</td>
                            <td>{upload.fileSize}</td>
                            <td>{upload.chunks}</td>
                            <td><button  onClick={this.onClick.bind(this, upload)}>Download</button></td>
                        </tr>
                    )
                }
                </table>
            </div>
        )
    }

}