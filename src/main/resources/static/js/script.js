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