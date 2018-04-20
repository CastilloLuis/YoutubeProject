function deletebutton(id) {
    let isadmin = localStorage.getItem("isadmin");
    if (localStorage.getItem("id") == null) {
        $("#deletevideo-btn").css("display", "none");
    }    
    if (isadmin==200) {
        console.log("es true")
        console.log(isadmin)
        $("#deletevideo-btn").css("display", "block");
    } else {
        $.ajax({
                type: "GET",
                data: {
                    "id": id
                },
                url: "../GetVideoStats"
            }) 
            .then((data) => {
                let mydata = JSON.parse(data);
                if (localStorage.getItem("id") == mydata.userid) {
                    console.log("ES EL MISMO USUARIO QUESUBIO")
                    $("#deletevideo-btn").css("display", "block");
                }
            });
    }
}