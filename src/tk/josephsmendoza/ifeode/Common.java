package tk.josephsmendoza.ifeode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import tk.josephsmendoza.ifeode.commandline.Parser;

public class Common {

    private static final String GUI = "--gui";
    private static final String EXE_FILE_EXTENTION = ".exe";
    private static ExecutorService executorService;
    private static Logger logger;
    private static String[] args;

    public static void main(String... args) {
	setArgs(args);
	initLogger();
	if (args.length == 0) {
	    setArgs("--help");
	    initCommandLine();
	    return;
	}
	if (args[0].equalsIgnoreCase(GUI)) {
	    initExecutorService();
	    initGUI();
	    return;
	}
	if (args[0].endsWith(EXE_FILE_EXTENTION)) {
	    initHandler();
	    return;
	}
	initCommandLine();
    }

    private static void setArgs(String... args) {
	Common.args = args;
    }

    private static void initCommandLine() {
	new Parser();
    }

    private static void initHandler() {
	new Handler();
    }

    private static void initLogger() {
	LogManager.getLogManager().reset();
	logger = Logger.getGlobal();
	logger.addHandler(new StreamHandler(System.out, new Formatter() {

	    @Override
	    public String format(LogRecord record) {
		return "\n" + record.getMessage();
	    }

	}));
    }

    private static void initExecutorService() {
	executorService = Executors.newCachedThreadPool();
    }

    private static void initGUI() {
	// TODO Auto-generated method stub

    }

    public static String[] getArgs() {
	return args;
    }

    public static Logger getLogger() {
	return logger;
    }

}
