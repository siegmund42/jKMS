// Similar to java:intToPackage
var alphabet = new Array ('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
	
// returns the not fractional Part of division of a and b
function div(a, b)	{
	return Math.floor(a/b);
}

// returns the number rounded up - Avoids numbers with .0000000001
function absoluteRound(number)	{
	return Math.ceil(Math.floor(number*1000)/1000);
}

function checkFile(field) {
	// allowed extension
	var allowed_extension = 'txt';

	// extension of file to load
	var extension = field.value.split('.');
	extension = extension[extension.length - 1];

	if(allowed_extension == extension)	{
		field.form.submit();
		return true;
	}	else	{
		document.getElementById("fileError").innerHTML = falseContentType;
		return false;
	}
}