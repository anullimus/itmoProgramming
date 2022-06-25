package commands;


import data.StudyGroup;
import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;

public class MinByIDCommand extends Command {
    private final String arg;

    public MinByIDCommand(String arg) {
        super("min_by_id");
        this.arg = arg;
    }

    @Override
    public CommandResultDto execute(
            DataManager dataManager,
            HistoryManager historyManager,
            String username
    ) {
        historyManager.addNote(this.getName());

        final StudyGroup minStudyGroup = dataManager.getMinByIdGroup();

        if (minStudyGroup == null) {
            return new CommandResultDto("Collection is empty :(", true);
        } else {
            return new CommandResultDto(minStudyGroup, true);
        }

    }
}
