package com.careerpath.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class AptitudeDataLoader {

    public static void main(String[] args) {
        String filePath = "src/main/resources/questions.txt";
        
        String dbUrl = "jdbc:mysql://localhost:3306/careerpath_db?allowPublicKeyRetrieval=true&useSSL=false";
        String dbUser = "root";
        String dbPass = "root";

        System.out.println("==================================================");
        System.out.println("Starting Aptitude Data Loader...");
        System.out.println("Reading questions from: " + filePath);
        System.out.println("==================================================");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {

            String sql = "INSERT INTO aptitude_questions (company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            String line;
            int count = 0;

            Long companyId = null;
            String category = "";
            String questionText = "";
            String optionA = "";
            String optionB = "";
            String optionC = "";
            String optionD = "";
            String correctOption = "";
            String explanation = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.equals("---")) {
                    if (!questionText.isEmpty()) {
                        stmt.setObject(1, companyId); 
                        stmt.setString(2, category);
                        stmt.setString(3, questionText);
                        stmt.setString(4, optionA);
                        stmt.setString(5, optionB);
                        stmt.setString(6, optionC);
                        stmt.setString(7, optionD);
                        stmt.setString(8, correctOption);
                        stmt.setString(9, explanation);

                        stmt.executeUpdate();
                        count++;
                        System.out.println("Saved Question " + count + ": " + (questionText.length() > 30 ? questionText.substring(0, 30) + "..." : questionText));
                    }

                    companyId = null;
                    category = "";
                    questionText = "";
                    optionA = "";
                    optionB = "";
                    optionC = "";
                    optionD = "";
                    correctOption = "";
                    explanation = "";
                } 
                else if (line.startsWith("Q:")) {
                    questionText = line.substring(2).trim();
                } 
                else if (line.startsWith("A:")) {
                    optionA = line.substring(2).trim();
                } 
                else if (line.startsWith("B:")) {
                    optionB = line.substring(2).trim();
                } 
                else if (line.startsWith("C:")) {
                    optionC = line.substring(2).trim();
                } 
                else if (line.startsWith("D:")) {
                    optionD = line.substring(2).trim();
                } 
                else if (line.startsWith("Correct:")) {
                    correctOption = line.substring(8).trim().toUpperCase();
                } 
                else if (line.startsWith("Explanation:")) {
                    explanation = line.substring(12).trim();
                } 
                else if (line.startsWith("Company:")) {
                    String compStr = line.substring(8).trim();
                    if (!compStr.isEmpty()) {
                        companyId = Long.parseLong(compStr);
                    }
                } 
                else if (line.startsWith("Category:")) {
                    category = line.substring(9).trim().toUpperCase();
                }
            }

            if (!questionText.isEmpty()) {
                stmt.setObject(1, companyId);
                stmt.setString(2, category);
                stmt.setString(3, questionText);
                stmt.setString(4, optionA);
                stmt.setString(5, optionB);
                stmt.setString(6, optionC);
                stmt.setString(7, optionD);
                stmt.setString(8, correctOption);
                stmt.setString(9, explanation);

                stmt.executeUpdate();
                count++;
                System.out.println("Saved Question " + count + ": " + (questionText.length() > 30 ? questionText.substring(0, 30) + "..." : questionText));
            }

            System.out.println("==================================================");
            System.out.println("SUCCESS: Loaded " + count + " questions successfully into the database!");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("Error running data loader: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
