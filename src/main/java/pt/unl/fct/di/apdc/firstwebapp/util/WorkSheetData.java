package pt.unl.fct.di.apdc.firstwebapp.util;

public class WorkSheetData {

    public String work_ref;
    public String work_description;
    public String work_target;
    public String work_adjudication_state;

    public WorkSheetData() {}

    public WorkSheetData(String work_ref, String work_description, String work_target, String work_adjudication_state) {
        this.work_ref = work_ref;
        this.work_description = work_description;
        this.work_target = work_target;
        this.work_adjudication_state = work_adjudication_state;
    }

    private boolean nonEmptyOrBlankField(String field) {
        return field != null && !field.isBlank();
    }

    public boolean validRegistration() {
        return nonEmptyOrBlankField(work_ref) &&
                nonEmptyOrBlankField(work_description) &&
                nonEmptyOrBlankField(work_target) &&
                nonEmptyOrBlankField(work_adjudication_state);
    }
}
