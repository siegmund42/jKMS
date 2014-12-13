package jKMS;


import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import au.com.bytecode.opencsv.CSVWriter;

public class Csv {
	
	public Csv(){
		
	}
	
	private String idb;
	private String ids;
	private String price;
	private String station;
	
	public void generateCSV(CSVWriter writer, Set<Card> cards,Set<Contract> contracts){
		
		Set<Card> playedCards = new LinkedHashSet<Card>();
		List<String[]> data = new ArrayList<String[]>();
		Properties propertie = LogicHelper.getProperetie();
		Integer idb =0;
		Integer ids =0;
		Integer price =0;
		Integer bvalue =0;
		Integer svalue =0;
		char    packb = ' ';
		char    packs = ' ';
		String station = "";
		Date time = new Date();
		Integer cid = 0;
		Integer cvalue = 0;
		char cpack = ' ';
		String ctyp = "";
		
		
		//TODO set Strings
		
		//Create table for Contracts
		
		data.add(new String[] {"Contracts"});
		data.add(new String[] {"id buyer","buyer value","Package Buyer","id seller","seller value","Package Seller", "price","Time","Station"});
		

		for(Contract iter : contracts){
			
			//get Data from Contract
			idb = iter.getBuyer().getId();
			ids = iter.getSeller().getId();
			bvalue = iter.getBuyer().getValue();
			svalue = iter.getSeller().getValue();
			packb = iter.getBuyer().getPackage();
			packs = iter.getSeller().getPackage();
			price = iter.getPrice();
			station = iter.getUri();
			time = iter.getTime();
			
		
			
			// add to CSV
			data.add(new String[] {idb.toString(),bvalue.toString(),String.valueOf(packb),ids.toString(),svalue.toString(),String.valueOf(packs),price.toString(),time.toString(),station});
			
			//add to playedbuyer and playedseller to get unplayed player later
			playedCards.add(iter.getBuyer());
			playedCards.add(iter.getSeller());
		}
		
		data.add(new String[] {});//Leerzeile ?
		

		
		//Create table for unplayed player
		data.add(new String[] {"unplayed cards"});
		
		data.add(new String[] {});//Leerzeile ?
		
		data.add(new String[] {"Card ID", "Typ","Package", "Value"});
		
		for(Card iter : cards){
			if(!playedCards.contains((Card) iter)){
				cid = iter.getId();
				cpack = iter.getPackage();
				if(iter instanceof BuyerCard) ctyp = "Buyercard";
				if(iter instanceof SellerCard) ctyp = "Sellercard";
				cvalue = iter.getValue();
				
				//add to CSV
				data.add(new String[] {cid.toString(),ctyp,String.valueOf(cpack),cvalue.toString()});
			}
		}
		
		writer.writeAll(data);
		
	}

}
