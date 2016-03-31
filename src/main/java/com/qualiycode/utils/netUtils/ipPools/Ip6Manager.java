package com.qualiycode.utils.netUtils.ipPools;

import java.math.BigInteger;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.commons.net.util.SubnetUtils;


/**
 * This object holds and manage a list of IPv6 IP's
 * 
 * @author Eli Rozenfeld
 */
public class Ip6Manager extends IpManager {

	/**
	 * Pool start IP 
	 */
	public String startIP;
	
	/**
	 * Pool end IP 
	 */
	public String endIP;
	
	/**
	 * The pool subnet bits 
	 */
	public int subnetBits = 64;
	
	/**
	 * the current IP that was given to the user 
	 */
	private String currentIp;
	
	/**
	 * Holds the list of the IPs that where released from the pool 
	 */
	private Stack<String> releasedIps;
	
	/**
	 * Hold the percentage probability for using IPv6 extension  
	 */
	public int ipv6ExtensionProbability = 0;
	
	/**
	 * mark if this is the first IP we assign 
	 */
	private boolean isFirstIp = true;
	
	/**
	 * @return allocate new IP from the pool (if pool is empty, meaning that all IP's were allocated return null)
	 */
	@Override
	public String getNextIp() {
		return allocateNewIp();
	}
	
	/**
	 * This function releases IP from the pool
	 * @param ipToRelease - the IP to release from the pool
	 */
	@Override
	public void releaseIp(String ipToRelease){
		if(releasedIps == null){
			releasedIps = new Stack<>();
		}
		releasedIps.push(ipToRelease);
	}
	
	/**
	 * @return a newly allocated IP from the pool or NULL if the pool is empty
	 */
	protected String allocateNewIp(){
		if(isFirstIp){
			isFirstIp = false;
			return currentIp;
		}
		
		if(releasedIps != null && !releasedIps.empty()){
			return releasedIps.pop();
		}
		if(isPoolEmpty()){
			return null;
		}else{
			BigInteger longip = ipv6ToNumber(currentIp);
			longip = longip.add(new BigInteger("1"));
			currentIp = numberToIPv6(longip);
			return currentIp;
		}
	}
	
	/**
	 * @return the previous IP allocated from from the pool
	 */
	public String getPrevIp(){
		BigInteger longip = ipv6ToNumber(currentIp);
		longip = longip.subtract(new BigInteger("1"));
		String prevIp = numberToIPv6(longip);
		return prevIp;
	}
	
	/**
	 * @return the current IP allocated from the pool
	 */
	@Override
	public String getCurrentIp(){
		return currentIp;
	}
	
	/**
	 * @return true if pool is empty (all IPs were allocated)
	 */
	@Override
	public boolean isPoolEmpty(){
		BigInteger longCurrentIp = ipv6ToNumber(currentIp);
		BigInteger longEndIp = ipv6ToNumber(endIP);
		if(longCurrentIp.compareTo(longEndIp) < 0){
			return false;
		}else {
			return true;
		}
	}

	/**
	 * @return the pool start IP
	 */
	@Override
	public String getStartIP() {
		return startIP;
	}

	/**
	 * Sets the pool start IP
	 * @param startIP - the pool start IP
	 */
	@Override
	public void setStartIP(String startIP) {
		this.startIP = startIP;
		currentIp = startIP;
	}

	/**
	 * @return the pool end IP
	 */
	@Override
	public String getEndIP() {
		return endIP;
	}

	/**
	 * Sets the pool end IP
	 * @param startIP - the pool end IP
	 */
	@Override
	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}

	private BigInteger ipv6ToNumber(String addr) {
	    int startIndex=addr.indexOf("::");

	    if(startIndex!=-1){


	        String firstStr=addr.substring(0,startIndex);
	        String secondStr=addr.substring(startIndex+2, addr.length());


	        BigInteger first=ipv6ToNumber(firstStr);

	        int x=countChar(addr, ':');

	        first=first.shiftLeft(16*(7-x)).add(ipv6ToNumber(secondStr));

	        return first;
	    }


	    String[] strArr = addr.split(":");

	    BigInteger retValue = BigInteger.valueOf(0);
	    for (int i=0;i<strArr.length;i++) {
	        BigInteger bi=new BigInteger(strArr[i], 16);
	        retValue = retValue.shiftLeft(16).add(bi);
	    }
	    return retValue;
	}


	private String numberToIPv6(BigInteger ipNumber) {
	    String ipString ="";
	    BigInteger a=new BigInteger("FFFF", 16);

	        for (int i=0; i<8; i++) {
	            ipString=ipNumber.and(a).toString(16)+":"+ipString;

	            ipNumber = ipNumber.shiftRight(16);
	        }

	    return ipString.substring(0, ipString.length()-1);

	}

	private int countChar(String str, char reg){
	    char[] ch=str.toCharArray();
	    int count=0;
	    for(int i=0; i<ch.length; ++i){
	        if(ch[i]==reg){
	            if(ch[i+1]==reg){
	                ++i;
	                continue;
	            }
	            ++count;
	        }
	    }
	    return count;
	}

	/**
	 * @return the IP Pool subnet mask (for example if subnet bits = 24 this function will return 255.255.255.0)
	 * @throws Exception
	 */
	@Override
	public String getSubnetMask() throws Exception{
		//using bogus IP just for mask calculation
		SubnetUtils subnet = new SubnetUtils("1.1.1.1/" + subnetBits);
		
		return subnet.getInfo().getNetmask();
	}

	@Override
	public int getSubnetBits() {
		return 64;
	}

	@Override
	public void setSubnetBits(int subnetBits) {	}

	/**
	 * @return the percentage probability for using IPv6 extension
	 */
	public int getIpv6ExtensionProbability() {
		return ipv6ExtensionProbability;
	}

	/**
	 * @param ipv6ExtensionProbability - percentage probability for using IPv6 extension
	 */
	public void setIpv6ExtensionProbability(int ipv6ExtensionProbability) {
		this.ipv6ExtensionProbability = ipv6ExtensionProbability;
	}
	
	/**
	 * This function convert IPv6 to it's shorted format
	 * @param ipv6 - the IPv6
	 * @return the IPv6 in short format
	 * @throws Exception
	 */
	public static String shortIPv6Format(String ipv6) throws Exception{
		Ip6Manager ip6Manager = new Ip6Manager();
		BigInteger longip = ip6Manager.ipv6ToNumber(ipv6);
		return ip6Manager.numberToIPv6(longip);
	}
	
	/**
	 * This function convert IPv6 to it's extended format
	 * @param ipv6 - the IPv6
	 * @return the IPv6 in extended format
	 * @throws Exception
	 */
	public static String expandIPv6Format(String ipv6) throws Exception{
		
		String parts[] = ipv6.split(":");
		if(parts.length != 8) {
			throw new Exception("Malformed IPv6 String: " + ipv6);
		}
		
		int vals[] = new int[8]; 
		for(int i=0; i<8; ++i) {
			if(parts[i].isEmpty()) {
				vals[i] = 0;
			} else {
				try {
					vals[i] = Integer.parseInt(parts[i], 16);
				} catch (NumberFormatException e) {
					throw new Exception("Malformed IPv6 String: " + ipv6);
				}
			}
		}
		
		return String.format("%04X:%04X:%04X:%04X:%04X:%04X:%04X:%04X",
				vals[0], vals[1], vals[2], vals[3], vals[4], vals[5], vals[6], vals[7]);
	}
	
	/**
	 * This function checks if a specific IP is a valid IPv6 IP
	 * @param ip - the IP to validate
	 * @return true if the supplied IP is a valid IPv6 IP
	 * @throws Exception
	 */
	public static boolean isValidIp(String ip) throws Exception{
		Pattern ipv6pattern = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}");
		return ipv6pattern.matcher(ip).matches();
	}
}
