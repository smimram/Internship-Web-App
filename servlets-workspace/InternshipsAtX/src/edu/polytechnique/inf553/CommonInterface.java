package edu.polytechnique.inf553;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The common interface to several classes. Avoid writing several times the same query!
 */
public class CommonInterface {
    public static Topic getTopicOfUSer(int userId, Connection con) throws SQLException {
        String query = "select i.id as id, i.title as title, p.email as email, p.name as name, i.confidential_internship as confidential_internship\n" +
                "FROM internship i\n" +
                "INNER JOIN person p on i.supervisor_id = p.id\n" +
                "INNER JOIN person_internship pi on i.id = pi.internship_id\n" +
                "WHERE pi.person_id = ?";
        try (PreparedStatement ps0 = con.prepareStatement(query)) {
            ps0.setInt(1, userId);
            try (ResultSet rs0 = ps0.executeQuery()) {
                rs0.next();
                return new Topic(rs0.getString("title"), rs0.getInt("id"), rs0.getString("email"), rs0.getString("name"), rs0.getBoolean("confidential_internship"));
            }
        }
    }

    public static List<Program> getProgramsOfUser(int userId, Connection con) throws SQLException {
        List<Program> programs = new ArrayList<>();

        //show only programs associated to the user
        String query = "SELECT DISTINCT p.id as id, p.name as name, p.year as year, p.description as description\n" +
                "FROM program p inner join person_program pp on p.id = pp.program_id\n" +
                "WHERE pp.person_id = ?";
        try (PreparedStatement ps1 = con.prepareStatement(query)) {
            ps1.setInt(1, userId);
            try (ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    Program p = new Program(rs1.getInt("id"), rs1.getString("name"), rs1.getString("year"), rs1.getString("description"));
                    programs.add(p);
                }
            }
        }
        return programs;
    }

    public static List<TopicsPerCategory> getTopicsPerCategory(List<Program> programs, Connection con) throws SQLException {
        List<TopicsPerCategory> topicsPerCategory = new ArrayList<>();
        for (Program program : programs) {
            String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" +
                    "FROM categories c\n" +
                    "INNER JOIN program_category pc ON pc.cat_id = c.id\n" +
                    "WHERE pc.program_id = ?;";

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(program.getId()));
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int categoryId = rs.getInt("id");
                        String queryTopics = "SELECT i.id as id, i.title as title, i.confidential_internship as confidential_internship, p.email as email, p.name as name " +
                                "FROM internship i " +
                                "INNER JOIN internship_category ic ON i.id = ic.internship_id " +
                                "INNER JOIN categories c ON c.id = ic.category_id " +
                                "INNER JOIN person p on i.supervisor_id = p.id " +
                                "WHERE program_id = ? AND c.id = ? AND i.is_taken=false AND scientific_validated=true AND administr_validated=true;";

                        try (PreparedStatement stmt2 = con.prepareStatement(queryTopics)) {
                            stmt2.setInt(1, Integer.parseInt(program.getId()));
                            stmt2.setInt(2, categoryId);
                            try (ResultSet rsTopics = stmt2.executeQuery()) {
                                List<Topic> topicsOfCategory = new ArrayList<>();
                                while (rsTopics.next()) {
                                    Topic s = new Topic(rsTopics.getString("title"),
                                            rsTopics.getInt("id"),
                                            rsTopics.getString("email"),
                                            rsTopics.getString("name"),
                                            rsTopics.getBoolean("confidential_internship"));
                                    topicsOfCategory.add(s);
                                }
                                topicsPerCategory.add(new TopicsPerCategory(Integer.parseInt(program.getId()), categoryId, topicsOfCategory));
                            }
                        }

                        Category c = new Category(rs.getString("desc"), categoryId);
                        program.addCategory(c);
                    }
                }
            }
        }
        return topicsPerCategory;
    }
}
