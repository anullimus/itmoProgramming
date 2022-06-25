package commands;


import data.StudyGroup;
import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;

public class UpdateCommand extends Command implements PrivateAccessedStudyGroupCommand {
    private final String idArg;
    private final StudyGroup studyGroup;

    public UpdateCommand(StudyGroup studyGroup, String id) {
        super("update");
        this.idArg = id;
        this.studyGroup = studyGroup;
    }

    @Override
    public CommandResultDto execute(
            DataManager dataManager,
            HistoryManager historyManager,
            String username
    ) {
        historyManager.addNote(this.getName());
        int intArg;
        try {
            intArg = Integer.parseInt(idArg);
        } catch (NumberFormatException e) {
            return new CommandResultDto("Your argument was incorrect. The command was not executed.", true);
        }

        dataManager.updateStudyGroupById(intArg, studyGroup);

        return new CommandResultDto("Element was updated if it was in the table", true);
    }


    @Override
    public int getStudyGroupId() {
        try {
            return Integer.parseInt(idArg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
