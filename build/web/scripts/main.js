$(document).ready(() => {
    signUp();
    login();
})

function validateJSON(data) {
    for (let params in data) {
        let isEmpty = ((data[params].length == 0) ? true : false);
        if (isEmpty) {
            return isEmpty; //its ok
            break;
        }
    }
}

function generateData(id,sv,callback) {
    let inputs = document.getElementsByClassName(id);
    let data = new Object(); //we create the empty json
    let propsName;
    let propsValue;
    for (let i = 0; i < inputs.length; i++) {
        propsName = inputs[i].id;
        propsValue = inputs[i].value;
        data["" + propsName + ""] = propsValue;
    }
    if (!validateJSON(data)) {
        //console.log("JSON STATUS: OK");
        xhrConnection(data,sv,callback);
    } else {
        console.log("JSON STATUS: EMPTY");
    }
}

function signUp() {
    $("#signup-btn").click((e) => {
        let name = $("#name").val();
        let lastname = $("#lastname").val();
        let email = $("#email").val();
        let user = $("#username").val();
        let pw = $("#password").val();
        let data = {
            name: name,
            lastname: lastname,
            email: email,
            username: user,
            password: pw
        }
        console.log(data);
        if (!validateJSON(data)) {
            xhrConnection(data, "SignUp");
        } else {
            console.log("JSON IS EMPTY BRUH");
        }
        e.preventDefault;
    })
}

function login() {
    $("#login-btn").click((e) => {
        generateData("login-form","Login",()=>{
            if(data.success){
                alert("OK TODO BIEN ESTAS LOGGEADO");
            }else{
                alert("NO ESTAS REGISTRADO. REGISTRATE BRO")
            }
        });
    });
}