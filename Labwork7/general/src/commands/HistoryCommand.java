package commands;


import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;

public class HistoryCommand extends Command {
    public HistoryCommand() {
        super("history");
    }

    @Override
    public CommandResultDto execute(
            DataManager dataManager,
            HistoryManager historyManager,
            String username
    ) {
        historyManager.addNote(this.getName());
        return new CommandResultDto(historyManager.niceToString(), true);
    }
}
