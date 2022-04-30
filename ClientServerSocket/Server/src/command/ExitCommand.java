package command;

import serverLogic.CommandManager;

public class ExitCommand implements AbstractCommand<String> {

    @Override
    public String execute() {
        System.out.println(CommandManager.PS1 + "До скорых встреч!");
        System.exit(0);
        return "ня, пока.";
    }
}
