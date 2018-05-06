package com.botsystem.modules.noeveryone;

import com.botsystem.core.BotSystemModule;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NoEveryoneModule extends BotSystemModule {

    private PermissionsModule permissions;
    private String reqPerm;

    public NoEveryoneModule(String reqPerm) {
        this.reqPerm = reqPerm;
    }

    /**
     * Called on module initiation
     */
    @Override
    public void onStart() {
        super.onStart();

        permissions = bot.getModule(PermissionsModule.class); // getting the permissions module
        bot.addEvent(MessageReceivedEvent.class, e -> { // adding event for chat message
            MessageReceivedEvent me = (MessageReceivedEvent) e; // getting event info

            if (me.getMessage().mentionsEveryone()) { // if message includes @everyone

                if (!permissions.checkUserPerm(reqPerm, me.getMember())) { // if the user doesn't have the perm to do that

                    if (!me.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
                        return; // return if cannot delete message

                    me.getMessage().delete().queue(); // delete the message

                    BotSystemEmbed emb = new BotSystemEmbed(); // new embed

                    // adding emb information
                    emb.setTitle("Invalid Permissions");
                    emb.setDescription("You don't have the permissions to `@everyone` or `@here`");

                    // adding message and queueing
                    me.getChannel().sendMessage(emb.build()).queue();
                }
            }
        });
    }
}
