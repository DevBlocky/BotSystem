package com.botsystem.modules.noeveryone;

import com.botsystem.core.BotSystemModule;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Module for BotSystem to make sure people don't @everyone without permission
 * @author BlockBa5her
 *
 */
public class NoEveryoneModule extends BotSystemModule {

    private PermissionsModule permissions;
    private String reqPerm;

    /**
     * Initializes instance of 
     * @param reqPerm The required permission to @everyone
     */
    public NoEveryoneModule(String reqPerm) {
        this.reqPerm = reqPerm;
    }

    @Override
    public void onStart() {
        super.onStart();

        permissions = bot.getModule(PermissionsModule.class); // getting the permissions module
        
        bot.addEvent(MessageReceivedEvent.class, e -> { // adding event for chat message
            if (e.getMessage().mentionsEveryone()) { // if message includes @everyone

                if (!permissions.checkUserPerm(reqPerm, e.getMember())) { // if the user doesn't have the perm to do that

                    if (!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
                        return; // return if cannot delete message

                    e.getMessage().delete().queue(); // delete the message

                    BotSystemEmbed emb = new BotSystemEmbed(); // new embed

                    // adding embed information
                    emb.setTitle("Invalid Permissions");
                    emb.setDescription("You don't have the permissions to `@everyone` or `@here`");

                    // adding message and then queue
                    e.getChannel().sendMessage(emb.build()).queue();
                }
            }
        });
    }
}
