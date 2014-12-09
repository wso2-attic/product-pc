$(document).ready(function() {

	var ids_pre = "";
	var ids_suc = "";
	var ids_gen = "";
	var ids_spe = "";
	var assets = {
		data: []
	}

var processName = $("#processName").text().trim();


	$.get( "/store/asts/process/apis/content", { name: processName, reqType:"get"} )
  .done(function( result ) {
    $("#content").append(result);
  });


	$("#R1").each(function() {
		ids_pre = $(this).find("#td_pre").text();
		$(this).find("#td_pre").empty();
	});
	$("#R2").each(function() {
		ids_suc = $(this).find("#td_suc").text();
		$(this).find("#td_suc").empty();
	});
	$("#R3").each(function() {
		ids_gen = $(this).find("#td_gen").text();
		$(this).find("#td_gen").empty();
	});
	$("#R4").each(function() {
		ids_spe = $(this).find("#td_spec").text();
		$(this).find("#td_spec").empty();
	});



	var tempPre = ids_pre.split(",");
	var tempSuc = ids_suc.split(",");
	var tempGen = ids_gen.split(",");
	var tempSpe = ids_spe.split(",");

		$.get("http://localhost:9763/store/apis/assets?type=process", function(response) {



		for (var i in response.data) {
			var item = response.data[i];

			assets.data.push({

				"id": item.id,
				"name": item.attributes.overview_name
			});



		}


		//loading the Pre 
		for (var i = 0; i < tempPre.length; i++) {
			for (var j in assets.data) {
				if (tempPre[i] === assets.data[j].id) {

					$("#R1").each(function() {

						$(this).find("#td_pre").append('<li><a href = https://localhost:9443/publisher/asts/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');

					});


				}

			}
		};

		//loading the Pre 
		for (var i = 0; i < tempSuc.length; i++) {
			for (var j in assets.data) {
				if (tempSuc[i] === assets.data[j].id) {

					$("#R2").each(function() {

						$(this).find("#td_suc").append('<li><a href = https://localhost:9443/publisher/asts/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');

					});


				}

			}
		};

		//loading the Pre 
		for (var i = 0; i < tempGen.length; i++) {
			for (var j in assets.data) {
				if (tempGen[i] === assets.data[j].id) {


					$("#R3").each(function() {

						$(this).find("#td_gen").append('<li><a href = https://localhost:9443/publisher/asts/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');

					});


				}

			}
		};

		//loading the Pre 
		for (var i = 0; i < tempSpe.length; i++) {
			for (var j in assets.data) {
				if (tempSpe[i] === assets.data[j].id) {

					$("#R4").each(function() {

						$(this).find("#td_spec").append('<li><a href = https://localhost:9443/publisher/asts/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');

					});


				}

			}
		};


	});



if (tempPre == "") {
		$("#R1").each(function() {

			$(this).find("#td_pre").append('None');

		});

	}

	if (tempSpe == "") {
		$("#R4").each(function() {

			$(this).find("#td_spec").append('None');

		});

	}

	if (tempGen == "") {
		$("#R3").each(function() {

			$(this).find("#td_gen").append('None');

		});

	}

	if (tempSuc == "") {
		$("#R2").each(function() {

			$(this).find("#td_suc").append('None');

		});

	}



});