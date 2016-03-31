package com.qualiycode.utils.commandLineExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * This class is a command line executor
 * 
 * @author Eli Rozenfeld
 *
 */
public class CommandLineExecutor {
	private static final Logger log = Logger.getLogger(CommandLineExecutor.class);

	private StringBuffer input;
	private StringBuffer error;
	
	/**
	 * This function execute a command in the linux shell
	 * 
	 * @param command - the command to execute
	 */
	public void executeCommand(String command){
		try {
			input = new StringBuffer();
			error = new StringBuffer();
			
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			
			String tmpStr = stdInput.readLine();
			while (tmpStr != null) {
			    input.append(tmpStr + "\r\n");
				tmpStr = stdInput.readLine();
			}
			
			tmpStr = stdError.readLine();
			while (tmpStr != null) {
			    error.append(tmpStr);
				tmpStr = stdError.readLine();
			}
		} catch (IOException e) {
			log.fatal("Unable to execute command in command line (command = " + command + ")",e);
		}
	}

	/**
	 * @return the command input
	 */
	public StringBuffer getInput() {
		return input;
	}

	/**
	 * @return the command error
	 */
	public StringBuffer getError() {
		return error;
	}

}