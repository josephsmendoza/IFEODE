package tk.josephsmendoza.wifedos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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
    private boolean help = false;

    public static void main(String[] args) {
	WIFEDOS wifedos = new WIFEDOS();
	CmdLineParser parser = new CmdLineParser(wifedos);
	try {
	    parser.parseArgument(args);
	} catch (Exception e) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    parser.printUsage(baos);
	    errOut("Exception during argument parsing", e.getMessage());
	}
	if (wifedos.help) {
	    ByteArrayOutputStream boas = new ByteArrayOutputStream();
	    parser.printUsage(boas);
	    String help = "\n" + new String(boas.toByteArray());
	    log.info(help);
	    System.exit(0);
	}
	if (!extraCliArgs.isEmpty()) {
	    errOut("Extra args", extraCliArgs.toString());
	}
	if (wifedos.oldProg == null || wifedos.newProg == null) {
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
	// If oldProg does not have an IFEO key, make one
	if (!keys.contains(oldProg)) {
	    Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, IFEO, oldProg);
	}
	// In direct mode, just set the IFEO Debugger to newProg and exit
	if (direct) {
	    Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, IFEO + oldProg, DO, newProg);
	    return;
	}
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
