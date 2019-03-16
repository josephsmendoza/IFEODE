package tk.josephsmendoza.ifeode;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;

public class RegEdit {

    private static final String IFEO = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Image File Execution Options\\";
    private static final String DEBUGGER = "Debugger";
    public static final String IFEODH = "\\ifeodec.exe";
    private static Map<String, String> debuggers;

    public static void set(String oldProg, String newProg, boolean direct) {
	try {
	    Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, IFEO + newProg);
	    if (direct) {
		Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + oldProg, DEBUGGER, newProg);
	    } else {
		Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + oldProg, DEBUGGER,
			new File(".").getAbsoluteFile().getParent() + IFEODH);
		Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + oldProg, IFEODH, newProg);
	    }
	    debuggers = null;
	} catch (Win32Exception e) {
	    Common.getLogger().severe(e.getMessage());
	    return;
	}
    }

    public static Map<String, String> get() {
	if (debuggers != null) {
	    return debuggers;
	}
	debuggers = new LinkedHashMap<>();
	for (String key : Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, IFEO)) {
	    if (Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, IFEO + key, DEBUGGER)) {
		String value = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + key, DEBUGGER);
		debuggers.put(key, value);
		if (value.endsWith(IFEODH)) {
		    value = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + key, IFEODH);
		    debuggers.put(key + IFEODH, value);
		}
	    }
	}
	return debuggers;
    }
}
