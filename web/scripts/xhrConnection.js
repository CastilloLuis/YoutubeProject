function xhrConnection(data,sv,callback) {
    let xhr = new XMLHttpRequest();
    return new Promise((res, rej) => {        
        xhr.open("POST",sv,true); //its ok
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onload = () => {
            if (xhr.status == 200) {
                //the request was OK
                res(xhr.response);
            } else {
                rej(new Error(xhr.statusText));
            }
        };
        xhr.onerror = () => {
            rej(new Error("Error de conexión"));
        };
        xhr.send(JSON.stringify(data));
    })
    .then((data)=>{
        let mydata = JSON.parse(data);
        console.log(data);
        console.log(mydata);
        console.log(mydata.success);
        callback(data.success); //to resolve
    });
}





