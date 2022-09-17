package edu.polytechnique.inf553;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Topic {

    private int id;
    private String title;
    private String supervisorEmail;
    private String supervisorName;
    private int programId;
    private boolean adminValid;
    private boolean sciValid;
    private List<Category> categories;
    private Person affiliatedStudent;
    private boolean isConfidentialInternship;
    private Timestamp dateFiche;
    private Timestamp dateReport;
    private Timestamp dateSlides;

    public Topic(String title, int id, String supervisorEmail, String supervisorName, int programId, boolean adminValid, boolean sciValid, boolean isConfidentialInternship) {
        this.title = title;
        this.id = id;
        this.supervisorEmail = supervisorEmail;
        this.supervisorName = supervisorName;
        this.adminValid = adminValid;
        this.sciValid = sciValid;
        this.programId = programId;
        this.categories = new ArrayList<>();
        this.affiliatedStudent = null;
        this.isConfidentialInternship = isConfidentialInternship;
    }

    public Topic(int id, String title, int programId, boolean adminValid, boolean sciValid, boolean isConfidentialInternship) {
        this.title = title;
        this.id = id;
        this.adminValid = adminValid;
        this.sciValid = sciValid;
        this.programId = programId;
        this.categories = new ArrayList<>();
        this.affiliatedStudent = null;
        this.isConfidentialInternship = isConfidentialInternship;
    }

    public Topic(String title, int id, String supervisorEmail, String supervisorName, boolean isConfidentialInternship) {
        this.title = title;
        this.id = id;
        this.supervisorEmail = supervisorEmail;
        this.supervisorName = supervisorName;
        this.categories = new ArrayList<>();
        this.affiliatedStudent = null;
        this.isConfidentialInternship = isConfidentialInternship;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getProgramId() {
        return String.valueOf(programId);
    }

    public boolean getAdminValid() {
        return adminValid;
    }

    public boolean getSciValid() {
        return sciValid;
    }

    public Person getAffiliatedStudent() {
        return this.affiliatedStudent;
    }

    public void setAffiliatedStudent(Person affiliatedStudent) {
        this.affiliatedStudent = affiliatedStudent;
    }

    public Timestamp getDateFiche() {
        return this.dateFiche;
    }

    public void setDateFiche(Timestamp dateFiche) {
        this.dateFiche = dateFiche;
    }

    public Timestamp getDateReport() {
        return this.dateReport;
    }

    public void setDateReport(Timestamp dateReport) {
        this.dateReport = dateReport;
    }

    public Timestamp getDateSlides() {
        return this.dateSlides;
    }

    public void setDateSlides(Timestamp dateSlides) {
        this.dateSlides = dateSlides;
    }

    public boolean isConfidentialInternship() {
        return this.isConfidentialInternship;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "Topic{" + "id=" + id + ", title='" + title + '\'' + ", dateFiche=" + dateFiche + ", dateReport=" + dateReport + ", dateSlides=" + dateSlides + '}';
    }
}
