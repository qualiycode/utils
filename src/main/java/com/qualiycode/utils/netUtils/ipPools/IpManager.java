package com.qualiycode.utils.netUtils.ipPools;

/**
 * This object holds and manage a list of IPv4 IP's
 * 
 * @author Eli Rozenfeld
 */
public abstract class IpManager{

	/**
	 * This function releases IP from the pool
	 * @param ipToRelease - the IP to release from the pool
	 */
	public abstract void releaseIp(String ipToRelease);
	
	public abstract String getNextIp();
	
	public abstract String getCurrentIp();
	
	public abstract boolean isPoolEmpty();

	public abstract String getStartIP();

	public abstract void setStartIP(String startIP);

	public abstract String getEndIP();

	public abstract void setEndIP(String endIP);

	public abstract int getSubnetBits();

	public abstract void setSubnetBits(int subnetBits);

	public abstract String getSubnetMask() throws Exception;


}
