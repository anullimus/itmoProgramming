package commands;


import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;

public class PrintAscendingCommand extends Command {

    public PrintAscendingCommand() {
        super("print_ascending");
    }

    @Override
    public CommandResultDto execute(
            DataManager dataManager,
            HistoryManager historyManager,
            String username
    ) {
        historyManager.addNote(this.getName());

        return new CommandResultDto(dataManager.ascendingDataToString(), true);
    }
}
