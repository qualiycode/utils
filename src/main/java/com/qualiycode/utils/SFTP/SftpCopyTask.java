package com.qualiycode.utils.SFTP;

import java.io.File;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * This is a thread task that performs the SFTP action
 * It is made as a workaround for the SftpChannel getting stack on copy.
 * 
 * @author Eli Rozenfeld
 *
 */
public class SftpCopyTask implements Runnable {
	
	protected final static Logger log = LoggerFactory.getLogger(SftpCopyTask.class);

	String user; 
	String password; 
	String host; 
	String sourceFilePath; 
	String sourceFileName; 
	String destinationFilePath; 
	boolean remoteToLocal = true;
	boolean finished = false;
	Session sftpSession = null;
	ChannelSftp sftpChannel = null;
	boolean disconnectAtEnd = true;
	
	public SftpCopyTask(String user, String password, String host, String sourceFilePath, String sourceFileName, String destinationFilePath, boolean remoteToLocal) {
		this.user = user;
		this.password = password;
		this.host = host;
		this.sourceFilePath = sourceFilePath;
		this.sourceFileName = sourceFileName;
		this.destinationFilePath = destinationFilePath;
		this.remoteToLocal = remoteToLocal;
		initChannelSftp();
	}
	
	public SftpCopyTask(ChannelSftp channelSftp, String sourceFilePath, String sourceFileName, String destinationFilePath){
		this.sftpChannel = channelSftp;
		disconnectAtEnd = false;
		this.sourceFilePath = sourceFilePath;
		this.sourceFileName = sourceFileName;
		this.destinationFilePath = destinationFilePath;
	}
	
	protected void initChannelSftp(){
		try {
			JSch jsch = new JSch();
			sftpSession = jsch.getSession(user, host, 22);
			// config 
			Hashtable<String, String> config = new Hashtable<String, String>();
			config.put("StrictHostKeyChecking", "no");
			sftpSession.setConfig(config);
			sftpSession.setPassword(password);
			//connect
			sftpSession.connect();
			sftpChannel = (ChannelSftp) sftpSession.openChannel("sftp");
			sftpChannel.connect();
		} catch (Exception e) {
			sftpChannel = null;
		}
		
	}
	
	@Override
	public void run() {
		try {
			// create the directories if needed
			if(remoteToLocal){
				File localFile = new File(destinationFilePath);
				if(!localFile.exists()){
					localFile.mkdirs();
				}
			}else{
				//TODO need to verify and create remote directory if needed
			}
			
			if (sftpChannel.isConnected()) {
				try {
					if(remoteToLocal){
						String homeDir = sftpChannel.getHome();
						sftpChannel.cd(sourceFilePath);
						sftpChannel.get(sourceFileName, destinationFilePath);
						sftpChannel.cd(homeDir);
					}else{
						sftpChannel.put(sourceFilePath + File.separator + sourceFileName, destinationFilePath);
					}
				} catch (Exception e) {
					log.warn("Error occur while trying to fetch file: " + sourceFileName, e);
				} finally{
					if(disconnectAtEnd){
						sftpSession.disconnect();
					}
				}
			}
		} catch (Exception e) {
			log.warn("Copy of " + sourceFileName + " failed", e);
		} finally {
			finished = true;
		}
	}

	/**
	 * @return true if the task is finished
	 */
	public boolean isFinised(){
		return finished;
	}
	
	/**
	 * disconnect the SSH & SFTP sessions, should be used in cases when we want to force stop the task 
	 */
	public void killTask() {
		log.warn("Copy of " + sourceFileName + " failed (probabliy due to timeout), disconnecting SFTP connection");
		try {
			if(sftpChannel != null){
				sftpChannel.disconnect();
			}
			if(sftpSession != null){
				sftpSession.disconnect();
			}
		} catch (Exception e) {
		}
	}
}
