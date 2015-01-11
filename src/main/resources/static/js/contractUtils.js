function copyForCorrect()	{
	
	// Copy values
	document.getElementById("id1").value = document.getElementById("a").value;
	document.getElementById("id2").value = document.getElementById("b").value;
	document.getElementById("price").value = document.getElementById("p").value;

	// Update action for the form
	document.getElementById("contract").action = "contract.html?c";
	
	// Update value for submit button
	document.getElementById("submit").value = document.getElementById("correct").value;
	
	// Update Schriftzug :D
	document.getElementById("schriftzug").innerHTML = correctText;
	
	// Update value for correct Button
	document.getElementById("correct").onclick = function(){ abortCorrect(); } ;
	document.getElementById("correct").value = abort;
	
}

function abortCorrect()	{
	// Delete values
	document.getElementById("id1").value = "";
	document.getElementById("id2").value = "";
	document.getElementById("price").value = "";

	// Update action for the form
	document.getElementById("contract").action = "contract.html";
	
	// Update value for correct Button
	document.getElementById("correct").onclick = function(){ copyForCorrect(); } ;
	document.getElementById("correct").value = document.getElementById("submit").value;
	
	// Update value for submit button
	document.getElementById("submit").value = submit;
	
	// Update Schriftzug :D
	document.getElementById("schriftzug").innerHTML = transmittedText;
	
}