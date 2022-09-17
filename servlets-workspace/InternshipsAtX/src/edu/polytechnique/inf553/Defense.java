package edu.polytechnique.inf553;

import java.sql.Date;
import java.sql.Time;

public class Defense {

    private int id;
    private Date date;
    private Time time;
    private Person referent;
    private Person jury2;
    private Person student;

    public Defense(int id, Date date, Time time, Person referent, Person jury2, Person student) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.referent = referent;
        this.jury2 = jury2;
        this.student = student;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Person getReferent() {
        return referent;
    }

    public void setReferent(Person referent) {
        this.referent = referent;
    }

    public Person getJury2() {
        return jury2;
    }

    public void setJury2(Person jury2) {
        this.jury2 = jury2;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Defense{" + "id=" + this.id
                + ", date=" + this.date
                + ", time=" + this.time
                + ", referent=" + this.referent
                + ", jury2=" + this.jury2
                + ", student=" + this.student
                + '}';
    }
}
