package com.botsystem.modules.permissions;

import com.botsystem.Debug;
import com.botsystem.core.BotSystemModule;
import com.botsystem.extensions.Pair;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.LinkedList;
import java.util.List;

/**
 * Module used for generic permissions without BotSystem
 * @author BlockBa5her
 *
 */
public class PermissionsModule extends BotSystemModule {

	/**
	 * A class to keep track of internal permissions (name and role)
	 * @author BlockBa5her
	 *
	 */
    private class InternalPermission {
        String name;
        String role;

        InternalPermission(String name, String role) {
            this.name = name;
            this.role = role;
        }
        
        /**
         * Converts this InternalPermission into a pair
         * @return The pair converted
         */
        public Pair<String, String> toPair() {
        	return new Pair<>(name, role);
        }
    }

    // the perms
    private List<InternalPermission> perms;

    /**
     * Class Private constructor for initializing everything
     */
    private PermissionsModule() {
        perms = new LinkedList<>();
    }

    /**
     * Initializes the Permissions module with the given permissions
     * @param permissions The initial permissions of the module
     */
    public PermissionsModule(List<Pair<String, String>> permissions) {
        this();
        for (Pair<String, String> p : permissions) {
            perms.add(new InternalPermission(p.getKey(), p.getValue()));
        }
    }

    /**
     * Finds the index of something if it has the name
     * @param name The name of what to find
     * @return The index of the element (-1 if not found)
     */
    private int indexOfName(String name) {
    	// for every perm
        for (int i = 0; i < perms.size(); i++) {
            if (perms.get(i).name.equals(name)) // if perm.name == name
                return i; // return name
        }
        return -1; // if nothing found, return -1
    }

    /**
     * Gets the InternalPermission for the Role Id
     * @param roleId The role id to search for
     * @return The permission found (null if not found)
     */
    private InternalPermission getFromRoleId(String roleId) {
        for (InternalPermission p : perms) { // for every permission
            if (p.role.equals(roleId)) // if role.id == roleId
                return p; // return it
        }
        return null; // return null if not found
    }

    /**
     * Class Private method to get the user's internal permissions
     * @param m The member to look for
     * @return
     */
    private InternalPermission[] getUserPerms(Member m) {
        List<InternalPermission> tmp = new LinkedList<>(); // tmp list for the perms
        for (Role r : m.getRoles()) { // for every role on the person
            InternalPermission p = getFromRoleId(r.getId()); // get the internal permission for the role
            if (p != null) { // if found the permission
                tmp.add(p); // add it to the tmp list
            }
        }
        return tmp.toArray(new InternalPermission[0]); // return the tmp list to an array
    }
    
    /**
     * Finds a members permissions
     * @param m The member to search permissions for
     * @return The member's permissions in a List<Pair<String, String>>
     */
    public List<Pair<String, String>> getMemberPerms(Member m) {
    	InternalPermission[] perms = getUserPerms(m); // get internal perms
    	List<Pair<String, String>> pub = new LinkedList<>(); // new tmp list
    	for (InternalPermission p : perms) {
    		pub.add(p.toPair()); // add to tmp list as a pair
    	}
    	return pub; // return tmp list
    }

    /**
     * Gets the highest permission of a user
     * @param m The member to look for
     * @return
     */
    public Pair<String, String> getMemberHighestPerm(Member m) {
        InternalPermission[] perms = getUserPerms(m); // finds all of the users permissions
        if (perms.length == 0) // if no permissions
            return null; // return nothing
        else // otherwise
            return perms[0].toPair(); // return the role as a pair
    }

    /**
     * Checks the user's permission to the given permission
     * @param permName The permission name to check to
     * @param m The member to check against
     * @return Whether the member has the permission
     */
    public boolean checkUserPerm(String permName, Member m) {
        boolean found = false; // bool for later
        int reqI = indexOfName(permName); // find the index of the given perm name
        if (reqI == -1) // if not found
            return false; // return false (doesnt have it)
        InternalPermission req = perms.get(reqI); // get the internal permission of the index

        if (req.role.equals(m.getGuild().getPublicRole().getId())) // if the required role is @everyone
            return true;

        InternalPermission[] userPerms = this.getUserPerms(m); // get the user's internal perms

        for (InternalPermission p : userPerms) { // for every user permission
            if (indexOfName(p.name) <= reqI) { // if index is less than or equal to the required index
                found = true; // found is now equal to true
                break; // break from loop
            }
        }

        return found; // return if found
    }

    /**
     * Adds a permission to the list of permissions
     * @param permName The permission name to add
     * @param roleId The role id to add
     * @param before Where to add it
     */
    public void addPermission(String permName, String roleId, String before) {
        if (indexOfName(permName) != -1)
            return; // permName already exists in this

        int index = 0; // the index
        if (before != null) { // if there is a before
            index = indexOfName(before); // find the index of the name of before
            if (index == -1) // if not found
                throw new RuntimeException(new ArrayStoreException("'before' doesn't exist in array")); // throw exception
        }

        perms.add(index, new InternalPermission(permName, roleId)); // add the permission
        Debug.trace(perms.get(0).name); // debug trace
    }

    /**
     * Checks whether a role already exists
     * @param roleName The role name
     * @return Whether the role exists
     */
    public boolean roleExists(String roleName) {
        return indexOfName(roleName) != -1;
    }
}
