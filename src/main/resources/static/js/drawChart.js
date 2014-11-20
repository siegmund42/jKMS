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
							hoverable:true
						}
					}
		
		$.plot($("#placeholder"), chartData , options);
	
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

function stopGame(){
	$("#prototypInput").hide();
	
}

