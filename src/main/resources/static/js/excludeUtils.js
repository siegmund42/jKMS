// Create "number" Fields for excluding cards
function createExcludeFields(number)	{
	
	// for every Field
	for(var i = 0; i < number; i++)	{
		
		// Create Row
		var row = document.createElement("div");
		row.setAttribute("class", "row");
		
		// Create Row
		var errorRow = document.createElement("div");
		errorRow.setAttribute("class", "row");
		
		// Create Row for Checkboxes
		var checkBoxRow = document.createElement("div");
		checkBoxRow.setAttribute("class", "row");
		
		// 4 Fields in every Row
		for(var r = 0; r <= 3 && i < number; r++)	{
			
			// Create Label for Field
			var label = document.createElement("label");
			label.setAttribute("for", i);
			label.innerHTML = alphabet[i].toUpperCase();
			row.appendChild(label);
			
			// Create Text field
			var input = document.createElement("input");
			input.setAttribute("type", "text");
			input.setAttribute("class", "numberText");
			input.setAttribute("name", "exclude[]");
			input.setAttribute("id", i);
			input.setAttribute("placeholder", alphabet[i].toUpperCase());
			row.appendChild(input);

			// Create Description Checkbox Div [under Label]
			// Create Label for Field
			var label = document.createElement("label");
			label.setAttribute("for", "check" + i);
			label.setAttribute("class", "small");
			// TODO i18n
			label.innerHTML = "Alle Karten ausgeteilt:";
			
			checkBoxRow.appendChild(label);
			
			// Create Checkbox-Div
			var checkDiv = document.createElement("div");
			checkDiv.setAttribute("class", "leftCell");
			
			// Create CheckBox
			var box = document.createElement("input");
			box.setAttribute("type", "checkbox");
			box.setAttribute("id", "check" + i);
			box.setAttribute("name", "check[]");
			box.setAttribute("value", i);
			box.setAttribute("onchange", "javascript:allExcluded(" + i + ")");
			
			checkDiv.appendChild(box);
			
			checkBoxRow.appendChild(checkDiv);
			
			// Create Empty Error Div [under Label]
			var cell = document.createElement("div");
			cell.setAttribute("class", "cell");
			errorRow.appendChild(cell);
			
			// Create Error Div
			var error = document.createElement("div");
			error.setAttribute("class", "error");
			error.setAttribute("id", "error" + i);
			errorRow.appendChild(error);
			
			// Increase number of Fields
			i++;
			
		}
		
		// Decrease Number of Fields [because one too much increased in last for-loop]
		i--;
		
		// Append Rows
		document.getElementById("excludeDiv").appendChild(row);
		document.getElementById("excludeDiv").appendChild(errorRow);
		document.getElementById("excludeDiv").appendChild(checkBoxRow);
		
	}
		
}

// Disables a Textfield depending on its Checkbox was checked
function allExcluded(id)	{
	// Checkbox was checked
//	if(document.getElementById("check" + id).checked)	{
//		// Set readonly and invisible
//		var element = document.getElementById(id);
//		element.readOnly = true;
//		element.style.opacity = "0";
//	}	else	{
//		var element = document.getElementById(id);
//		element.readOnly = false;
//		element.style.opacity = "1";
//	}
	document.getElementById(id).readOnly = document.getElementById("check" + id).checked
}