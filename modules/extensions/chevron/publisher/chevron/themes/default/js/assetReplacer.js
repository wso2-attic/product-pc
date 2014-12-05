$(document).ready(function() {

    var assets = {
        data: []
    };


    var ids_mod = "";
    var assets = {
        data: []
    }



    $("#R2").each(function() {
        ids_mod = $(this).find("#td_mod").text();
        $(this).find("#td_mod").empty();
    });



    var tempMod = ids_mod.split(",");

    $.get("https://localhost:9443/publisher/apis/assets?type=process", function(response) {



        for (var i in response.data) {
            var item = response.data[i];

            assets.data.push({

                "id": item.id,
                "name": item.attributes.overview_name
            });



        }



        for (var i = 0; i < tempMod.length; i++) {

            for (var j in assets.data) {

                if (tempMod[i] === assets.data[j].id) {

                    $("#R2").each(function() {

                        $(this).find("#td_mod").append('<li><a href = https://localhost:9443/publisher/asts/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');

                    });


                }

            }
        };
    });


    if (tempMod == "") {
        $("#R2").each(function() {

            $(this).find("#td_mod").append('None');

        });

    }


});