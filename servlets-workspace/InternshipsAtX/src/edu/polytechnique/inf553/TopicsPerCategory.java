package edu.polytechnique.inf553;

import java.util.List;

public class TopicsPerCategory {

    private int programId;
    private int categoryId;
    private List<Topic> topics;

    public TopicsPerCategory(int programId, int categoryId, List<Topic> topics) {
        this.topics = topics;
        this.categoryId = categoryId;
        this.programId = programId;
    }

    public String getCategoryId() {
        return String.valueOf(categoryId);
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public String getProgramId() {
        return String.valueOf(programId);
    }
}
