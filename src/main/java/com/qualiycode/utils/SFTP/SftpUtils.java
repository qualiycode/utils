package com.qualiycode.utils.SFTP;

/**
 * This class is a SFTP utility class that allows SFTP Operations
 * 
 * @author Eli Rozenfeld
 */
public class SftpUtils {

	public static int MAX_TIMEOUT_FOR_SFTP_COPY_IN_SECONDS = 900; //15 minutes 
	
	/**
	 * Copy File from remote machine to local machine
	 * @param user - remote machine user
	 * @param password - remote machine password
	 * @param host - remote machine IP / Hostname
	 * @param remoteFilePath - path to directory on remote machine
	 * @param remoteFileName - file name on remote machine
	 * @param localFilePath - path to put the file on local machine
	 * @return true if the operation is successful, false elsewhere 
	 * 
	 * Note: if local path does not exist it will create it.
	 * @throws Exception
	 */
	public static boolean copyFromRemoteToLocal(String user, String password, String host, String remoteFilePath, String remoteFileName, String localFilePath)	throws Exception {
		return copyBetweenRemoteAndLocal(user, password, host, remoteFilePath, remoteFileName, localFilePath, true);
	}

	/**
	 * Copy File from local machine to remote machine
	 * @param user - remote machine user
	 * @param password - remote machine password
	 * @param host - remote machine IP / Hostname
	 * @param remoteFilePath - path to directory on remote machine
	 * @param remoteFileName - file name on remote machine
	 * @param localFilePath - path to put the file on local machine
	 * @return true if the operation is successful, false elsewhere 
	 * 
	 * Note: if remote path does not exist it will NOT create it and exception will be thrown.
	 * @throws Exception
	 */
	public static boolean copyFromLocalToRemote(String user, String password, String host, String localFilePath, String localFileName, String remoteFilePath)	throws Exception {
		return copyBetweenRemoteAndLocal(user, password, host, localFilePath, localFileName, remoteFilePath, false);
	}

	/**
	 * Copy File between local machine and remote machine
	 * @param user - remote machine user
	 * @param password - remote machine password
	 * @param host - remote machine IP / Hostname
	 * @param sourceFilePath - the path to the source file (the one we copy)
	 * @param sourceFileName - the file name to copy
	 * @param destinationFilePath - the path to the destination folder (were we copy to)
	 * @param remoteToLocal - if true will copy from remote to local, else will copy from local to remote
	 * @return true if the operation is successful, false elsewhere 
	 * @throws Exception
	 */
	public static boolean copyBetweenRemoteAndLocal(String user, String password, String host, String sourceFilePath, String sourceFileName, String destinationFilePath, boolean remoteToLocal) throws Exception {

		SftpCopyTask sftpCopyTask = new SftpCopyTask(user, password, host, sourceFilePath, sourceFileName, destinationFilePath, remoteToLocal);
		Thread  sftpCopyTaskThread = new Thread(sftpCopyTask);
		sftpCopyTaskThread.start();
		
		long timeout = MAX_TIMEOUT_FOR_SFTP_COPY_IN_SECONDS * 1000;
		long startTime = System.currentTimeMillis();
		boolean done = false;
		
		while(System.currentTimeMillis() <= startTime + timeout && !done){
			Thread.sleep(1000);
			done = sftpCopyTask.isFinised();
		}
		
		if(!done){
			sftpCopyTask.killTask();
		}
		
		return done;
	}
		
}
