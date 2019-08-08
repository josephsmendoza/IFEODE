import core.memory, std.stdio, std.getopt, std.path, std.algorithm.searching, std.windows.registry;

string oldprog, newprog, extraargs;
bool passargs, direct, showbinds;
immutable string desc="Set IFEOD and IFEODE registry values";
immutable string db="Debugger";

void main(string[] args) {
	GC.disable();

	auto xargs=getopt(args,
	"old-prog|o", "program to be debugged/replaced, or delete value when provided without --new-prog", &oldprog,
	"new-prog|n", "debugger/replacement prog", &newprog,
	//"extra-args|e", "extra args to pass to new-prog", &extraargs,
	//"pass-args|p", "pass old-prog args to new-prog", &passargs,
	//"direct|d", "use default IFEO behaviour", &direct,
	"show-binds|s", "show current IFEOE settings", &showbinds
	);

	if(xargs.helpWanted){
		defaultGetoptPrinter(desc,xargs.options);
		return;
	}

	if(!showbinds && !oldprog){
		"Missing args".writeln;
		defaultGetoptPrinter(desc,xargs.options);
		return;
	}

	Key ifeo=Registry.localMachine().getKey("SOFTWARE")
		.getKey("Microsoft").getKey("Windows NT")
		.getKey("CurrentVersion").getKey("Image File Execution Options");
	
	if(oldprog){
		auto key = ifeo.createKey(oldprog,REGSAM.KEY_ALL_ACCESS);
		if(showbinds){
			"pre value:".writeln;
			key.writeValue;
		}
		if(newprog){
			if(!newprog.isAbsolute){
				throw new Exception("new-prog must be an absolute path");
			}
			key.setValue(db,newprog);
		} else {
			key.deleteValue(db);
		}
		key.flush();
	}

	if(showbinds){
		"post values:".writeln;
		foreach (key;ifeo.keys()) { key.writeValue; }
	}
}

void writeValue(Key key) {
	foreach (value;key.values()){
		if(value.name==db){
			key.name.write;
			" >> ".write;
			value.value_SZ.writeln;
			return;
		}
	}
}