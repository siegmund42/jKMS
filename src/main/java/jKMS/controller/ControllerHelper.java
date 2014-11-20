package jKMS.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

public class ControllerHelper {

	private int[][] standardCustomerConfiguration;
	private int[][] standardSalesmanConfiguration;
	
	public ControllerHelper()	{
		standardCustomerConfiguration = new int[6][2];
		standardCustomerConfiguration[0][0]	= 20;
		standardCustomerConfiguration[0][1]	= 70;
		standardCustomerConfiguration[1][0]	= 16;
		standardCustomerConfiguration[1][1]	= 65;
		standardCustomerConfiguration[2][0]	= 16;
		standardCustomerConfiguration[2][1]	= 60;
		standardCustomerConfiguration[3][0]	= 16;
		standardCustomerConfiguration[3][1]	= 55;
		standardCustomerConfiguration[4][0]	= 16;
		standardCustomerConfiguration[4][1]	= 50;
		standardCustomerConfiguration[5][0]	= 16;
		standardCustomerConfiguration[5][1]	= 45;
		
		standardSalesmanConfiguration = new int[6][2];
		standardSalesmanConfiguration[0][0]	= 10;
		standardSalesmanConfiguration[0][1]	= 63;
		standardSalesmanConfiguration[1][0]	= 18;
		standardSalesmanConfiguration[1][1]	= 58;
		standardSalesmanConfiguration[2][0]	= 18;
		standardSalesmanConfiguration[2][1]	= 53;
		standardSalesmanConfiguration[3][0]	= 18;
		standardSalesmanConfiguration[3][1]	= 48;
		standardSalesmanConfiguration[4][0]	= 18;
		standardSalesmanConfiguration[4][1]	= 43;
		standardSalesmanConfiguration[5][0]	= 18;
		standardSalesmanConfiguration[5][1]	= 38;
	}

	public int[][] getStandardCustomerConfiguration() {
		return standardCustomerConfiguration;
	}

	public int[][] getStandardSalesmanConfiguration() {
		return standardSalesmanConfiguration;
	}
	
	public String getIP()	{
		Enumeration<NetworkInterface> ifaces = null;
		// Pick up all Network Interfaces
		try {
			ifaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		// Go through the Network Interfaces
		while (ifaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface)ifaces.nextElement();
			// Pick up all Addresses of the Network Interface
		    Enumeration<InetAddress> addrs = ni.getInetAddresses();
		    // Go through the Addresses
		    while (addrs.hasMoreElements()) {
		    	InetAddress ia = (InetAddress)addrs.nextElement();
		    	// Only display an Address, which is not localhost and has no ":" in it [Filter for MAC Adresses]
		    	if(!ia.getHostAddress().toString().equals("127.0.0.1") && ia.getHostAddress().toString().indexOf(":") == -1)	{
		    		return ia.getHostAddress().toString();
		        }
		    }
		}
		return null;
	}
	
	public int getPort(ServletRequest request)	{
		return request.getServerPort();
	}
	
}