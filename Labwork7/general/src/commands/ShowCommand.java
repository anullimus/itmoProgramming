package commands;

import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;


public class ShowCommand extends Command {


    public ShowCommand() {
        super("show");
    }

    @Override
    public CommandResultDto execute(
            DataManager dataManager,
            HistoryManager historyManager,
            String username
    ) {
        historyManager.addNote(this.getName());

        return new CommandResultDto(dataManager.showSortedByName(), true);
    }
}
