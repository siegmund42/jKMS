var alphabet = new Array ('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');

	function updateArrangement()	{
		updateAbsolutes();
		updateSum();
    }
	
	function updateSum()	{
		var customerTotalRelative = 0, salesmanTotalRelative = 0, customerTotalAbsolute = 0, salesmanTotalAbsolute = 0, i = 1;
		
    	for(var i = 1; i <= arrangement.groupQuantity.value; i++)	{
    		customerTotalRelative += document.getElementById('c' + i + 'relativeQuantity').value*1;
    		salesmanTotalRelative += document.getElementById('s' + i + 'relativeQuantity').value*1;
    		customerTotalAbsolute += document.getElementById('c' + i + 'absoluteQuantity').innerHTML*1;
    		salesmanTotalAbsolute += document.getElementById('s' + i + 'absoluteQuantity').innerHTML*1;
    	}
    	
    	if(customerTotalRelative > 100 || salesmanTotalRelative > 100)	{
    		alert(totalOV);
    	}
    	
    	document.getElementById('customerTotalRelative').innerHTML = customerTotalRelative + percent;
    	document.getElementById('salesmanTotalRelative').innerHTML = salesmanTotalRelative + percent;
    	document.getElementById('customerTotalAbsolute').innerHTML = customerTotalAbsolute;
    	document.getElementById('salesmanTotalAbsolute').innerHTML = salesmanTotalAbsolute;
	}
	
	function updateAbsolutes()	{
    	for(var i=1;i<=arrangement.groupQuantity.value;i++)	{
    		document.getElementById('c' + i + 'absoluteQuantity').innerHTML = (document.getElementById('c' + i + 'relativeQuantity').value/200)*numberOfPlayers;
    		document.getElementById('s' + i + 'absoluteQuantity').innerHTML = (document.getElementById('s' + i + 'relativeQuantity').value/200)*numberOfPlayers;
    	}
	}

	function addRow()	{
		
		var rows = arrangement.groupQuantity.value;
		
		// add Customer Group ----------------------------------
		var row = document.getElementById("customerTable").insertRow(rows*1 + 2);
		
		// Add field for relative Quantity
		var cell = row.insertCell(0);
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 'c' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('id', 'c' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('placeholder', percent);
		cell.appendChild(box);
		
		// Add field for price
		var cell = row.insertCell(1);
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 'c' + (rows*1 + 1) + 'price');
        box.setAttribute('placeholder', currency);
		cell.appendChild(box);
		
		// Add field for absolute Quantity
		var cell = row.insertCell(2);
        cell.setAttribute('id', 'c' + (rows*1 + 1) + 'absoluteQuantity');

		// Verkäufer Gruppe hinzufügen ---------------------------------------
		var row = document.getElementById("salesmanTable").insertRow(rows*1 + 2);
		
		// Add field for relative Quantity
		var cell = row.insertCell(0);
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 's' + (rows*1 + 1) + 'realtiveQuantity');
        box.setAttribute('id', 's' + (rows*1 + 1) + 'relativeQuantity');
        box.setAttribute('placeholder', percent);
		cell.appendChild(box);
		
		// Add field for price
		var cell = row.insertCell(1);
		var box = document.createElement('input');
        box.setAttribute('type', 'number');
        box.setAttribute('name', 's' + (rows*1 + 1) + 'price');
        box.setAttribute('placeholder', currency);
		cell.appendChild(box);
		
		// Add field for absolute Quantity
		var cell = row.insertCell(2);
        cell.setAttribute('id', 's' + (rows*1 + 1) + 'absoluteQuantity');

		// Increase number of groups
		arrangement.groupQuantity.value++;
		
		// Update values -> write zeros
		updateArrangement();
		
	}
	
	function createExcludeFields(number)	{
		
		for(var i = 0; i < number; i++)	{
			
			var row = document.createElement("div");
			row.setAttribute("id", "row");
			
			for(var r = 0; r <= 3 && i < number; r++)	{
				
				var label = document.createElement("label");
				label.setAttribute("for", i);
				label.innerHTML = alphabet[i].toUpperCase();
				
				var input = document.createElement("input");
				input.setAttribute("type", "text");
				input.setAttribute("name", i);
				input.setAttribute("id", i);
				input.setAttribute("placeholder", alphabet[i].toUpperCase());
				row.appendChild(label);
				row.appendChild(input);
				
				i++;
				
			}
			
			i--;
			
			document.getElementById("exclude").appendChild(row);
			
		}
			
	}
	
	
	function validateForm(form)	{
		
		return generalValidateForm(form);
		
		
		
	}
	
	function generalValidateForm(form)	{
		
		var errorCodes = new Array();
		
		errorCodes[0] = "Feld leer";
		errorCodes[1] = "NaN";
		errorCodes[2] = "Zahl <= 0";
		
		var errorString = "";
		var errors = new Array();
		errors['text'] = new Array();
		errors['number'] = new Array();
		
		var text = Array.filter(document.getElementById(form).elements,	(	function (elm) {
		     													return (elm.type == "text");
		   													}
		   												));
		
		var number = Array.filter(document.getElementById(form).elements,	(	function (elm) {
																	return (elm.type == "number");
																}
															));
		
		for(var i = 0; i < text.length && text[i] != null; i++)	{
			
			if(text[i].value == "")	{
				errors['text'][i] = 0;
			}
			
			if(errors['text'][i] != null)	{
				text[i].style.border = "1px solid red";
				errorString += errorCodes[errors['text'][i]];
			}	else	{
				text[i].style.border = "1px solid #AAA";
			}
			
		}
		
		for(var i = 0; i < number.length && number[i] != null; i++)	{
			
			if(document.getElementById('error' + i) == null)	{
				var errorDiv = document.createElement("div");
				errorDiv.setAttribute("id", "error" + i);
				errorDiv.setAttribute("class", "error");
				number[i].parentNode.appendChild(errorDiv);
			}	else	{
				var errorDiv = document.getElementById('error' + i);
			}
			
			if(isNaN(number[i].value))	{
				errors['number'][i] = 1;
			}
			
			if(number[i].value <= 0)	{
				errors['number'][i] = 2;
			}

			if(number[i].value == "")	{
				errors['number'][i] = 0;
			}
		
			if(errors['number'][i] != null)	{
				number[i].style.border = "1px solid red";
				errorDiv.innerHTML = errorCodes[errors['number'][i]];
				errorString += "d";
			}	else	{
				number[i].style.border = "1px solid #AAA";
				errorDiv.innerHTML = "";
			}
			
		}
		
		if(errorString != "")	{
			return false;
		}	else
			return true;
		
	}