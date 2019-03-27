package tk.josephsmendoza.ifeode.installer;

import com.sun.jna.platform.win32.Advapi32Util;

public class Link {

    public enum Location {
	DESKTOP, START_MENU
    }

    private static final String TARGET = "ifeode.exe";
    private static final String LINK = "\\ifeode.lnk";
    private static final String REG_SHELL_FOLDERS = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";
    private static final String REG_START = "Common Start Menu";
    private static final String REG_USER_SHELL_FOLDERS = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders";
    private static final String REG_DESKTOP = "Desktop";

    public static void make(Location l) {
	if (l == Location.DESKTOP) {
	    // TODO
	}
    }

}
