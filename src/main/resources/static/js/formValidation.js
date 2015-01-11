// General Validation of Field "field"
function generalValidateField(field)	{
	
	var error = null;
	
	// Validate number fields
	if(field.type == "number" || field.className == "numberText")	{

		// Field must not be empty
		if(field.value == "")	{
			error = 0;
		}	else
		
		// Field must be a number
		if(isNaN(field.value))	{
			error = 1;
		}	else
			
		// Number must not be fractional 
		if((field.value % 1) != 0)	{
			error = 3;
		}	else
		
		// Number must be bigger than 0 
		if(field.value <= 0)	{
			error = 2;
		}
		
	}	else	{
		// Validate text/password fields
		
		// Field must not be empty
		if(field.value == "")	{
			error = 0;
		}
		
	}

	// If any Error occurred
	if(error != null)	{
		// Write Error
		writeError(error, field);
		return false;
	}
	
	// Everything is fine
	return true;
	
}

// Specific Validation of a form
function validateForm(form)	{
	
	var error = false;
	var globalError = false;
	
	// Get Array of input fields
	var coll = document.getElementById(form).elements;
	
	var inputs = [];
	
	for(var i = 0; i < coll.length; i++)	{
		// Relevant Fields are: number, text, password
		if((coll[i].type == "number" || coll[i].type == "text" || coll[i].type == "password") && coll[i].readOnly == false)
			inputs[inputs.length] = coll[i];
	}
	
	// Go through all input fields
	for(var i = 0; i < inputs.length && inputs[i] != null; i++)	{
		
		error = false;
		
		// Do a general Validation of the current field
		if(generalValidateField(inputs[i]))	{
			
			// Do specific Validation
			switch(form)	{
			case 'config':
				// Basic Configuration/Metadata Formular
				
				if(inputs[i] == document.getElementById('players'))	{
					// Number of players must be even
					if(inputs[i].value % 2 != 0)	{
						writeError(4, inputs[i]);
						error = true;
					}
					// Number of Players must be lower than (10000 - firstID)
					if(inputs[i].value > (10000 - firstID))	{
						writeError(5, inputs[i]);
						error = true;
					}
				}
				// Number of Assistants must be lower or equal 26 (Because of max 26 letters)
				if(inputs[i] == document.getElementById('assistants') && inputs[i].value > 26)	{
					writeError(6, inputs[i]);
					error = true;
				}
				break;
			case 'exclude':
				// Exclude Cards Formular
				var error;
				// Number must be between first ID and lastID
				if(inputs[i].value < firstID || inputs[i].value > (firstID + numberOfPlayers))	{
					writeError(7, inputs[i]);
					error = true;
				}
				break;
			case 'contract':
				// Contract Form
				// Number must be between first ID and lastID - ONLY IF id FIELD!
				if(inputs[i].name != "price" && (inputs[i].value < firstID || inputs[i].value > (firstID + numberOfPlayers)))	{
					writeError(7, inputs[i]);
					error = true;
				}
				break;
			case 'arrangement':
				if(inputs[i].name == "sRelativeQuantity[]" || inputs[i].name == "cRelativeQuantity[]")	{
					if(inputs[i].value > 100)	{
						writeError(8, inputs[i]);
						error = true;
					}
				}
				break;
			}
			
		}	else	{
			// General Validation failed
			error = true;
		}
		
		if(!error)	{
			// remove Error if none
			removeError(inputs[i]);
		}
		
		// Set the global Error
		globalError = globalError || error;
		
	}
	
	// Special Check for distribution Form
	if(form == "arrangement" && !globalError)	{
		// Check if total relative is hundred
		if(!isHundred())	{
			alert(totalOOB);
			globalError = true;
		}	else	{
			
			// relative = 100
			// Check if we needed to round
			if(document.getElementById('customerTotalAbsolute').innerHTML > numberOfPlayers/2 ||
					document.getElementById('salesmanTotalAbsolute').innerHTML > numberOfPlayers/2)	{
				
				if(document.getElementById('customerTotalAbsolute').innerHTML*1 + document.getElementById('salesmanTotalAbsolute').innerHTML*1 > (10000 - firstID))	{
					// Actual number of players exceede the Limit of 8999
					globalError = true;
					window.alert(roundedUp + (document.getElementById('customerTotalAbsolute').innerHTML*1 + document.getElementById('salesmanTotalAbsolute').innerHTML*1 - numberOfPlayers) + "\n\n" + totalOV);
				}
					
				// Ask User if it was OK to round
				globalError =  globalError || !window.confirm(roundedUp + (document.getElementById('customerTotalAbsolute').innerHTML*1 + document.getElementById('salesmanTotalAbsolute').innerHTML*1 - numberOfPlayers));
			}
			
			if(globalError == false && numberOfAssistants > document.getElementById('customerTotalAbsolute').innerHTML*1 + document.getElementById('salesmanTotalAbsolute').innerHTML*1)	{
				globalError = true;
				window.alert(moreAsThanPl);
			}
			
		}
	}
	return !globalError;
	
}

// Create Error fields if the distribution was loaded from logic
function createErrorFields(element, numberOfColumns)	{
	
	// Get inputs Array ----------------------------------
	var coll = document.getElementById(element).elements;
	
	var inputs = [];
	
	for(var i = 0; i < coll.length; i++)	{
		// Relevant inputs: number fields, that are not readonly
		// [Absolute fields are readonly for passing it with POST request, they dont need error field]
		if(coll[i].type == "number" && coll[i].readOnly == false)
			inputs[inputs.length] = coll[i];
	}
	
	// For every Error relevant field
	for(var i = 0; i <= inputs.length && inputs[i] != null; i++)	{
		
		// No error div for the current field
		if(document.getElementById('error' + inputs[i].name) == null)	{
			
			// No such row to contain error div for current field
			if(document.getElementById('error' + inputs[i].parentNode.parentNode.id) == null)	{
			
				// create Error Row
				var errorRow = document.createElement("div");
				errorRow.setAttribute("class", "errorRow");
				errorRow.setAttribute("id", 'error' + inputs[i].parentNode.parentNode.id);
				
				// Append Error Row
				inputs[i].parentNode.parentNode.parentNode.insertBefore(errorRow, inputs[i].parentNode.parentNode.nextElementSibling);
				
			}	else	{
				// Get Error Row
				var errorRow = document.getElementById('error' + inputs[i].parentNode.parentNode.id);
			}
			
			// Add empty div
			if(errorRow.id.indexOf('s') != -1 && errorRow.firstElementChild == null)	{
				var error = document.createElement("div");
				error.setAttribute("class", "error");
				errorRow.appendChild(error);
			}
			
			// Create Error Div
			var error = document.createElement("div");
			error.setAttribute("class", "error");
			error.setAttribute("id", "error" + inputs[i].id);
			
			// Append Error Div to Row
			errorRow.appendChild(error);
			
		}
		
	}
}

// Write Error "error" in field under Element "element"
function writeError(error, element)	{
	// errorCodes is defined in resources.html
	// Write Error Text in Field
	document.getElementById('error' + element.id).innerHTML = errorCodes[error];
	// Paint border of element red
	element.style.border = "1px solid red";
}

// Remove Errors in field under Element "element"
function removeError(element)	{
	// Clear Text
	document.getElementById('error' + element.id).innerHTML = "";
	// Paint Border white
	element.style.border = "";
}