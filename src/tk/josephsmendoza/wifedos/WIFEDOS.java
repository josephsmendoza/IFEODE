package tk.josephsmendoza.wifedos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public class WIFEDOS implements Serializable, Runnable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String IFEO = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Image File Execution Options\\";
    private static final String DO = "Debugger";
    private static final String GLOBALDOH = "C:\\ProgramData\\WIFEDOS\\";
    private static final String ARGS = "%*";
    private static final String WIFEDOH = "C:\\Program Files\\WIFEDOS\\WIFEDOH.exe";
    private static final String TXT = ".txt";
    private static final Logger log = Logger.getGlobal();
    @Argument
    private static List<String> extraCliArgs = new LinkedList<>();
    @Option(name = "-o", usage = "File name of the old program to be replaced (required)")
    private String oldProg;
    @Option(name = "-n", usage = "Absolute path of new program (required)")
    private String newProg;
    @Option(name = "-p", usage = "Pass original args to new program")
    private boolean passArgs = false;
    @Option(name = "-e", usage = "Extra args for new program")
    private String extraArgs;
    @Option(name = "-d", usage = "Use default IFEO behaviour. All optional args are ignored")
    private boolean direct = false;
    @Option(name = "-h", usage = "Show this help text. All other args are ignored")
    private boolean help;
    @Option(name = "-s", usage = "Show current bindings. No other args required, but can be used together")
    private boolean showBinds;

    public static void main(String[] args) {
	LogManager.getLogManager().reset();
	log.addHandler(new StreamHandler(System.out, new Formatter() {

	    @Override
	    public String format(LogRecord record) {
		return record.getMessage() + "\n";
	    }

	}));
	WIFEDOS wifedos = new WIFEDOS();
	CmdLineParser parser = new CmdLineParser(wifedos);
	try {
	    parser.parseArgument(args);
	} catch (Exception e) {
	    errOut("Exception during argument parsing", e.getMessage());
	}
	if (wifedos.help) {
	    ByteArrayOutputStream boas = new ByteArrayOutputStream();
	    parser.printUsage(boas);
	    String help = new String(boas.toByteArray());
	    log.info(help);
	    System.exit(0);
	}
	if (!extraCliArgs.isEmpty()) {
	    errOut("Extra args", extraCliArgs.toString());
	}
	if ((wifedos.oldProg == null || wifedos.newProg == null) && !wifedos.showBinds) {
	    errOut("Missing args");
	}
	wifedos.run();
    }

    private static void errOut(String... strings) {
	for (String s : strings) {
	    log.severe(s);
	}
	System.exit(1);
    }

    @Override
    public void run() {
	// Get the list of IFEO keys
	List<String> keys = Arrays.asList(Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, IFEO));
	if (oldProg != null && newProg != null) {
	    // If oldProg does not have an IFEO key, make one
	    if (!keys.contains(oldProg)) {
		Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, IFEO, oldProg);
	    }
	    if (direct) {
		// In direct mode, set the IFEO Debugger to newProg
		Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + oldProg, DO, newProg);
	    } else {
		// In indirect mode, set the IFEO Debugger to the WIFEDOS handler
		Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + oldProg, DO, WIFEDOH);
		// if it's enabled, pass the full oldProg command to newProg
		if (passArgs) {
		    newProg += ARGS;
		}
		// add any extra args to the end of the command
		if (extraArgs != null) {
		    newProg += " " + extraArgs;
		}
		// write config to global location
		File f = new File(GLOBALDOH + oldProg + TXT);
		f.mkdirs();
		if (f.exists()) {
		    try {
			Files.delete(f.toPath());
		    } catch (IOException e) {
			errOut("Exception while deleting old config", e.getMessage());
		    }
		}
		try (FileWriter fw = new FileWriter(f)) { // write new command
		    fw.write(newProg);
		} catch (Exception e) {
		    errOut("Exception while writing new config", e.getMessage());
		}
	    }
	}
	if (showBinds) {
	    for (String key : keys) {
		if (Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, IFEO + key, DO)) {
		    log.info(key);
		    String val = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + key, DO);
		    log.info(val);
		    if (val.equals(WIFEDOH)) {
			try {
			    log.info(new String(Files.readAllBytes(Paths.get(GLOBALDOH + key + TXT))));
			} catch (IOException e) {
			    log.severe(e.getMessage());
			}
		    }
		}
	    }
	}
    }
}
