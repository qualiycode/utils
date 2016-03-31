package com.qualiycode.utils.netUtils.ipPools;

import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.commons.net.util.SubnetUtils;

import com.qualiycode.utils.strings.StringUtils;

/**
 * This object holds and manage a list of IP's
 * 
 * @author Eli Rozenfeld
 */
public class Ip4Manager extends IpManager {

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
	public int subnetBits = 24;
	
	/**
	 * the current IP that was given to the user 
	 */
	private String currentIp;
	
	/**
	 * Holds the list of the IPs that where released from the pool 
	 */
	private Stack<String> releasedIps;
	
	/**
	 * mark if this is the first IP we assign 
	 */
	private boolean isFirstIp = true;
	
	/**
	 * @return allocate new IP from the pool (if pool is empty, meaning that all IP's were allocated return null)
	 * Note: IP's ending with 0 or 255 are not allocated
	 */
	@Override
	public String getNextIp() {

		String newIp = allocateNewIp();
		if(newIp!=null){
			int lastOctet = Integer.valueOf(StringUtils.getSubstringUsingRegex(newIp, "\\d+.\\d+.\\d+.(\\d+)")); // \d+.\d+.\d+.(\d+)
			if( lastOctet == 255){
				allocateNewIp();
				newIp = allocateNewIp();
			}else{
				if( lastOctet == 0){
					newIp = allocateNewIp();
				}
			}
		}
		return newIp;
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
			long longip = ipToLong(currentIp);
			longip = longip +1;
			currentIp = longToIp(longip);
			return currentIp;
		}
	}
	
	/**
	 * @return the previous IP allocated from from the pool
	 */
	public String getPrevIp(){
		long longip = ipToLong(currentIp);
		longip = longip -1;
		String prevIp = longToIp(longip);
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
		long longCurrentIp = ipToLong(currentIp);
		long longEndIp = ipToLong(endIP);
		//TODO: add limitation according to subnet bits
		if(longCurrentIp <= longEndIp){
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



	/**
	 * Convert IP to Long
	 * @param ipAddress - the String representation of IP
	 * @return the Long representation of IP
	 */
	private long ipToLong(String ipAddress) {
	    long result = 0;
	    String[] atoms = ipAddress.split("\\.");

	    for (int i = 3; i >= 0; i--) {
	        result |= (Long.parseLong(atoms[3 - i]) << (i * 8));
	    }

	    return result & 0xFFFFFFFF;
	}

	
	/**
	 * Convert long to IP
	 * @param ip - the long representation of IP
	 * @return the String representation of IP
	 */
	private String longToIp(long ip) {
	    StringBuilder sb = new StringBuilder(15);

	    for (int i = 0; i < 4; i++) {
	        sb.insert(0, Long.toString(ip & 0xff));

	        if (i < 3) {
	            sb.insert(0, '.');
	        }

	        ip >>= 8;
	    }

	    return sb.toString();
	  }

	/**
	 * @return the pool subnet bits
	 */
	@Override
	public int getSubnetBits() {
		return subnetBits;
	}

	/**
	 * @param subnetBits - the pool subnet bits
	 */
	@Override
	public void setSubnetBits(int subnetBits) {
		this.subnetBits = subnetBits;
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

	/**
	 * This function checks if a specific IP is a valid IPv4 IP
	 * @param ip - the IP to validate
	 * @return true if the supplied IP is a valid IPv4 IP
	 * @throws Exception
	 */
	public static boolean isValidIp(String ip) throws Exception{
		Pattern ipv6pattern = Pattern.compile("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])");
		return ipv6pattern.matcher(ip).matches();
	}

}
