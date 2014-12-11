// Updates Fields in Arrangement Form
function updateArrangement()	{
	// Update Absolute Numbers
	updateAbsolutes();
	// Update Sum of Relative/Absolute Numbers
	updateSum();
}

// Updates the Absolute Amount depending on Number of Players and relative Amount
function updateAbsolutes()	{
	// For every Field
	for(var i = 1; i <= document.getElementById('groupQuantity').value; i++)	{
		// Only if Field is not null
		if(document.getElementById('cRelativeQuantity[' + i + ']') != null)	{
			// Update Absolute Fields for customer and salesman
    		document.getElementById('cAbsoluteQuantity[' + i + ']').value = absoluteRound((document.getElementById('cRelativeQuantity[' + i + ']').value/200)*numberOfPlayers);
    		document.getElementById('sAbsoluteQuantity[' + i + ']').value = absoluteRound((document.getElementById('sRelativeQuantity[' + i + ']').value/200)*numberOfPlayers);
		}
	}
}

// Updates the sum of relative and absolute Amounts
function updateSum()	{
	var customerTotalRelative = 0, salesmanTotalRelative = 0, customerTotalAbsolute = 0, salesmanTotalAbsolute = 0, i = 1;
	// For every Field
	for(var i = 1; i <= document.getElementById('groupQuantity').value; i++)	{
		// Only if Field is not null
		if(document.getElementById('cRelativeQuantity[' + i + ']') != null)	{
			// Update Sums of Relative and absolute Amounts for Customer and Salesman
    		customerTotalRelative += document.getElementById('cRelativeQuantity[' + i + ']').value*1;
    		salesmanTotalRelative += document.getElementById('sRelativeQuantity[' + i + ']').value*1;
    		customerTotalAbsolute += document.getElementById('cAbsoluteQuantity[' + i + ']').value*1;
    		salesmanTotalAbsolute += document.getElementById('sAbsoluteQuantity[' + i + ']').value*1;
		}
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
	
	// add Row for Error displaying
	var errorRow = document.createElement("div");
	errorRow.setAttribute("class", "row");
	
	var error = document.createElement("div");
	error.setAttribute("class", "error");
	error.setAttribute("id", 'errorcRelativeQuantity[' + (rows*1 + 1) + ']');
	
	errorRow.appendChild(error);
	
	var error = document.createElement("div");
	error.setAttribute("class", "error");
	error.setAttribute("id", 'errorcPrice[' + (rows*1 + 1) + ']');
	
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
	errorRow.setAttribute("class", "row");
	
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
	arrangement.groupQuantity.value++;
	
	// Update values -> write zeros
	updateArrangement();
	
}

// Remove a row (group) with Number "number"
function removeRow(number)	{
	// Get the row Element
	var row = document.getElementById("row" + number);
	// Remove ErrorRow too
	row.parentNode.removeChild(row.nextSibling);
	row.parentNode.removeChild(row);

	// Get the row Element
	var row = document.getElementById("row" + number);
	// Remove ErrorRow too
	row.parentNode.removeChild(row.nextSibling);
	row.parentNode.removeChild(row);
	
	// Update Absolutes and sums
	updateArrangement();
}