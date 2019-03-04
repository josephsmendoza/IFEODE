package tk.josephsmendoza.WIFEDOS;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;

public class WIFEODS {

    public static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final String WIFEO = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Image File Execution Options\\";
    private static final String DS = "Debugger";
    private static Map<String, String> reg;

    public static void main(String[] args) {
	new GUI();
    }

    public static Map<String, String> getReg() {
	if (reg != null) {
	    return reg;
	}
	reg=new LinkedHashMap<String,String>();
	for (String key : Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, WIFEO)) {
	    String value;
	    try {
		value = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, WIFEO + key, DS);
	    } catch (Win32Exception e) {
		value = "";
	    }
	    reg.put(key, value);
	}
	return reg;
    }

}
