package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;

import java.io.File;
import java.util.List;

/**
 * A command to save a file to the server
 * @author BlockBa5her
 *
 */
public class SaveCommand extends BotCommand {

    private String cmd;
    private String reqPerm;
    private String saveFolder;

    /**
     * Creates an Instance of the "save" command
     * @param cmd The command to invoke with
     * @param reqPerm The required permission to invoke
     * @param saveFolder The folder to save the files in
     */
    public SaveCommand(String cmd, String reqPerm, String saveFolder) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
        this.saveFolder = saveFolder;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
    	// setup method
        BotSystemEmbed emb = new BotSystemEmbed();

        // get attachments
        List<Message.Attachment> attrList = m.getAttachments();
        if (attrList.size() == 0) { // if no attachments
        	// say so and return
            emb.setTitle("Not Enough Attachments");
            emb.setDescription("There was not enough attachments ");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        // get the first attachment
        Message.Attachment attr = attrList.get(0);
        // setup name and path
        String fileName = attr.getFileName();
        String filePath = saveFolder + "/" + fileName;
        // create file to that path
        File f = new File(filePath);

        // below basically just makes sure that it doesn't already exist
        int i = 0;
        while (f.exists()) {
            String[] split = filePath.split("\\.");

            String newFileName = split[0] + " (" + i + ")";
            if (split.length > 1)
                newFileName += "." + split[1];

            f = new File(newFileName);
            i++;
        }

        // saves file, and gets success
        boolean success = attr.download(f);

        // if succeeded
        if (success) {
        	// setup embed
            emb.setTitle("Saved File Successfully");
            emb.setDescription("The file `" + fileName + "` has been saved on the VPS");
        } else { // otherwise
        	// setup embed but for error
            emb.setTitle("Internal Error");
            emb.setDescription("An internal error, with JDA, caused the file to not be able to be saved :cry:");
        }
        // send channel message
        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Saves a file on the VPS HDD";
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
