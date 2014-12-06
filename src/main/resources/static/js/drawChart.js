function drawPlayChart(data){

		playData = JSON.parse(data);

		var options = {	xaxis:{
							minTickSize: 1,
							autoscaleMargin:0.02,
							tickDecimals: 0
						}
					}
		
		$.plot($("#placeholder"), [playData] , options);
	
}

function drawEvaluationChart(data){
		//Daten verarbeiten und darstellen
		dataArray = data.split(";");
		playData = JSON.parse(dataArray[0]);
		if(dataArray.length > 1){
			sellerData = JSON.parse(dataArray[1]);
			buyerData = JSON.parse(dataArray[2]);
			chartData = [{data:playData},{lines:{steps:true},data:sellerData},{lines:{steps:true},data:buyerData}];
		} else {
			chartData = "["+playData+"]";
		}

		var options = {	xaxis:{
							minTickSize: 1,
							autoscaleMargin:0.02,
							tickDecimals: 0
						},
						grid:{
							backgroundColor: "white",
							hoverable:true
						}
					}
		
		var plot = $.plot($("#placeholder"), chartData , options);
		
		//Bildexport bereitstellen
		var header = $("meta[name='_csrf_header']").attr("content");
		var token = $("meta[name='_csrf']").attr("content");
		var myCanvas = plot.getCanvas();
		var formData = new FormData();
		
		myCanvas.toBlob(
			function(image){
				formData.append("image",image,"image.png");
			},
			"image/png");
		alert();
		console.log(formData);
		$.ajax({
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
			},
			url: "pdfExport.html",
			type: "POST",
			data: formData,
			processData: false,
			contentType: false,
			mimeType: "multipart/form-data",
			success:function(response){console.log(response)},
			error: function(e){console.log(e);}			
		});
}

function insertValue(){
	
	
	$.ajax({
		type: "Get",
		url: "getData.html",
		success: function(response) {drawPlayChart(response);},
		error: function(e){alert('Error' + e);}
	});
	
}

function drawEvaluation(){
	$.ajax({
		type: "Get",
		url: "getEvaluation.html",
		success: function(response) {drawEvaluationChart(response);},
		error: function(e){alert('Error' + e);}
	});
	
}


