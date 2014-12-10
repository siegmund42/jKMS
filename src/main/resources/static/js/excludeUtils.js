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
			input.setAttribute("name", "exclude[]");
			input.setAttribute("id", i);
			input.setAttribute("placeholder", alphabet[i].toUpperCase());
			row.appendChild(input);
			
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
		
	}
		
}