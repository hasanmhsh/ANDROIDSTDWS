package hms.tvc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class NetworkHandler extends Handler {
	private static ArrayList<DatagramSocket> dss = new ArrayList<DatagramSocket>();
	private static DatagramSocket ds = null;
	private static DatagramPacket dp = null;
	private static byte buffer [] = null;
	private static DatagramPacket dp2 = null;
	private static byte buffer2 [] = null;
	private final static int espPort = 45433;
	private final static String espIP = "192.168.1.255";
	private final static int localPort = 13343;
	private static MainActivity conAct;
	private static byte b0=0;
	private static byte b1=0;
	private static byte b2=0;
	private static boolean is_static_instances_inestantiated = false;
	
	
	public NetworkHandler(Looper looper,MainActivity ma) {
	    super(looper);
		conAct = ma;
		if(!is_static_instances_inestantiated)
			prepare_instances();
	}
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		int req = msg.arg1;
		b0 = (byte)req;
		req >>=8;
		b1 = (byte)req;
		req >>=8;
		b2 = (byte)req;
		// Do some processing
		send_request_recieve_response();
	}
	

	
	private static void prepare_instances() {
		// TODO Auto-generated method stub
		is_static_instances_inestantiated = true;
		try{
			Enumeration<NetworkInterface> nis=NetworkInterface.getNetworkInterfaces();
			while(nis.hasMoreElements()){
				Enumeration<InetAddress> ia = nis.nextElement().getInetAddresses();
				while(ia.hasMoreElements()){
				InetAddress iainst = ia.nextElement();
				if(iainst.isLoopbackAddress()||!(iainst instanceof Inet4Address))
					continue;
				dss.add(new DatagramSocket(new InetSocketAddress(iainst,localPort)));
				}
			}
			buffer = new byte[11];
			buffer2 = new byte[40];
			InetAddress remoteAddr = InetAddress.getByName(espIP);
			dp = new DatagramPacket(buffer,11,remoteAddr,espPort);
			dp2 = new DatagramPacket(buffer2,40);
		}
		catch (final Exception ex){
			conAct.exceptions_tv_.post(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					conAct.exceptions_tv_.setText(conAct.exceptions_tv_.getText().toString()+"Exception:getNetworkInterfacesUDPSockets:"+ex.getMessage()+"\n");
				}
			});
		}
	}
	
	
	
	
	private static void reset_buffers(){
		for(int i = 0 ; i < buffer.length ; i++){
			buffer[i]=0;
		}
		for(int i = 0 ; i < buffer2.length ; i++){
			buffer2[i]=0;
		}
		
	}
	
	
	
	
	private static void send_request_recieve_response(){
		reset_buffers();
		for(DatagramSocket ds : dss){
			try{
			
				    	buffer[0]=(byte)0x34;
				    	buffer[1]=(byte)0xE4;
				    	buffer[2]=(byte)0xD4;
				    	buffer[3]=(byte)0x53;
				    	buffer[4]=b0;
				    	buffer[5]=b1;
				    	buffer[6]=b2;
						int chksm = 0;
						if(buffer[4]<0){
							chksm+=buffer[4]+256;
						}
						else{
							chksm+=buffer[4];
						}
						if(buffer[5]<0){
							chksm+=buffer[5]+256;
						}
						else{
							chksm+=buffer[5];
						}
						if(buffer[6]<0){
							chksm+=buffer[6]+256;
						}
						else{
							chksm+=buffer[6];
						}
						chksm = ~chksm;
				    	chksm++;
				    	buffer[7]=(byte)(chksm);
				    	buffer[8]=(byte)(chksm>>8);
				    	buffer[9]=(byte)(chksm>>16);
				    	buffer[10]=(byte)(chksm>>24);
				    	ds.setSoTimeout(300);
				    	ds.send(dp);
				    	ds.receive(dp2);
				    	if(dp2.getLength()==25){
				    		if(buffer2[0]==0x34){
				    			if(buffer2[1]==(byte)0xE4){
				    				if(buffer2[2]==(byte)0xD4){
				    					if(buffer2[3]==(byte)0x53){
				    						chksm=0;
				    						for(int i = 4 ; i < 21 ; i++){
				    							chksm += buffer2[i];
				    						}
				    						chksm = ~chksm;
				    						chksm++;
				    						if(buffer2[21] == (byte)(chksm)){
				    							if(buffer2[22] == (byte)(chksm>>8)){
				    								if(buffer2[23] == (byte)(chksm>>16)){
				    									if(buffer2[24] == (byte)(chksm>>24)){
				    										conAct.exceptions_tv_.post(new Runnable(){
				    											@Override
				    											public void run() {
				    												// TODO Auto-generated method stub
				    												conAct.exceptions_tv_.setText(conAct.exceptions_tv_.getText().toString()+"correct resp"+"\n");
				    											}
				    										});
				    										return;
				    									}
				    								}
				    							}
				    						}
				    					}
				    				}
				    			}
				    		}
				    	}
				    	conAct.exceptions_tv_.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								conAct.exceptions_tv_.setText("bad resp");
							}
						});
				    }
					catch (final Exception ex){
						conAct.exceptions_tv_.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//conAct.exceptions_tv_.setText(conAct.exceptions_tv_.getText().toString()+"Exception:send_reqi:"+ex.getMessage()+"\n");
							}
						});
					}
					
			    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
