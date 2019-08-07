import std.stdio, std.getopt, std.windows.registry;

string oldprog, newprog, extraargs;
bool passargs, direct, help, showbinds;
immutable string desc="Set IFEOD and IFEODE registry values";
immutable string neargs="Missing args";

void main(string[] args) {

	GetoptResult xargs=getopt(args,
	"old-prog|o", "program to be debugged/replaced", &oldprog,
	"new-prog|n", "debugger/replacement prog", &newprog,
	"extra-args|e", "extra args to pass to new-prog", &extraargs,
	"pass-args|p", "pass old-prog args to new-prog", &passargs,
	"direct|d", "use default IFEOD behaviour", &direct,
	"showbinds|s", "show current IFEODE settings", &showbinds
	);

	if(xargs.helpWanted){
		defaultGetoptPrinter(desc,xargs.options);
		return;
	}

	if(!showbinds && !oldprog){
		neargs.writeln;
		defaultGetoptPrinter(desc,xargs.options);
		return;
	}

	Key ifeod=Registry.localMachine().getKey("SOFTWARE")
		.getKey("Microsoft").getKey("Windows NT")
		.getKey("CurrentVersion").getKey("Image File Execution Options");
	
	if(showbinds){
		foreach (x;ifeod.keyNames()){
			Key k=ifeod.getKey(x);
			foreach (y;k.valueNames()){
				if(y=="Debugger"){
					x.write;
					"=".write;
					k.getValue("Debugger").value_EXPAND_SZ().writeln;
				}
			}
		}
	}
}
