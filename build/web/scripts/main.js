$(document).ready(() => {
    $('.modal').modal();
    signUp();
    login();
    formDataUpload();
    userLikes();
    userComments();
    console.log("is admin: status ="+localStorage.getItem("isadmin"));
});

// START OF: HELPER FUNCTIONS //
function validateJSON(data) {
    for (let params in data) {
        let isEmpty = ((data[params].length == 0) ? true : false);
        if (isEmpty) {
            return isEmpty; //its ok
            break;
        }
    }
}

function generateData(classname) {
    let inputs = document.getElementsByClassName(classname);
    let data = new Object(); //we create the empty json (data={})
    let propsName;
    let propsValue;
    for (let i = 0; i < inputs.length; i++) {
        propsName = inputs[i].id;
        propsValue = inputs[i].value;
        data["" + propsName + ""] = propsValue;
    }
    return data;
}

const clearfields = (id) => $("#" + id + "").val("");

const storageData = (key, value) => localStorage.setItem(key, value);

const fdResponse = (data) => ans = ((data.success) ? true : false);

const setTagProps = (id, text) => document.getElementById(id).innerHTML = text;

const setSrc = (id, src) => document.getElementById(id).src = src;

const setHref = (id, href) => document.getElementById(id).href = href;

const showtoast = (message,iconcolor,icon) => M.toast({html: `<p>${message}<i class="material-icons right prefix" style="color: ${iconcolor};">${icon}</i><p>`,classes: 'rounded'});

const preloader = (time,playvideo,videoid) => {
    $("body").css("pointer-events", "none");
    $("#modal-preloader").modal("open");
    if(playvideo){
        setTimeout(() => {
            document.getElementById(videoid).play();
            $("#modal-preloader").modal("close");
            $("body").css("pointer-events", "auto");
        }, time);
    }else{
        setTimeout(() => {
            $("#modal-preloader").modal("close");
            $("body").css("pointer-events", "auto");
        }, time);        
    }
}
// END OF: HELPER FUNCTIONS //

function signUp() {
    let data;
    let mydata;
    let xhr = new XMLHttpRequest();
    $("#signup-btn").click((e) => {
        data = generateData("signup-form");
        console.log(data);
        let formData = new FormData();
        if (!validateJSON(data)) {
            formData.append("file", $("#myfile")[0].files[0]);
            formData.append("name", data.name);
            formData.append("lastname", data.lastname);
            formData.append("email", data.email);
            formData.append("username", data.usernamesignup);
            formData.append("password", data.passwordsignup);
            formData.append("bio", data.bio);
            if(($("#myfile").val().indexOf(".png")==-1) && ($("#myfile").val().indexOf(".jpg")==-1)){
                showtoast("The file is not an image. Just allowed { png,jpg }. Choose a correct file.","red","close");
            }else{
                xhr.onload = () => {
                    if ((xhr.status === 200) && (xhr.readyState === 4)) {
                        mydata = JSON.parse(xhr.response);
                        if(mydata.status===200){
                            showtoast("Registration successful!!","green","check");
                            setTimeout(() => {$("#modal-signup").modal("close");},1000);
                        }
                    }
                };
                e.preventDefault();
                xhr.open("POST", "SignUp", true);
                xhr.send(formData);  
            }
        } else {
            showtoast("You have to fill al the fields!","red","close");
        }
    });
}

function login() {
    let data;
    $("#login-btn").click((e) => {
        data = generateData("login-form");
        if (!validateJSON(data)) {
            //console.log("JSON STATUS: OK");
            xhrConnection("POST", data, "Login", (mydata) => { //mydata is what I got from my xhrConnection
                if (mydata.success) {
                        storageData("isadmin",mydata.isadmin);
                        storageData("username", mydata.username);
                        storageData("id", mydata.id);
                        window.top.location.href = "user/user-index.html";                        
                } else {
                    showtoast("User/password incorrect.","red","close");
                }
            });
        } else {
            showtoast("You have to fill all the fields.","red","close");
        }
    });
}

function logout() {
    localStorage.removeItem("username");
    localStorage.removeItem("id");
    localStorage.removeItem("isadmin");
    localStorage.removeItem("helperid");
    window.top.location.href = "../LogOut";
}

function formDataUpload() {
    let xhr = new XMLHttpRequest();
    $("#upload-btn").click((e) => {
        let data;
        let formData = new FormData();
        let videoName = $("#title").val();
        let description = $("#description").val();
        formData.append("file", $("#file")[0].files[0]);
        formData.append("title", videoName);
        formData.append("description", description);
        formData.append("tagid", $("#select-tag").val());
        if ((videoName == "") || (description == "")) {
            showtoast("You have to fill all the fields.","red","close");
        } else {
            $("#myprogressbar").fadeIn("slow");
            xhr.onload = () => {
                if ((xhr.status === 200) && (xhr.readyState === 4)) {
                    $("#myprogressbar").fadeOut("slow");
                    console.log(xhr.response);
                    data = JSON.parse(xhr.response);
                    if (fdResponse(data)) {
                        showtoast("The file was successfully uploaded!!","green","check");
                        clearfields("title");
                        clearfields("description");
                        clearfields("file");
                        setTimeout(() => {location.reload()},2000);
                    } else {
                        showtoast("This file is not allowed.","red","close");
                    }
                }
            };
        }
        e.preventDefault();
        xhr.open("POST", "../UploadFile", true);
        xhr.send(formData);
    });
}

function getVideos(isvisitor) {
    var id1, id2, id3;
    xhrConnection("GET", null, "../GetVideos", (mydata) => {
        if (mydata.status == 200) {
            //console.log(JSON.stringify(mydata));
            id1 = mydata.videoid1;
            id2 = mydata.videoid2;
            id3 = mydata.videoid3;
            setSrc("recent-video-video1", "../Streaming?id=" + mydata.videoid1);
            setSrc("recent-video-video2", "../Streaming?id=" + mydata.videoid2);
            setSrc("recent-video-video3", "../Streaming?id=" + mydata.videoid3);
            setHref("recent-video-link1", "../user/videos-page.html?id=" + mydata.videoid1);
            setHref("recent-video-link2", "../user/videos-page.html?id=" + mydata.videoid2);
            setHref("recent-video-link3", "../user/videos-page.html?id=" + mydata.videoid3);
            userIndex(id1, "recent-video-title1", "recent-video-desc1", "username1", isvisitor);
            userIndex(id2, "recent-video-title2", "recent-video-desc2", "username2", isvisitor);
            userIndex(id3, "recent-video-title3", "recent-video-desc3", "username3", isvisitor);
        } else {
            console.log("ERROR AL BUSCAR VIDEO" + mydata);
        }
    });
}

function userIndex(videoid, id1, id2, id3, isvisitor) {
    data = {};
    data.id = videoid;
    data.isvisitor = isvisitor;
    $.ajax({
            type: "GET",
            data: data,
            url: "../GetVideoStats"
        })
        .then((data) => {
            let mydata = JSON.parse(data);
            document.getElementById(id1).innerHTML = mydata.name;
            document.getElementById(id2).innerHTML = mydata.description;
            document.getElementById(id3).innerHTML = "Username: @" + mydata.username;
        })
}

function userLikes() {
    let likes = document.getElementById("video-likes");
    let myparams = new URLSearchParams(window.location.search);
    let data = {};
    data.id = myparams.get("id");
    $("#fav-icon").click(() => {
        xhrConnection("POST", null, "../LikeServlet?id=" + data.id, (mydata) => {
            if (mydata.success) {
                //console.log("EL VIDEO AHORA TIENE " + mydata.likes + " LIKES");
                likes.innerHTML = mydata.likes;
            } else if (!mydata.success && !mydata.likexists) {
                $("#fav-icon").css("color", "black");
                let act = document.getElementById("video-likes").innerHTML - 1;
                document.getElementById("video-likes").innerHTML = act;
            }
        });
    });
}

function userComments() {
    let data;
    let myparams = new URLSearchParams(window.location.search);
    $("#send-comment-btn").click((e) => {
        data = generateData("commentfield");
        data.id = myparams.get("id");
        //console.log(data);
        if (!validateJSON(data)) {
            xhrConnection("POST", data, "../CommentServlet", (mydata) => { //mydata is what I got from my xhrConnection
                //console.log("Success? = " + mydata.success);
                if (mydata.success) {
                    //console.log("agregado correctamente");
                    addComment((mydata.comment), (mydata.username), (mydata.date), localStorage.getItem("id"));
                    clearfields("commentfield");
                } else {
                    showtoast("The comment was not made. Try it again!","red","close");
                }
            });
        } else {
            showtoast("You have to fill all the fields","red","close");
        }
        e.preventDefault();
    });
}

function showComments() {
    let myparams = new URLSearchParams(window.location.search);
    let myid = myparams.get("id");
    console.log(myid);
    $.ajax({
        type: "GET",
        url: "../getComments?id=" + myid
    }).then((data) => {
        let mydata = JSON.parse(data);
        console.log(mydata);
        delete mydata["success"];
        for (let params in mydata) {
            addComment((mydata[params].comment_text), (mydata[params].username), (mydata[params].created_at), (mydata[params].id_user), (mydata[params].comment_id));
        }
    });
}

function showMostLiked() {
    document.getElementById("myvideos").src = "../Streaming?mostliked=true";
}

/* page for video*/
function showVideosPage(isvisitor) {
    //this will run on the videos-page.html file
    let myparams = new URLSearchParams(window.location.search);
    document.getElementById("myvideos").src = "../Streaming?id=" + myparams.get("id");
    console.log(document.getElementById("myvideos").src);

    let myurl = window.location.pathname;
    let data = {};
    if (myurl.indexOf("user-index.html") == -1) {
        data.mostliked = false;
        data.id = myparams.get("id");
        data.isvisitor = isvisitor;
    }
    console.log(data);
    $.ajax({
            type: "GET",
            data: data, //sended by url, not body
            contentType: "application/json",
            url: "../GetVideoStats"
        })
        .then((data) => {
            let mydata = JSON.parse(data);
            storageData("helperid",mydata.userid); //helper id is to save the id of the user that uploaded the video, to check it later & show or not the deletebtn
            console.log("LOCAL STORAGE MOMENT ID:"+localStorage.getItem("momentid"));
            if (localStorage.getItem("username") != null) {
                if (mydata.likexists) {
                    $("#fav-icon").css("color", "red");
                } else {
                    $("#fav-icon").css("color", "black");
                }
            }
            console.log(data);
            
            document.getElementById("deletevideo-btn").href = "../Delete?id="+myparams.get("id")+"&tn="+"mt";
            document.getElementById("title-video").innerHTML = mydata.name;
            document.getElementById("video-likes").innerHTML = mydata.likes;
            document.getElementById("video-comment").innerHTML = mydata.comments;
            document.getElementById("video-description").innerHTML = mydata.description;
            document.getElementById("video-date").innerHTML = mydata.date;
            document.getElementById("video-username").innerHTML = mydata.username;
            document.getElementById("myviews").innerHTML = mydata.mediaviews;
            document.getElementById("download-btn").href = "../DownloadFile?id=" + myparams.get("id");
            document.getElementById("user-comment-img").src = "../ProfileImage?id=" + mydata.userid;
            deletebutton(myparams.get("id"));
        })
        .fail((data) => {
            console.log("ERROR AL CONECTAR... " + data);
        });
}

/*PROFILE REQUEST*/
function userData() {
    var url1, url2;
    let myparams = new URLSearchParams(window.location.search);
    data = {};
    data.id = myparams.get("id");
    $.ajax({
            type: "GET",
            data: data,
            url: "../GetUserData"
        })
        .then((data) => {
            let mydata = JSON.parse(data);
            console.log(mydata);
            url1 = "../GetImage?url=" + mydata.bgpicture;
            console.log(url1);
            url2 = mydata.profilepicture;
            document.getElementById("name").innerHTML = mydata.name + " " + mydata.lastname;
            document.getElementById("email").innerHTML = mydata.email;
            document.getElementById("username").innerHTML = "@" + mydata.username;
            document.getElementById("bio").disabled = true;
            document.getElementById("bio").value = mydata.bio;
            document.body.style.backgroundImage = "url('../images/home-bg.jpg')";

            document.getElementById("user-profile-picture").src = "../GetImage?url=" + url2;
        });
}

function userVideos() {
    var url1, url2;
    let myparams = new URLSearchParams(window.location.search);
    data = {};
    data.id = myparams.get("id");
    $.ajax({
            type: "GET",
            data: data,
            url: "../GetMyVideos"
        })
        .then((data) => {
            let mydata = JSON.parse(data);
                for (let params in mydata) {
                    console.log(mydata[params]);
                    myVideos((mydata[params].media_name), mydata[params].media_id);
                }
        });
}

function dataSideBar() {
    let data = {};
    var url1;
    data.id = localStorage.getItem("id");
    $.ajax({
            type: "GET",
            data: data,
            url: "../GetUserData"
        })
        .then((data) => {
            let mydata = JSON.parse(data);
            url1 = "../GetImage?url=" + mydata.profilepicture;
            console.log(mydata);
            document.getElementById("user-bg-sidebar").src = url1;
            document.getElementById("user-profile-sidebar").src = url1;
            document.getElementById("side-username").innerHTML = mydata.username;
            document.getElementById("side-email").innerHTML = mydata.email;
        });
}

function getVideoList() {
    $.ajax({
            type: "GET",
            url: "../SearchServlet",
            ContentType: "application/json"
        })
        .then((data) => {
            console.log(data);
            let mydata = JSON.parse(data);
            console.log(mydata);
            delete mydata["status"];
            for (let params in mydata) {
                searchAnimation("videos-page.html?id=" + mydata[params].media_id, mydata[params].media_name);
            }
        });
}

function searchAction() {
    var key;
    var searchkey;
    var list = document.getElementsByClassName("items-list");
    $("#search-input").keydown(() => {
        key = $("#search-input").val().toUpperCase();
        for (var i = 0; i < list.length; i++) {
            if ((list[i].innerText.indexOf(key)) == -1) {
                list[i].style.display = "none";
            } else {
                list[i].style.display = "block";
            }
        }
    });
}

function updateUser() {
    let data;
    $("#save-changes").click((e) => {
        data = generateData("profile-form");
        data.id = localStorage.getItem("id");
        if (!validateJSON(data)) {
            //console.log("JSON STATUS: OK");
            xhrConnection("POST", data, "../UpdateUserData", (mydata) => { //mydata is what I got from my xhrConnection
                console.log("Success? = " + mydata.status);
                if (mydata.status == 200) {
                    showtoast("Data saved!","green","check");
                    console.log(mydata.url);
                    setInterval(() => location.href = mydata.url, 3000);

                } else {
                    showtoast("Oops, its taking too much... try later!","red","close");
                }
            });
        } else {
            showtoast("You have to fill all the fields.","red","close");
        }
    });
}