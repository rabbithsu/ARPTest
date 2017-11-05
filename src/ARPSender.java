

import java.io.IOException;  
import java.net.InetAddress;  
import java.util.Date;  
  
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;  
import jpcap.packet.ARPPacket;  
import jpcap.packet.EthernetPacket;  


public class ARPSender extends Thread{
	public void run(){
		try{
			Thread.sleep(2000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		setF("arp");
		
		ARPPacket arp = new ARPPacket();
		arp.hardtype = ARPPacket.HARDTYPE_ETHER;
		arp.prototype = ARPPacket.PROTOTYPE_IP;
		arp.operation = ARPPacket.ARP_REQUEST;
		arp.hlen = 6;
		arp.plen = 4;
		// 源MAC地址
		arp.sender_hardaddr = device.mac_address;
		// 源IP地址
		arp.sender_protoaddr = device.addresses[0].address.getAddress();
		// 目地MAC地址:广播地址全为1(二进制)
		arp.target_hardaddr = broadcast;
		// 目地IP地址
		arp.target_protoaddr = targetIP.getAddress();
		// 构造以太网头部
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac = device.mac_address;
		ether.dst_mac = broadcast;
		// ARP数据包加上以网关头部
		arp.datalink = ether;
		// 向局域网广播ARP请求数据报
		int count = 5;
		for(int i = 0; i-1 < count; i++){
			sender.sendPacket(arp);
		}
	}
	
	//int index = Integer.parseInt(args[0]);
	InetAddress targetIP;
	NetworkInterface device;
	JpcapCaptor Scaptor;// = JpcapCaptor.openDevice(device, 65535, false, 20);
	JpcapSender sender;// = Scaptor.getJpcapSenderInstance();
	
	public void setF(String fil){
		try{
			Scaptor.setFilter(fil, true);}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
		// 进行广播数据报的MAC地址
	byte[] broadcast = new byte[] { (byte) 255, (byte) 255, (byte) 255,
	(byte) 255, (byte) 255, (byte) 255 };
	// 构造REQUEST 类型的ARP的数据包
	
	public ARPSender(NetworkInterface d, InetAddress n){

		//targetIP = InetAddress.getLocalHost();
		try{
			targetIP = n;
			device = d;
			Scaptor = JpcapCaptor.openDevice(device, 65535, false, 20);
			sender = Scaptor.getJpcapSenderInstance();// = Scaptor.getJpcapSenderInstance();
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	
}
