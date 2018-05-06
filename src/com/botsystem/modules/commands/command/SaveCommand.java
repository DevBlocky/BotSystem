package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;

import java.io.File;
import java.util.List;

public class SaveCommand extends BotCommand {

    private String cmd;
    private String reqPerm;
    private String saveFolder;

    public SaveCommand(String cmd, String reqPerm, String saveFolder) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
        this.saveFolder = saveFolder;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();

        List<Message.Attachment> attrList = m.getAttachments();
        if (attrList.size() == 0) {
            emb.setTitle("Not Enough Attachments");
            emb.setDescription("There was not enough attachments ");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        Message.Attachment attr = attrList.get(0);
        String fileName = attr.getFileName();
        String filePath = saveFolder + "/" + fileName;
        File f = new File(filePath);

        int i = 0;
        while (f.exists()) {
            String[] split = filePath.split("\\.");

            String newFileName = split[0] + " (" + i + ")";
            if (split.length > 1)
                newFileName += "." + split[1];

            f = new File(newFileName);
            i++;
        }

        boolean success = attr.download(f);

        if (success) {
            emb.setTitle("Saved File Successfully");
            emb.setDescription("The file `" + fileName + "` has been saved on the VPS");
        } else {
            emb.setTitle("Internal Error");
            emb.setDescription("An internal error, with JDA, caused the file to not be able to be saved :cry:");
        }
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
