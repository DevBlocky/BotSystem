package com.blockybot.modules.commands.command;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.monitor.MonitorModule;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * A bot command to monitor program CPU and Memory
 * 
 * @author BlockBa5her
 *
 */
public class MonitorCommand extends BotCommand {
    private static DecimalFormat format1 = new DecimalFormat("#0.0");
    private static DecimalFormat format2 = new DecimalFormat("#0.00");
    private static DecimalFormat format3 = new DecimalFormat("#0.000");

    /**
     * Creates an instance of the "monitor" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public MonitorCommand(String cmd) {
        super(cmd);
    }

    private static double bytesToMegabytes(long bytes) {
        return bytes / 1e+6;
    }

    /**
     * Get's the monitor info, and places it into a HashMap
     * 
     * @return
     */
    private static Map<String, String> getMonitorInfo(MonitorModule monitor) {
        // creating the return map
        Map<String, String> returnMap = new HashMap<>();

        // getting system info from monitor
        double processCpu = monitor.getProcessCpuLoad();
        double systemCpu = monitor.getSystemCpuLoad();
        long totalMemoryBytes = monitor.getTotalSystemMemory();
        long heapAlloc = monitor.getProcessHeapAllocationSize();
        long stackAlloc = monitor.getProcessNonheapAllocationSize();
        long totalAlloc = monitor.getProcessMemoryUsage();
        long systemMemoryUsageBytes = monitor.getSystemMemoryUsage();

        // getting strings for CPU
        returnMap.put("processCpuStr", format3.format(processCpu * 100));
        returnMap.put("systemCpuStr", systemCpu < 0d ? "N/A" : format3.format(systemCpu * 100));

        // getting strings for memory options
        returnMap.put("totalMemoryStr", format1.format(bytesToMegabytes(totalMemoryBytes)));
        returnMap.put("heapAllocStr", format1.format(bytesToMegabytes(heapAlloc)));
        returnMap.put("stackAllocStr", format1.format(bytesToMegabytes(stackAlloc)));
        returnMap.put("totalAllocStr", format1.format(bytesToMegabytes(totalAlloc)));
        returnMap.put("systemMemoryStr", format1.format(bytesToMegabytes(systemMemoryUsageBytes)));

        // getting percentages
        double heapAllocPercent = (double) heapAlloc / totalMemoryBytes;
        double stackAllocPercent = (double) stackAlloc / totalMemoryBytes;
        double totalAllocPercent = (double) totalAlloc / totalMemoryBytes;
        double systemMemoryPercent = (double) systemMemoryUsageBytes / totalMemoryBytes;

        // percent to string
        returnMap.put("heapAllocPercentStr", format2.format(heapAllocPercent * 100));
        returnMap.put("stackAllocPercentStr", format2.format(stackAllocPercent * 100));
        returnMap.put("totalAllocPercentStr", format2.format(totalAllocPercent * 100));
        returnMap.put("systemMemoryPercentStr", format2.format(systemMemoryPercent * 100));

        return returnMap;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        Map<String, String> info = getMonitorInfo(parent.getBot().getModule(MonitorModule.class));

        // creating embed
        BlockyBotEmbed emb = new BlockyBotEmbed();
        emb.setTitle("System Monitor");

        // adding information for stuff above
        emb.addField(new MessageEmbed.Field("Process CPU Load", info.get("processCpuStr") + "%", true));
        emb.addField(new MessageEmbed.Field("System CPU Load", info.get("systemCpuStr") + "%", true));
        emb.addField(new MessageEmbed.Field("Process Heap Memory Allocation", info.get("heapAllocStr") + "MB / "
                + info.get("totalMemoryStr") + "MB (" + info.get("heapAllocPercentStr") + "%)", true));
        emb.addField(new MessageEmbed.Field("Process Stack Memory Allocation", info.get("stackAllocStr") + "MB / "
                + info.get("totalMemoryStr") + "MB (" + info.get("stackAllocPercentStr") + "%)", true));
        emb.addField(new MessageEmbed.Field("Total Process Memory Allocation", info.get("totalAllocStr") + "MB / "
                + info.get("totalMemoryStr") + "MB (" + info.get("totalAllocPercentStr") + "%)", true));
        emb.addField(new MessageEmbed.Field("System Memory Usage", info.get("systemMemoryStr") + "MB / "
                + info.get("totalMemoryStr") + "MB (" + info.get("systemMemoryPercentStr") + "%)", true));

        // sending message to channel
        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Displays statistical info about the bot on the computer";
    }
}
