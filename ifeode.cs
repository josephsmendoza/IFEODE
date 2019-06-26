using System;
using System.Linq;
using System.IO;
using System.Security;
using Microsoft.Win32;

namespace IFEODE
{
    class Program
    {
        private static String oldProg;
        private static Boolean oldProgSet=false;
        private static String newProg;
        private static Boolean newProgSet=false;
        private static readonly String[] EXIT_MESSAGES = new String[]{"Done","Missing args","Incorrect args","Extra args","Key/Value Not Found",
        "The user does not have the permissions required to access the registry key"};
        private static readonly int EXIT_SUCCESS=0;
        private static readonly int EXIT_ARGS_MISSING=1;
        private static readonly int EXIT_ARGS_INCORRECT=2;
        private static readonly int EXIT_ARGS_EXTRA=3;
        private static readonly int EXIT_NOT_FOUND=4;
        private static readonly int EXIT_SECURITY=5;
        private static readonly String[] HELP_TEXT=new String[]{"Usage: ifeode.exe [program.exe] [\\path\\to\\debugger.exe]",
        "provide [program.exe] alone to remove an entry"};
        private static readonly String REG_IFEO= @"SOFTWARE\Microsoft\Windows NT\CurrentVersion\Image File Execution Options\";
        private static readonly String REG_DEBUGGER= "Debugger";
        private static readonly RegistryKey HKLM=Registry.LocalMachine;
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
                            return exit(EXIT_ARGS_INCORRECT);
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

            RegistryKey progKey;
            if(newProg==null){
                try{
                    progKey=HKLM.OpenSubKey(REG_IFEO+oldProg,true);
                    if(progKey==null){
                        exit(EXIT_NOT_FOUND);
                        return exit(EXIT_SUCCESS);
                    } else {
                        progKey.DeleteValue(REG_DEBUGGER);
                        return exit(EXIT_SUCCESS);
                    }
                } catch (SecurityException se){
                    return exit(EXIT_SECURITY);
                } catch (ArgumentException ae){
                    exit(EXIT_NOT_FOUND);
                    return exit(EXIT_SUCCESS);
                }
                
            }
            try{
                progKey=HKLM.CreateSubKey(REG_IFEO+oldProg,true);
                progKey.SetValue(REG_DEBUGGER,newProg);
                return exit(EXIT_SUCCESS);
            } catch (SystemException se){
                if(se is SecurityException || se is UnauthorizedAccessException){
                    return exit(EXIT_SECURITY);
                } else {
                    throw se;
                }
            } 

        }

        private static int exit(int exitCode){
            Console.WriteLine(EXIT_MESSAGES[exitCode]);
            if(exitCode > 0 && exitCode < 4){
                Console.WriteLine(getHelp());
            }
            return exitCode;
        }
        private static String getHelp(){
            String helpText="";
            foreach(String help in HELP_TEXT){
                helpText+=help+Environment.NewLine;
            }
            return helpText;
        }
    }
}
