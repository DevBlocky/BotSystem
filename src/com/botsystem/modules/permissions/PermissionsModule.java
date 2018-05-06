package com.botsystem.modules.permissions;

import com.botsystem.Debug;
import com.botsystem.core.BotSystemModule;
import com.botsystem.extensions.Pair;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.LinkedList;
import java.util.List;

public class PermissionsModule extends BotSystemModule {

    private class InternalPermission {
        String name;
        String role;

        InternalPermission(String name, String role) {
            this.name = name;
            this.role = role;
        }
    }

    private List<InternalPermission> perms;

    private PermissionsModule() {
        perms = new LinkedList<>();
    }

    public PermissionsModule(List<Pair<String, String>> permissions) {
        this();
        for (Pair<String, String> p : permissions) {
            perms.add(new InternalPermission(p.getKey(), p.getValue()));
        }
    }

    private int indexOfName(String name) {
        for (int i = 0; i < perms.size(); i++) {
            if (perms.get(i).name.equals(name))
                return i;
        }
        return -1;
    }

    private InternalPermission getFromRoleId(String roleId) {
        for (InternalPermission p : perms) {
            if (p.role.equals(roleId))
                return p;
        }
        return null;
    }

    public String[] getUserPermRoles(Member m) {
        List<String> tmp = new LinkedList<>();
        for (Role r : m.getRoles()) {
            InternalPermission p = getFromRoleId(r.getId());
            if (p != null) {
                tmp.add(p.role);
            }
        }
        return tmp.toArray(new String[0]);
    }

    public String getMemberHighestPermName(Member m) {
        String[] perms = getUserPermRoles(m);
        if (perms.length == 0)
            return null;
        else
            return getFromRoleId(perms[0]).name;
    }

    public boolean checkUserPerm(String permName, Member m) {
        boolean found = false;
        int reqI = indexOfName(permName);
        if (reqI == -1)
            return false;
        InternalPermission req = perms.get(reqI);

        if (req.role.equals(m.getGuild().getPublicRole().getId()))
            return true;

        LinkedList<String> userPerms = new LinkedList<>();
        for (Role role : m.getRoles()) {
            InternalPermission perm = getFromRoleId(role.getId());
            if (perm != null) {
                userPerms.add(perm.name);
            }
        }

        for (String pName : userPerms) {
            if (indexOfName(pName) <= reqI) {
                found = true;
                break;
            }
        }

        return found;
    }

    public void addPermission(String permName, String roleId, String before) {
        if (indexOfName(permName) != -1)
            return; // permName already exists in this

        int index = 0;
        if (before != null) {
            index = indexOfName(before);
            if (index == -1)
                throw new RuntimeException(new ArrayStoreException("'array' doesn't exist in array"));
        }

        perms.add(index, new InternalPermission(permName, roleId));
        Debug.trace(perms.get(0).name);
    }

    public boolean roleExists(String roleName) {
        return indexOfName(roleName) != -1;
    }
}
