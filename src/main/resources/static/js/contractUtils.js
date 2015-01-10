function copyForCorrect()	{
	
	document.getElementById("id1"). value = document.getElementById("a").value;
	document.getElementById("id2"). value = document.getElementById("b").value;
	document.getElementById("price"). value = document.getElementById("p").value;

	document.getElementById("contract").action = "contract.html?c";
	
	document.getElementById("submit").value = document.getElementById("correct").value;
	
}