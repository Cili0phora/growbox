    function eventHook() {
        console.log('halo')
        var checkBox = document.getElementById("check");
        console.log(checkBox)
        var text = document.getElementById("datetime");
        console.log(text)
        if (checkBox.checked == true){
            text.style.display = "block";
        } else {
            text.style.display = "none";
        }
    }