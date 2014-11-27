var alphabet = new Array ('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');

	function updateArrangement()	{
		updateAbsolutes();
		updateSum();
    }
	
	function updateSum()	{
		var customerTotalRelative = 0, salesmanTotalRelative = 0, customerTotalAbsolute = 0, salesmanTotalAbsolute = 0, i = 1;
		
    	for(var i = 1; i <= arrangement.groupQuantity.value; i++)	{
    		if(document.getElementById('c' + i + 'relativeQuantity') != null)	{
	    		customerTotalRelative += document.getElementById('c' + i + 'relativeQuantity').value*1;
	    		salesmanTotalRelative += document.getElementById('s' + i + 'relativeQuantity').value*1;
	    		customerTotalAbsolute += document.getElementById('c' + i + 'absoluteQuantity').innerHTML*1;
	    		salesmanTotalAbsolute += document.getElementById('s' + i + 'absoluteQuantity').innerHTML*1;
    		}
    	}
    	
    	document.getElementById('customerTotalRelative').innerHTML = customerTotalRelative + percent;
    	document.getElementById('salesmanTotalRelative').innerHTML = salesmanTotalRelative + percent;
    	document.getElementById('customerTotalAbsolute').innerHTML = customerTotalAbsolute;
    	document.getElementById('salesmanTotalAbsolute').innerHTML = salesmanTotalAbsolute;
    	
		if(customerTotalRelative > 100)	{
			document.getElementById('customerTotalRelative').style.color = "red";
    		alert(totalOV);
		}	else
			if(customerTotalRelative == 100)
    			document.getElementById('customerTotalRelative').style.color = "green";
			else
        		document.getElementById('customerTotalRelative').style.color = "black";
		
		if(salesmanTotalRelative > 100)	{
			document.getElementById('salesmanTotalRelative').style.color = "red";
    		alert(totalOV);
		}	else
			if(salesmanTotalRelative == 100)
    			document.getElementById('salesmanTotalRelative').style.color = "green";
			else
        		document.getElementById('salesmanTotalRelative').style.color = "black";
    	
	}
	
	function updateAbsolutes()	{
    	for(var i = 1; i <= arrangement.groupQuantity.value; i++)	{
    		if(document.getElementById('c' + i + 'relativeQuantity') != null)	{
	    		document.getElementById('c' + i + 'absoluteQuantity').innerHTML = (document.getElementById('c' + i + 'relativeQuantity').value/200)*numberOfPlayers;
	    		document.getElementById('s' + i + 'absoluteQuantity').innerHTML = (document.getElementById('s' + i + 'relativeQuantity').value/200)*numberOfPlayers;
    		}
    	}
	}

	function addRow()	{
		
		var rows = arrangement.groupQuantity.value;
		
		// add Customer Group ----------------------------------
		var row = document.createElement("div");
		row.setAttribute("class", "row");
		row.setAttribute("id", "row" + ((rows*1) + 1));
		
		// Add field for relative Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 'c' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('id', 'c' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('placeholder', percent);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for price
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 'c' + (rows*1 + 1) + 'price');
        box.setAttribute('placeholder', currency);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for absolute Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
        cell.setAttribute('id', 'c' + (rows*1 + 1) + 'absoluteQuantity');

		row.appendChild(cell);
		
		// add Row for Error displaying
		var errorRow = document.createElement("div");
		errorRow.setAttribute("class", "row");
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errorc' + (rows*1 + 1) + 'relativeQuantity');
		
		errorRow.appendChild(error);
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errorc' + (rows*1 + 1) + 'price');
		
		errorRow.appendChild(error);
		
		document.getElementById('customerTable').insertBefore(row, document.getElementById('customerTable').lastChild.previousSibling);
		document.getElementById('customerTable').insertBefore(errorRow, document.getElementById('customerTable').lastChild.previousSibling);
        
		// Verkäufer Gruppe hinzufügen ---------------------------------------
		var row = document.createElement("div");
		row.setAttribute("class", "row");
		row.setAttribute("id", "row" + (rows*1 + 1));
		
		// Add remove Link
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		var div = document.createElement('div');
		div.setAttribute('class', 'round');
		div.setAttribute('onclick', 'javascript:removeRow(' + (rows*1 + 1) + ');');
		div.innerHTML = "-";
		cell.appendChild(div);
		
		row.appendChild(cell);
		
		// Add field for relative Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 's' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('id', 's' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('placeholder', percent);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for price
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 's' + (rows*1 + 1) + 'price');
        box.setAttribute('placeholder', currency);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for absolute Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
        cell.setAttribute('id', 's' + (rows*1 + 1) + 'absoluteQuantity');

		row.appendChild(cell);
		
		// add Row for Error displaying
		var errorRow = document.createElement("div");
		errorRow.setAttribute("class", "row");
		
		var error = document.createElement("div");
		error.setAttribute("class", "cell");
		
		errorRow.appendChild(error);
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errors' + (rows*1 + 1) + 'relativeQuantity');
		
		errorRow.appendChild(error);
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errors' + (rows*1 + 1) + 'price');
		
		errorRow.appendChild(error);
		
		document.getElementById('salesmanTable').insertBefore(row, document.getElementById('salesmanTable').lastChild.previousSibling.previousSibling.previousSibling);
		document.getElementById('salesmanTable').insertBefore(errorRow, document.getElementById('salesmanTable').lastChild.previousSibling.previousSibling.previousSibling);
		
		// Increase number of groups
		arrangement.groupQuantity.value++;
		
		// Update values -> write zeros
		updateArrangement();
		
	}
	
	function removeRow(number)	{
		var row = document.getElementById("row" + number);
		row.parentNode.removeChild(row.nextSibling);
		row.parentNode.removeChild(row);

		var row = document.getElementById("row" + number);
		row.parentNode.removeChild(row.nextSibling);
		row.parentNode.removeChild(row);
		
		updateArrangement();
	}
	
	function createExcludeFields(number)	{
		
		for(var i = 0; i < number; i++)	{
			
			var row = document.createElement("div");
			row.setAttribute("class", "row");
			
			var errorRow = document.createElement("div");
			errorRow.setAttribute("class", "row");
			
			for(var r = 0; r <= 3 && i < number; r++)	{
				
				var label = document.createElement("label");
				label.setAttribute("for", i);
				label.innerHTML = alphabet[i].toUpperCase();
				row.appendChild(label);
				
				var input = document.createElement("input");
				input.setAttribute("type", "text");
				input.setAttribute("name", i);
				input.setAttribute("id", i);
				input.setAttribute("placeholder", alphabet[i].toUpperCase());
				row.appendChild(input);
				
				var cell = document.createElement("div");
				cell.setAttribute("class", "cell");
				errorRow.appendChild(cell);
				
				var error = document.createElement("div");
				error.setAttribute("class", "error");
				error.setAttribute("id", "error" + i);
				errorRow.appendChild(error);
				
				i++;
				
			}
			
			i--;
			
			document.getElementById("excludeDiv").appendChild(row);
			document.getElementById("excludeDiv").appendChild(errorRow);
			
		}
			
	}
	
	function div(a, b)	{
		return Math.floor(a/b);
	}
	
	function generalValidateField(field)	{
		
		var error = null;
		
		if(field.type == "number")	{
			
			if(isNaN(field.value))	{
				error = 1;
			}
			
			if(field.value <= 0)	{
				error = 2;
			}

			if(field.value == "")	{
				error = 0;
			}
			
			if((field.value % 1) != 0)	{
				error = 3;
			}
			
		}	else	{
			
			if(field.value == "")	{
				error = 0;
			}
			
		}

		if(error != null)	{
			writeError(error, field);
			return false;
		}
		
		return true;
		
	}
	
	function validateForm(form)	{
		
		var error = false;
		
		var inputs = Array.filter(document.getElementById(form).elements,	(	function (elm) {
		     													return (elm.type == "text" || elm.type == "password" || elm.type == "number");
		   													}
		   												));
		
		for(var i = 0; i < inputs.length && inputs[i] != null; i++)	{
			
			if(generalValidateField(inputs[i]))	{
				
				switch(form)	{
				case 'config':
					var players = document.getElementById('players');
					var assistants = document.getElementById('assistants');
					if(players.value % 2 != 0)	{
						writeError(4, players);
						error = true;
					}
					if(players.value > 8999)	{
						writeError(5, players);
						error = true;
					}
					if(assistants.value > 26)	{
						writeError(5, assistants);
						error = true;
					}
					break;
				case 'exclude':
					var error;
					var field = document.getElementById(i);
					if(isNaN(field.value))	{
						writeError(1, field);
						error = true;
					}
//					if(field.value < kms.getFirstID OR field.value > (kms.getFirstID + kms.getPlayerCount))	{
//						writeError(5, field);
//						error = true;
//					}
					break;
				}
				
			}	else	{
				error = true;
			}
			
			if(!error)
				removeError(inputs[i]);
			
		}
		
		if(error)	{
			return false;
		}	else
			return true;
		
	}
	
	function createErrorFields(element, numberOfColumns)	{
		
		var inputs = Array.filter(document.getElementById(element).elements,	(	function (elm) {
																						return (elm.type == "number");
																					}
																				));
		
		for(var i = 0; i <= inputs.length && inputs[i] != null; i++)	{
			
			if(document.getElementById('error' + inputs[i].name) == null)	{
				
				if(document.getElementById('errorRow' + div(i, numberOfColumns)) == null)	{
				
					var errorRow = document.createElement("div");
					errorRow.setAttribute("class", "row");
					errorRow.setAttribute("id", "errorRow" + div(i, numberOfColumns));
					
					inputs[i].parentNode.parentNode.parentNode.insertBefore(errorRow, inputs[i].parentNode.parentNode.nextSibling);
					
				}	else	{
					var errorRow = document.getElementById('errorRow' + div(i, numberOfColumns));
				}
				
//				if(boxdesc)	{
//					var cell = document.createElement("div");
//					cell.setAttribute("class", "cell");
//					row.appendChild(cell);
//				}
				
				var error = document.createElement("div");
				error.setAttribute("class", "error");
				error.setAttribute("id", "error" + inputs[i].name);
				
				errorRow.appendChild(error);
				
			}
			
			
		}
	}
	
	function writeError(error, element)	{
		
		var errorCodes = new Array();
		
		errorCodes[0] = "Feld leer";
		errorCodes[1] = "NaN";
		errorCodes[2] = "Zahl <= 0";
		errorCodes[3] = "Zahl gebrochen";
		errorCodes[4] = "Zahl ungerade";
		errorCodes[5] = "Zahl zu groß";
		
		document.getElementById('error' + element.name).innerHTML = errorCodes[error];
		element.style.border = "1px solid red";
	}
	
	function removeError(element)	{
		document.getElementById('error' + element.name).innerHTML = "";
		element.style.border = "1px solid #AAA";
	}