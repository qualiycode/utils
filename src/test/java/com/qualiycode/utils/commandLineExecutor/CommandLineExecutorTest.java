package com.qualiycode.utils.commandLineExecutor;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CommandLineExecutorTest {

	/**
	 * This test execute command to the shell and verify that result is not empty
	 * @throws Exception
	 */
	@Test
	public void runShellCommand() throws Exception{
		CommandLineExecutor shell = new CommandLineExecutor();
		String command;
		if(SystemUtils.IS_OS_LINUX){
			command = "ls -l";
		}else{
			command = "dir";
		}
		shell.executeCommand(command);
		
		String commandOutput = shell.getInput().toString();
		
		Assert.assertTrue(commandOutput.length() > 0);
	}
}
