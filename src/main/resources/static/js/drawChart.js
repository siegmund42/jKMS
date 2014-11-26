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
		var link = document.getElementById("pdf_export");
		var myCanvas = plot.getCanvas();
		var image = myCanvas.toDataURL("image/jpeg");
		
		var timestamp = new Date();
		timestamp = timestamp.toLocaleDateString();
		
		var pdf = new jsPDF('l');
		//image = image.replace("image/png","image/jpeg");
		pdf.addImage(image,"JPEG",10,50,280,110); //margin-left, margin-top, width, height
		
		//link.setAttribute("href",image);
		link.setAttribute("href",pdf.output('datauristring'));
		link.setAttribute("download","chart_export_"+timestamp+".pdf");
}

function insertValue(){
	
	var lastValue = $('#lastValue').val();
	
	$.ajax({
		type: "Get",
		url: "getData.html",
		data: "lastValue=" + lastValue,
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


