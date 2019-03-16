package tk.josephsmendoza.ifeode;

import java.io.IOException;
import java.util.Map;

public class Handler {

    private static final String PASS_ARGS = "%\\*";

    public Handler() {
	final Map<String, String> debuggers = RegEdit.get();
	final String[] arg = Common.getArgs();
	String newProg = debuggers.get(arg[0] + RegEdit.IFEODH);
	if (newProg.contains(PASS_ARGS)) {
	    String args = "";
	    for (int i = 1; i < arg.length; i++) {
		args += " " + arg[i];
	    }
	    newProg = newProg.replace(PASS_ARGS, args);
	}
	try {
	    Runtime.getRuntime().exec(newProg);
	} catch (IOException e) {
	    Common.getLogger().severe(e.getMessage());
	}
    }

}
