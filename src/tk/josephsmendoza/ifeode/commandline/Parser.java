package tk.josephsmendoza.ifeode.commandline;

import java.util.Map;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.UnmatchedArgumentException;
import tk.josephsmendoza.ifeode.Common;
import tk.josephsmendoza.ifeode.RegEdit;

@Command(name = "ifeodec.exe")
public class Parser {

    @Option(names = { "-o", "--old-prog" }, description = "File name of the old program to be replaced")
    private String oldProg;
    @Option(names = { "-n", "--new-prog" }, description = "Absolute path of new program")
    private String newProg;
    @Option(names = { "-p", "--pass-args" }, description = "Pass original args to new program")
    private boolean passArgs = false;
    @Option(names = { "-e", "--extra-args" }, description = "Extra args for new program")
    private String extraArgs;
    @Option(names = { "-d",
	    "--direct" }, description = "Use default IFEO Debugger behaviour. All optional args are ignored")
    private boolean direct = false;
    @Option(names = { "-h",
	    "--help" }, usageHelp = true, description = "Show this help text. All other args are ignored")
    private boolean help;
    @Option(names = { "-s",
	    "--show-binds" }, description = "Show current bindings. No other args required, but can be used together")
    private boolean showBinds;
    CommandLine commandLine = new CommandLine(this);

    public Parser() {
	try {
	    commandLine.parse(Common.getArgs());
	} catch (UnmatchedArgumentException e) {
	    Common.getLogger().severe(e.getMessage());
	    System.exit(1);
	}
	if (help) {
	    new Help(commandLine);
	    System.exit(0);
	}
	if (oldProg != null && newProg != null) {
	    parse();
	}
	if (showBinds) {
	    showBinds();
	}
    }

    private void showBinds() {
	final Map<String, String> debuggers = RegEdit.get();
	for (String key : debuggers.keySet()) {
	    if (key.endsWith(RegEdit.IFEODH)) {
		continue;
	    }
	    Common.getLogger().info(key);
	    String value = debuggers.get(key);
	    Common.getLogger().info(value);
	    if (value.endsWith(RegEdit.IFEODH)) {
		value = debuggers.get(key + RegEdit.IFEODH);
		Common.getLogger().info(value);
	    }
	    Common.getLogger().info("");
	}
    }

    private void parse() {
	if (!direct) {
	    if (passArgs) {
		newProg += " %*";
	    }
	    if (extraArgs != null) {
		newProg += " " + extraArgs;
	    }
	}
	RegEdit.set(oldProg, newProg, direct);
    }

}
