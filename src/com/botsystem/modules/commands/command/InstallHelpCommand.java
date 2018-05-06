package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommandsModule;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InstallHelpCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    private Map<String, List<MessageEmbed.Field>> responses = new HashMap<>();

    /**
     * Creates an instance of the "installhelp" command
     * @param cmd The command to be invoked with
     * @param reqPerm The required permission to be invoked with
     */
    public InstallHelpCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;

        setResponses();
    }

    /**
     * Sets all of the responses to the command in the "responses" variable
     */
    private void setResponses() {
        List<MessageEmbed.Field> f1 = new LinkedList<>();

        f1.add(new MessageEmbed.Field("Depreciated Version", "" +
                "This version is depreciated, please upgrade to the latest version " +
                "[here](https://github.com/DispatchSystems/DispatchSystem/releases)", false));

        List<MessageEmbed.Field> f2 = new LinkedList<>();

        f2.add(new MessageEmbed.Field("Step 1", "" +
                "Download the release.zip file from [here](https://github.com/DispatchSystems/DispatchSystem/releases) (if not done already)", false));
        f2.add(new MessageEmbed.Field("Step 2", "" +
                "Unpack the .zip file using 7zip or WinRaR", false));
        f2.add(new MessageEmbed.Field("Step 3", "" +
                "Drag the folder `dispatchsystem` inside of the `FiveM Resource` folder into your FiveM resources folder", false));
        f2.add(new MessageEmbed.Field("Step 4", "" +
                "***This and onward are optional.***\nPort forward port `33333` to use Terminal", false));
        f2.add(new MessageEmbed.Field("Step 5", "" +
                "Change the `settings.ini` file inside of the `Terminal` folder, then open the `Terminal.exe` to run the terminal", false));

        List<MessageEmbed.Field> f3 = new LinkedList<>();

        f3.add(new MessageEmbed.Field("Step 1", "" +
                "No download available, get from the channel <#403769028793073665>", false));
        f3.add(new MessageEmbed.Field("Step 2", "" +
                "Unpack the .zip file using 7zip or WinRaR", false));
        f3.add(new MessageEmbed.Field("Step 3", "" +
                "Drag the folder `dispatchsystem` and `ds-main` inside of the `FiveM Resources` folder into your FiveM server's resources folder", false));
        f3.add(new MessageEmbed.Field("Step 4", "" +
                "Drag the `dispatchsystem.cfg` that's inside of the `FiveM Resources` folder into your root FiveM directory", false));
        f3.add(new MessageEmbed.Field("Step 5", "" +
                "Inside of the `server.cfg`, put the line `exec dispatchsystem.cfg` for the last line", false));
        f3.add(new MessageEmbed.Field("Step 6", "" +
                "***This and onward are optional.***\nPort forward port `33333` to use Terminal", false));
        f3.add(new MessageEmbed.Field("Step 7", "" +
                "Change the `settings.ini` file inside of the `Terminal` folder, then open the `Terminal.exe` to run the terminal", false));

        responses.put("1", f1);
        responses.put("2", f2);
        responses.put("3", f2);
    }

    /**
     * Invokes the "installhelp" command, with the message
     * @param m The message
     * @param args The arguments of the message
     */
    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();

        if (args.length == 0) {
            emb.setTitle("No Version Inputted");
            emb.setDescription("" +
                    "You need to include a version as an argument in this command.\n\n" +
                    "Example: `" + BotCommandsModule.HEADER + "installhelp 2.2`");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        for (String key : responses.keySet()) {
            if (args[0].startsWith(key)) {
                for (MessageEmbed.Field f : responses.get(key)) {
                    emb.addField(f);
                }

                m.getChannel().sendMessage(emb.build()).queue();
                return;
            }
        }

        emb.setTitle("Invalid Version Inputted");
        emb.setDescription("" +
                "You inputted a version that doesn't exist!\n\n" +
                "Example: `" + BotCommandsModule.HEADER + "installhelp 2.2`");
        m.getChannel().sendMessage(emb.build()).queue();
    }

    /**
     * @return The description of the command
     */
    @Override
    public String getDesc() {
        return "Displays helpful information for installing DispatchSystem";
    }

    /**
     * @return The required permission for the command
     */
    @Override
    public String getRequiredPerm() {
        return reqPerm;
    }

    /**
     * @return The command invoke text for this command
     */
    @Override
    public String getCmd() {
        return cmd;
    }
}
