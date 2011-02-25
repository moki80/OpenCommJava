package opencomm.rtp.bombaysapphire;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.sound.sampled.LineUnavailableException;

public class DemoRTP {
	/** int ptCnt = 0; // number of ports open
	
	// create users with specified IP addresses
	UserRTP risa = new UserRTP("risa", "128.84.98.247");
	UserRTP dellA = new UserRTP("dellA", "128.84.98.126");
	UserRTP dellB = new UserRTP("dellB", "128.84.98.34");
	UserRTP dellC = new UserRTP("dellC", "128.84.98.238");
	
	// all users in an array
	UserRTP[] allUsers = {risa, dellA, dellB, dellC}; */
	
	/** main method 
	 * @throws IOException */
	public static void main(String[] args) {
		int ptCnt = 0; // number of ports open

		// create users with specified IP addresses
		// UserRTP risa = new UserRTP("risa", "128.84.98.247");
		UserRTP dellA = new UserRTP("dellA", "128.84.98.126");
		UserRTP dellB = new UserRTP("dellB", "128.84.98.34");
		UserRTP dellC = new UserRTP("dellC", "128.84.98.238");
		
		// put all users in an array
		UserRTP[] allUsers = {dellA, dellB, dellC};
		
		// put each pair and the ports to be used between them
		AudioConnect AB = new AudioConnect(dellA, dellB, 5010);
		AudioConnect AC = new AudioConnect(dellA, dellC, 5011);
		AudioConnect BA = new AudioConnect(dellB, dellA, 5012);
		AudioConnect BC = new AudioConnect(dellB, dellC, 5013);
		AudioConnect CA = new AudioConnect(dellC, dellA, 5014);
		AudioConnect CB = new AudioConnect(dellC, dellB, 5015);
		
		// put all pair in an array
		AudioConnect[] allPairs = {AB, AC, BA, BC, CA, CB};
		
		String myIP = ""; // ip address of this computer
		UserRTP me = dellA;
		
		try {
	        InetAddress addr = InetAddress.getLocalHost();
	        myIP = addr.toString();
	        System.out.println("myIP: " + myIP);
		}
		catch (UnknownHostException e) {
			System.err.println("Unknown Host Exception");
		}
		
		
		if (myIP.contains(dellA.getIPAdd())) {
			me = dellA;
		}
		else if (myIP.contains(dellB.getIPAdd())) {
			me = dellB;
		}
		else if (myIP.contains(dellC.getIPAdd())) {
			me = dellC;
		}
		else {
			System.out.println("I'm going to assume dellA, but who am I???");
		}
		System.out.println("I'm " + me.getUsername());
		
		for (int i = 0; i < allPairs.length; i++) {
			if (allPairs[i].getSender().equals(me)) {
				AudioSender as = new AudioSender(allPairs[i].getPort());
				as.start();
				System.out.println("AudioSender created from " + allPairs[i].getSender().getUsername()
						+ " to " + allPairs[i].getReceiver().getUsername());
			}
		}
		
		/** try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		// for each pair, make a receiver
			for (int i = 0; i < allPairs.length; i++) {
				// check if this is a receiver
				if (allPairs[i].getReceiver().equals(me)) {
					AudioReceiver ar = new AudioReceiver(allPairs[i].getSender().getIPAdd(), 
							allPairs[i].getPort());
					ar.start();
					System.out.println("AudioReceiver created from " + allPairs[i].getReceiver().getUsername()
							+ " to " + allPairs[i].getSender().getUsername());
						
					}
				}

	} // end main method
	
	

} // end class DemoRTP
