package net.opencomm.smackTester;
import java.net.UnknownHostException;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class smackTester {
	public static void main(String[] args) {
		try {
			// Create a connection to the jabber.org server on a specific port.
			ConnectionConfiguration config = new ConnectionConfiguration("jabber.org", 5222);
			config.setCompressionEnabled(true);
			config.setSASLAuthenticationEnabled(true);
			XMPPConnection conn2 = new XMPPConnection(config);
			conn2.connect();
			conn2.login(args[0], args[1]);
			Roster roster = conn2.getRoster();
			roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

			Collection<RosterEntry> entries = roster.getEntries();
			try {
				java.net.InetAddress i = java.net.InetAddress.getLocalHost();
			
			for (RosterEntry entry : entries) {
				ChatManager chatmanager = conn2.getChatManager();
				Chat newChat = chatmanager.createChat(entry.getUser(), new MessageListener() {
				    public void processMessage(Chat chat, Message message) {
				        System.out.println("Received message: " + message);
				    }
				});

				try {
				    newChat.sendMessage(i.getHostAddress());			}
				catch(XMPPException e) {
					e.printStackTrace();
				}}}
			catch(UnknownHostException e) {
				e.printStackTrace();
			}

			PacketFilter filter = new PacketTypeFilter(Message.class);
			PacketCollector myCollector = conn2.createPacketCollector(filter);
			PacketListener myListener = new PacketListener() {
				public void processPacket(Packet packet) {
					Message rec = (Message)packet;
					System.out.println(rec.getBody());
				}
			};
			conn2.addPacketListener(myListener, filter);
			while(true){try {
				Thread.sleep(100);}catch(Exception e) {e.printStackTrace();}
			}}
		catch(XMPPException e) {
			e.printStackTrace();
		}
	}
}
