using System;
using System.Linq;
using System.IO;

namespace IFEODE
{
    class Program
    {
        private static String oldProg;
        private static Boolean oldProgSet=false;
        private static String newProg;
        private static Boolean newProgSet=false;
        private static readonly String[] EXIT_MESSAGES = new String[]{"Done","Missing args","Incorrect args","Extra args"};
        private static readonly int EXIT_SUCCESS=0;
        private static readonly int EXIT_ARGS_MISSING=1;
        private static readonly int EXIT_ARGS_INCORRECT=2;
        private static readonly int EXIT_ARGS_EXTRA=3;
        private static readonly String HELP_TEXT="Usage: ifeode.exe [program.exe] [\\path\\to\\debugger.exe]\nprovide program.exe alone to remove an entry";
        static int Main(string[] args)
        {
            if(args.Length == 0){
                return exit(EXIT_ARGS_MISSING);
            }
            if(args.Length > 2){
                return exit(EXIT_ARGS_EXTRA);
            }
            foreach(String arg in args){
                if(arg.EndsWith(".exe")){
                    if(Path.IsPathRooted(arg) && !newProgSet){
                        newProg=arg;
                        newProgSet=true;
                    }else{
                        if(arg.Contains(Path.DirectorySeparatorChar+"") || oldProgSet){
                            return exit(2);
                        }else{
                            oldProg=arg;
                            oldProgSet=true;
                        }
                    }
                    continue;
                }else{
                    return exit(EXIT_ARGS_INCORRECT);
                }
            }
            Console.WriteLine(oldProg);
            Console.WriteLine(newProg);
            return exit(EXIT_SUCCESS);
        }

        private static int exit(int exitCode){
            Console.WriteLine(EXIT_MESSAGES[exitCode]);
            if(exitCode > 0){
                Console.WriteLine(getHelp());
            }
            return exitCode;
        }
        private static String getHelp(){
            return HELP_TEXT;
        }
    }
}
