// Updates Fields in Arrangement Form
function updateArrangement()	{
	// Update Absolute Numbers
	//updateAbsolutes();
	// Update Sum of Relative/Absolute Numbers
	updateSum();
}

// Updates the Absolute Amount depending on Number of Players and relative Amount
//function updateAbsolutes()	{
//	// For every Field
//	for(var i = 1; i <= document.getElementById('cGroupQuantity').value; i++)	{
//		// Only if Field is not null
//		if(document.getElementById('cRelativeQuantity[' + i + ']') != null)	{
//			// Update Absolute Fields for customer and salesman
//    		document.getElementById('cAbsoluteQuantity[' + i + ']').value = absoluteRound((document.getElementById('cRelativeQuantity[' + i + ']').value/200)*numberOfPlayers);
//		}
//	}
//	// For every Field
//	for(var i = 1; i <= document.getElementById('sGroupQuantity').value; i++)	{
//		// Only if Field is not null
//		if(document.getElementById('sRelativeQuantity[' + i + ']') != null)	{
//			// Update Absolute Fields for customer and salesman
//    		document.getElementById('sAbsoluteQuantity[' + i + ']').value = absoluteRound((document.getElementById('sRelativeQuantity[' + i + ']').value/200)*numberOfPlayers);
//		}
//	}
//}

// Updates the sum of relative and absolute Amounts
function updateSum()	{

	var customerTotalRelative = 0, salesmanTotalRelative = 0, customerTotalAbsolute = 0, salesmanTotalAbsolute = 0;
	
	// Get Array of input fields
	var coll = arrangement.elements;
	
	var inputs = new Array();
	inputs[0] = new Array();
	inputs[1] = new Array();
	
	for(var i = 0; i < coll.length; i++)	{
		// Relevant Fields are: number, text, password
		if(coll[i].type == "number" && coll[i].id.indexOf("Absolute") == -1)	{
			if(coll[i].id.indexOf("cRelative") != -1)
				inputs[0][inputs[0].length] = coll[i];
			if(coll[i].id.indexOf("sRelative") != -1)
				inputs[1][inputs[1].length] = coll[i];
		}
	}
	
	// For every Field
	for(var i = 0; i < inputs[0].length; i++)	{
		// Update Sums of Relative and absolute Amounts for Customer and Salesman
    	customerTotalRelative += inputs[0][i].value*1;
    	// Update Absolute number
//    	if(isStandard)
//    		inputs[0][i].parentNode.parentNode.lastChild.previousSibling.firstChild.value = absoluteRound((inputs[0][i].value/200)*numberOfPlayers);
//    	else
    	inputs[0][i].parentNode.nextElementSibling.nextElementSibling.firstElementChild.value = absoluteRound((inputs[0][i].value/200)*numberOfPlayers);
    	customerTotalAbsolute += inputs[0][i].parentNode.nextElementSibling.nextElementSibling.firstElementChild.value*1;
	}
	// For every Field
	for(var i = 0; i < inputs[1].length; i++)	{
		// Update Sums of Relative and absolute Amounts for Customer and Salesman
    	salesmanTotalRelative += inputs[1][i].value*1;
    	// Update Absolute number
    	inputs[1][i].parentNode.nextElementSibling.nextElementSibling.firstElementChild.value = absoluteRound((inputs[1][i].value/200)*numberOfPlayers);
    	salesmanTotalAbsolute += inputs[1][i].parentNode.nextElementSibling.nextElementSibling.firstElementChild.value*1;
	}
	
	// Set the values
	document.getElementById('customerTotalRelative').innerHTML = customerTotalRelative + percent;
	document.getElementById('salesmanTotalRelative').innerHTML = salesmanTotalRelative + percent;
	document.getElementById('customerTotalAbsolute').innerHTML = customerTotalAbsolute;
	document.getElementById('salesmanTotalAbsolute').innerHTML = salesmanTotalAbsolute;
	
	// Update Color of Sum of Relative Values
	isHundred();
		
}

// Updates the color of the fields for the Sum of relative Amounts
function isHundred()	{
	
	var error = false;
	var ctr = document.getElementById('customerTotalRelative');
	var str = document.getElementById('salesmanTotalRelative');
	
	// Customer ---------------------------------------------------------
	// if Sum is 100% [remove "%" Sign from string]
	if(ctr.innerHTML.substring(0, (ctr.innerHTML.length - 1)) == 100)	{
		ctr.style.color = "green";
	}	else	{
		ctr.style.color = "red";
		error = true;
	}

	// Salesman ---------------------------------------------------------
	// if Sum is 100% [remove "%" Sign from string]
	if(str.innerHTML.substring(0, (str.innerHTML.length - 1)) == 100)	{
		str.style.color = "green";
	}	else	{
		str.style.color = "red";
		error = true;
	}
	
	if(error)	{
		return false;
	}	else	{
		return true;
	}
	
}

// Add a new row (group)
function addRow(table)	{
	
	if(table == 'c')	{
		var rows = cGroupCount;
		
		// add Customer Group ----------------------------------
		var row = document.createElement("div");
		row.setAttribute("class", "row");
		row.setAttribute("id", "cRow" + (rows*1));
		
		// Add field for relative Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
	    box.setAttribute('type', 'number');
	    box.setAttribute('name', 'cRelativeQuantity[]');
	    box.setAttribute('id', 'cRelativeQuantity[' + (rows*1 + 1) + ']');
	    box.setAttribute('placeholder', percent);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for price
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
	    box.setAttribute('type', 'number');
	    box.setAttribute('name', 'cPrice[]');
	    box.setAttribute('id', 'cPrice[' + (rows*1 + 1) + ']');
	    box.setAttribute('placeholder', currency);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for absolute Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
	
		var input = document.createElement('input');
		input.setAttribute('type', 'number');
		input.setAttribute('readonly', 'readonly');
		input.setAttribute('name', 'cAbsoluteQuantity[]');
		input.setAttribute('id', 'cAbsoluteQuantity[' + (rows*1 + 1) + ']');
		
		cell.appendChild(input);
	
		row.appendChild(cell);
		
		// Add remove Link
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		var div = document.createElement('div');
		div.setAttribute('class', 'round');
		div.setAttribute('onclick', 'javascript:removeRow(' + (rows*1) + ', \'c\');');
		div.innerHTML = removeRowButton;
		
		cell.appendChild(div);
		
		row.appendChild(cell);
		
		// Adding 1 + first group - display the first remove-link
		if(arrangement.cGroupQuantity.value == "1")	{
			var table = document.getElementById("customerTable");
			var firstLine = table.firstChild;
			var firstGroup = firstLine.nextSibling.nextSibling.nextSibling;
			firstGroup.lastChild.style.visibility = "visible";
		}
		
		// add Row for Error displaying
		var errorRow = document.createElement("div");
		errorRow.setAttribute("id", "errorcRow" + (rows*1));
		errorRow.setAttribute("class", "errorRow");
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errorcRelativeQuantity[' + (rows*1 + 1) + ']');
		
		errorRow.appendChild(error);
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errorcPrice[' + (rows*1 + 1) + ']');
		
		errorRow.appendChild(error);
		
		document.getElementById('customerTable').insertBefore(row, document.getElementById('customerTable').lastChild.previousSibling.previousSibling.previousSibling);
		document.getElementById('customerTable').insertBefore(errorRow, document.getElementById('customerTable').lastChild.previousSibling.previousSibling.previousSibling);

		// Increase number of groups
		arrangement.cGroupQuantity.value++;
		// Increase name counter
		cGroupCount++;
	   
	}
	if(table == 's')	{
		
		var rows = sGroupCount;
		
		// Verkäufer Gruppe hinzufügen ---------------------------------------
		var row = document.createElement("div");
		row.setAttribute("class", "row");
		row.setAttribute("id", "sRow" + (rows*1));
		
		// Add remove Link
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		var div = document.createElement('div');
		div.setAttribute('class', 'round');
		div.setAttribute('onclick', 'javascript:removeRow(' + (rows*1) + ', \'s\');');
		div.innerHTML = removeRowButton;
		
		cell.appendChild(div);
		
		row.appendChild(cell);
		
		// Adding 1 + first group - display the first remove-link
		if(arrangement.sGroupQuantity.value == "1")	{
			var table = document.getElementById("salesmanTable");
			var firstLine = table.firstChild;
			var firstGroup = firstLine.nextSibling.nextSibling.nextSibling;
			firstGroup.firstChild.style.visibility = "visible";
		}
		// Add field for relative Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
	    box.setAttribute('type', 'number');
	    box.setAttribute('name', 'sRelativeQuantity[]');
	    box.setAttribute('id', 'sRelativeQuantity[' + (rows*1 + 1) + ']');
	    box.setAttribute('placeholder', percent);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for price
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
		
		var box = document.createElement('input');
	    box.setAttribute('type', 'number');
	    box.setAttribute('name', 'sPrice[]');
	    box.setAttribute('id', 'sPrice[' + (rows*1 + 1) + ']');
	    box.setAttribute('placeholder', currency);
		cell.appendChild(box);
		
		row.appendChild(cell);
		
		// Add field for absolute Quantity
		var cell = document.createElement('div');
		cell.setAttribute('class', 'cell');
	
		var input = document.createElement('input');
		input.setAttribute('type', 'number');
		input.setAttribute('readonly', 'readonly');
	    input.setAttribute('name', 'sAbsoluteQuantity[]');
	    input.setAttribute('id', 'sAbsoluteQuantity[' + (rows*1 + 1) + ']');
		
		cell.appendChild(input);
	
		row.appendChild(cell);
		
		// add Row for Error displaying
		var errorRow = document.createElement("div");
		errorRow.setAttribute("id", "errorsRow" + (rows*1));
		errorRow.setAttribute("class", "errorRow");
		
		var error = document.createElement("div");
		error.setAttribute("class", "cell");
		
		errorRow.appendChild(error);
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errorsRelativeQuantity[' + (rows*1 + 1) + ']');
		
		errorRow.appendChild(error);
		
		var error = document.createElement("div");
		error.setAttribute("class", "error");
		error.setAttribute("id", 'errorsPrice[' + (rows*1 + 1) + ']');
		
		errorRow.appendChild(error);
		
		document.getElementById('salesmanTable').insertBefore(row, document.getElementById('salesmanTable').lastChild.previousSibling.previousSibling.previousSibling);
		document.getElementById('salesmanTable').insertBefore(errorRow, document.getElementById('salesmanTable').lastChild.previousSibling.previousSibling.previousSibling);
		
		// Increase number of groups
		arrangement.sGroupQuantity.value++;
		// Increase name counter
		sGroupCount++;
		
	}
	
	// Update values -> write zeros
	updateArrangement();
	
}

// Remove a row (group) with Number "number"
function removeRow(number, type)	{
	
	// Get the row Element
	var row = document.getElementById(type + "Row" + number);
	// Remove ErrorRow too
	row.parentNode.removeChild(row.nextElementSibling);
	row.parentNode.removeChild(row);
	
	// Remove remove-link if last row
	if(document.getElementById(type + "GroupQuantity").value == "2")	{
		if(type == 'c')
			var table = document.getElementById("customerTable");
		if(type == 's')
			var table = document.getElementById("salesmanTable");
		
		var firstGroup = table.firstChild.nextSibling.nextSibling.nextSibling;
		
		if(type == 'c')
			firstGroup.lastChild.style.visibility = "hidden";
		if(type == 's')
			firstGroup.firstChild.style.visibility = "hidden";
	}
	
	document.getElementById(type + "GroupQuantity").value--;
	
	// Update Absolutes and sums
	updateArrangement();
}