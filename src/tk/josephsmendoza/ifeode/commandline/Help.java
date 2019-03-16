package tk.josephsmendoza.ifeode.commandline;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import picocli.CommandLine;
import tk.josephsmendoza.ifeode.Common;

public class Help {

    public Help(CommandLine commandLine) {
	final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	try (PrintStream ps = new PrintStream(baos, true, "UTF-8")) {
	    commandLine.usage(ps);
	    String help = new String(baos.toByteArray(), StandardCharsets.UTF_8);
	    Common.getLogger().info(help);
	} catch (UnsupportedEncodingException e) {
	    Common.getLogger().severe(e.getMessage());
	}
    }
}
