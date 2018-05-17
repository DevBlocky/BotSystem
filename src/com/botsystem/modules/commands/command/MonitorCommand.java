package com.botsystem.modules.commands.command;

import java.text.DecimalFormat;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.monitor.MonitorModule;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * A bot command to monitor program CPU and Memory
 * @author BlockBa5her
 *
 */
public class MonitorCommand extends BotCommand {

	private String cmd;
	private String reqPerm;
	
	/**
	 * Creates an instance of the "monitor" command
	 * @param cmd The command to invoke with
	 * @param reqPerm The required permission to invoke
	 */
	public MonitorCommand(String cmd, String reqPerm) {
		this.cmd = cmd;
		this.reqPerm = reqPerm;
	}
	
	private static double bytesToMegabytes(long bytes) {
		return bytes / 1e+6;
	}
	
	@Override
	public void onInvoke(Message m, String[] args) {
		// creating decimal formatter
		DecimalFormat format0 = new DecimalFormat("#0");
		DecimalFormat format2 = new DecimalFormat("#0.00");
		DecimalFormat format3 = new DecimalFormat("#0.000");
		
		// getting monitor from the bot
		MonitorModule monitor = parent.getBot().getModule(MonitorModule.class);
		
		// getting system info from monitor
		double processCpu = monitor.getProcessCpuLoad();
		double systemCpu = monitor.getSystemCpuLoad();
		long totalMemoryBytes = monitor.getTotalSystemMemory();
		long virtualMemoryUsageBytes = monitor.getProcessMemoryUsage();
		long systemMemoryUsageBytes = monitor.getSystemMemoryUsage();
		
		// getting strings for CPU
		String processCpuStr = format3.format(processCpu * 100);
		String systemCpuStr = systemCpu < 0d ? "N/A" : format3.format(systemCpu * 100);
		
		// getting strings for memory options
		String totalMemoryStr = format0.format(bytesToMegabytes(totalMemoryBytes));
		String virtualMemoryStr = format0.format(bytesToMegabytes(virtualMemoryUsageBytes));
		String systemMemoryStr = format0.format(bytesToMegabytes(systemMemoryUsageBytes));
		
		// getting percentages
		double virtualMemoryPercent = (double)virtualMemoryUsageBytes / totalMemoryBytes;
		double systemMemoryPercent = (double)systemMemoryUsageBytes / totalMemoryBytes;
		
		// percent to string
		String virtualMemoryPercentStr = format2.format(virtualMemoryPercent * 100);
		String systemMemoryPercentStr = format2.format(systemMemoryPercent * 100);
		
		// creating embed
		BotSystemEmbed emb = new BotSystemEmbed();
		emb.setTitle("System Monitor");
		
		// adding information for stuff above
		emb.addField(new MessageEmbed.Field(
				"Process CPU Load",
				processCpuStr + "%",
				false));
		emb.addField(new MessageEmbed.Field(
				"System CPU Load",
				systemCpuStr + "%",
				false));
		emb.addField(new MessageEmbed.Field(
				"Process Memory Usage",
				virtualMemoryStr + "MB / " + totalMemoryStr + "MB (" + virtualMemoryPercentStr + "%)",
				false));
		emb.addField(new MessageEmbed.Field(
				"System Memory Usage",
				systemMemoryStr + "MB / " + totalMemoryStr + "MB (" + systemMemoryPercentStr + "%)",
				false));
		
		// sending message to channel
		m.getChannel().sendMessage(emb.build()).queue();
	}

	@Override
	public String getDesc() {
		return "Displays statistical info about the bot on the computer";
	}

	@Override
	public String getRequiredPerm() {
		return reqPerm;
	}

	@Override
	public String getCmd() {
		return cmd;
	}
}
