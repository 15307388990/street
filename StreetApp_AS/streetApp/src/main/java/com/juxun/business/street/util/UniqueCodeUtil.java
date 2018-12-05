package com.juxun.business.street.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class UniqueCodeUtil {
	public static String getImei(Context context)
	{
		TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
		String Imei = TelephonyMgr.getDeviceId(); 
		return Imei;
	}
	
	public static String getDeviceId(Context context)
	{
		String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
		return deviceId;
	}
	
	public static String getWlanMac(Context context)
	{
		WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
		String wlanMac = wm.getConnectionInfo().getMacAddress();
		return wlanMac;
	}
	
	public static String getBtMac(Context context)
	{
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter      
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
		String bTMAC = m_BluetoothAdapter.getAddress();
		return bTMAC;
	}
	
	public static String getUid(String Imei,String deviceId,String wlanMac,String bTMAC){
		String m_szLongID = Imei + deviceId 
			    + wlanMac+ bTMAC;      
			// compute md5     
			 MessageDigest m = null;   
			try {
			 m = MessageDigest.getInstance("MD5");
			 } catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();   
			}    
			m.update(m_szLongID.getBytes(),0,m_szLongID.length());   
			// get md5 bytes   
			byte p_md5Data[] = m.digest();   
			// create a hex string   
			String m_szUniqueID = new String();   
			for (int i=0;i<p_md5Data.length;i++) {   
			     int b =  (0xFF & p_md5Data[i]);    
			// if it is a single digit, make sure it have 0 in front (proper padding)    
			    if (b <= 0xF) 
			        m_szUniqueID+="0";    
			// add number to string    
			    m_szUniqueID+=Integer.toHexString(b); 
			   }   // hex string to uppercase   
			m_szUniqueID = m_szUniqueID.toUpperCase();
			return m_szUniqueID;
	}



	
}
