package jKMS.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

public class ControllerHelper {
	
	public static String getIP()	{
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
	
	public static int getPort(ServletRequest request)	{
		return request.getServerPort();
	}
	
}
