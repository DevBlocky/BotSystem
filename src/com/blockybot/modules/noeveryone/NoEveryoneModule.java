package com.blockybot.modules.noeveryone;

import com.blockybot.core.BlockyBotModule;
import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.permissions.PermissionsModule;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Module for BlockyBot to make sure people don't @everyone without permission
 * 
 * @author BlockBa5her
 *
 */
public class NoEveryoneModule extends BlockyBotModule {

    private PermissionsModule permissions;

    @Override
    public void onStart() {
        super.onStart();

        permissions = bot.getModule(PermissionsModule.class); // getting the permissions module

        bot.addEvent(MessageReceivedEvent.class, e -> { // adding event for chat message
            if (e.getMessage().mentionsEveryone()) { // if message includes @everyone

                if (!permissions.checkUserPerm(config.getString("permission"), e.getMember())) { // if the user doesn't have the perm to do
                                                                          // that

                    if (!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
                        return; // return if cannot delete message

                    e.getMessage().delete().queue(); // delete the message

                    BlockyBotEmbed emb = new BlockyBotEmbed(); // new embed

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
