$(document).ready(() => {
    console.log("THE DOC IS READY: main-design.js");
    var elem = document.querySelector('.modal');
    $('.modal').modal();
    $('input#title').characterCounter();
    likesPaint();
    homeComments();
    profileDivs();
    editProfile();
    saveChanges();    
    console.log(localStorage.getItem("username"));
}); 


function likesPaint() {
    $("#fav-icon").click(() => {
        if ($("#fav-icon").css("color", "black")) {
            $("#fav-icon").css("color", "red");
        } else {
            $("#fav-icon").css("color", "black");
        }
    })
}

function homeComments() {
    $("#home-comment-icon").click(() => {
        $("#home-comment-sec").slideToggle("fast");
    });
}

function addComment(comment, username, date, userid, commentid) {
    let externaldiv = document.createElement("div"); //card horizontal
    let userimg = document.createElement("img"); //user comment image
    let internaldiv = document.createElement("div"); //card stacked
    let contentdiv = document.createElement("div"); //card content
    let usercomment = document.createElement("p");
    let usercommentinfo = document.createElement("p"); //card user info comment
    let deletebutton = document.createElement("a");
    let mycomment;
    let userinfo;

    externaldiv.className = "card horizontal";
    userimg.src = "../ProfileImage?id=" + userid;
    userimg.className = "circle";
    userimg.style.width = "100px";
    userimg.style.height = "100px";
    userimg.style.padding = "10px 10px";
    internaldiv.className = "card-stacked";
    contentdiv.className = "card-content";
    usercomment.className = "user-comment-div left-align";
    usercommentinfo.className = "right-align";
    usercommentinfo.id = "comment-info";
    deletebutton.className = "btn red deletecm-btn";
    deletebutton.href="../Delete?id="+commentid+"&tn="+"cm";

    //text
    mycomment = document.createTextNode(comment);
    userinfo = document.createTextNode(`${username} - Posted on: ${date}`);
    deletebutton.innerHTML = "<i class='material-icons'>close</i>";

    usercomment.appendChild(mycomment); //adjuntado comentario en el <p>
    usercommentinfo.appendChild(userinfo); //adjuntado a el <p> de info de comentario

    usercommentinfo.style.fontStyle = "italic";
    usercommentinfo.style.fontWeight = "bold";

    contentdiv.appendChild(usercomment); //adjuntado al contenido del div el comentario mycomment
    contentdiv.appendChild(usercommentinfo); //adjuntado al contenido del div del comentario under mycomment

    internaldiv.appendChild(contentdiv); //adjuntamos al card stacked (second principal div) el card-content

    if(localStorage.getItem("id")==null){
        console.log("eres visitante, no puedes ver los botones de eliminar");
    }
    if((localStorage.getItem("id")==localStorage.getItem("helperid"))){
        console.log(localStorage.getItem("helperid"));
        console.log("este es tu video. puedes eliminar comentarios o video");
        externaldiv.appendChild(deletebutton);        
    }else{
        console.log("no es tu video... no puedes eliminar nada!");
    }
    let isadmin = localStorage.getItem("isadmin");
    if(isadmin == 200){
        console.log("eres admin puedes elimianr lo que queras");
        externaldiv.appendChild(deletebutton);
    }
    
    externaldiv.appendChild(userimg);
    externaldiv.appendChild(internaldiv);

    document.getElementById("comments-div").appendChild(externaldiv);
}

function myVideos(video_name, video_id,) {
    let externalrowdiv = document.getElementById("externaldiv");
    let coldiv = document.createElement("div");
    let externalcard = document.createElement("div");
    let cardcontent = document.createElement("div");
    let cardtitle = document.createElement("span");
    let cardaction = document.createElement("div");
    let hrefa = document.createElement("a");
    let atext = document.createElement("p");
    /*VIDEO*/
    let myvideo = document.createElement("video");

    //props
    coldiv.className = "col s3";
    externalcard.className = "card blue-grey darken-1";
    cardcontent.className = "card-content white-text";
    cardtitle.className = "card-title";
    cardaction.className = "card-action";
    hrefa.href = "videos-page.html?id="+video_id;
    myvideo.src = "../Streaming?id=" + video_id;
    myvideo.type = "video/mp4";
    myvideo.style.width = "100%";

    /*text*/
    cardtitle = document.createTextNode(video_name);
    atext = document.createTextNode("GO TO THE VIDEO");
 
    //appendchilds
    hrefa.appendChild(atext);

    cardcontent.appendChild(cardtitle);
    cardaction.appendChild(hrefa);
    externalcard.appendChild(myvideo);
    externalcard.appendChild(cardcontent);
    externalcard.appendChild(cardaction);
    coldiv.appendChild(externalcard);

    externalrowdiv.appendChild(coldiv);
}

function popularUsers(name, username, bio, profile_picture) {
    //styles in css
    let externalrowdiv = document.getElementById("popular-users-div");
    let externalcoldiv = document.createElement("div");
    let card_div = document.createElement("div");
    let card_img_div = document.createElement("div");
    let card_img = document.createElement("img");
    let card_content = document.createElement("div");
    let card_title = document.createElement("h5");
    let card_username = document.createElement("p");
    let card_bio = document.createElement("p");

    //props
    externalrowdiv.className = "row";
    externalcoldiv.className = "col s4";
    card_div.className = "mycard";
    card_img_div.className = "mycard-img center-align";
    card_img.src = "#";
    card_content.className = "mycard-content";
    card_title.className = "mycard-title";
    card_username.className = "mycard-username";
    card_bio.className = "mycard-bio";

    //text info
    card_title = document.createTextNode(name);
    card_username = document.createTextNode(`@ ${username}`);
    card_bio = document.createAttribute(bio);

    //append childs

    //We insert in content the title, username and bio
    card_content.appendChild(card_title);
    card_content.appendChild(card_username);
    card_content.appendChild(card_bio);
    //we insert in content the image div
    card_img_div.appendChild(card_img);
    //we insert in principal card div, the img div and content div
    card_div.appendChild(card_img_div);
    card_div.appendChild(card_content);

    //we insert in col div the card div
    externalcoldiv.appendChild(card_div);

    //finally we insert in principal external row div, the internalcoldiv
    externalrowdiv.appendChild(externalcoldiv);
    /*we insert the column into pupular-users-div that its declare
        on html not here */

}

function recentVideos(videourl) {
    let externaldiv = document.createElement("div");
    let video = document.createElement("video");
    let source_video = document.createElement("source");

    externaldiv.className = "center-align carousel";
    externaldiv.id = "recent-videos";

    video.className = "carousel-item";
    video.id = "recentvideos";
    video.controls = true; //check this
    video.style.width = "60%";
    video.style.height = "auto";
    video.style.borderRadius = "10px";

    source_video.src = videourl;
    source_video.type = "video/mp4";

    video.appendChild(source_video);
    externaldiv.appendChild(video);

    document.getElementById("recent-videos-div").appendChild(externaldiv);


}

function profileDivs(){
    $("#my-videos-btn").click(() => {
        $("#profile-card").fadeOut(500);
        $("#my-videos-div").fadeIn(500);
        $("#my-stats-div").fadeOut(500);
    });
    $("#my-stats-btn").click(()=>{
        $("#profile-card").fadeOut(500);
        $("#my-videos-div").fadeOut(500);
        $("#my-stats-div").fadeIn(500);
    });
    $(".back-profile-btn").click(() =>{
        $("#my-stats-div").fadeOut(500);
        $("#my-videos-div").fadeOut(500);
        $("#profile-card").fadeIn(500);
    });
}

function editProfile(){
    $("#edit-profile").click(() => {
        $("#save-changes").fadeIn("slow");
        $("#bio").prop("disabled", false);
    });
}

function saveChanges(){
    $("#save-changes").click(() => {
        $("#bio").prop("disabled", true);
    })
}

function searchAnimation(videolink, videoname){
    let externaldiv = document.getElementById("search-results");
    let itemlist = document.createElement("a");
    let itemtext = document.createElement("p");
    //let itemspan = document.createElement("span");  badges

    /*props*/
    itemlist.href = videolink;
    itemlist.className = "collection-item items-list indigo-text";
    itemlist.target = "_blank";

    /*text*/
    itemtext = document.createTextNode(videoname.toUpperCase());

    /*appends childs*/
    itemlist.appendChild(itemtext);
    externaldiv.appendChild(itemlist);
}
