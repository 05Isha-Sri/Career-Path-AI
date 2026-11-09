package com.careerpath.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Value("${gemini.api.key:}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostMapping("/review")
    public ResponseEntity<?> reviewCode(@RequestBody Map<String, Object> payload, HttpSession session) {
        
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Sign in required."));
        }

        String problemTitle = (String) payload.getOrDefault("problemTitle", "Coding Problem");
        String studentCode = (String) payload.getOrDefault("studentCode", "");
        String userMessage = (String) payload.getOrDefault("userMessage", "");

        if (studentCode.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("response", "Please write some code in the editor before asking the AI bot for help."));
        }

        String prompt;
        if (userMessage == null || userMessage.trim().isEmpty()) {
            
            prompt = "You are an expert Java technical interviewer checking student placement code. "
                    + "The student has written the following Java code for the problem: \"" + problemTitle + "\".\n"
                    + "Check if their code is correct or not. Point out syntax errors, compilation bugs, or logical errors. "
                    + "Give brief, clear suggestions on how to improve it, and keep it simple. "
                    + "Do NOT give the full solution immediately, encourage them to write it. Here is the student's code:\n\n"
                    + studentCode;
        } else {
            prompt = "Context: The student is working on the Java coding problem \"" + problemTitle + "\".\n"
                    + "Their current code is:\n" + studentCode + "\n\n"
                    + "The student asks: \"" + userMessage + "\"\n\n"
                    + "Respond briefly as an encouraging Java tutor. Answer their question directly based on their code.";
        }

        if (apiKey != null && !apiKey.trim().isEmpty()) {
            try {
                String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

                ObjectNode requestRoot = objectMapper.createObjectNode();
                ArrayNode contents = requestRoot.putArray("contents");
                ObjectNode contentObj = contents.addObject();
                ArrayNode parts = contentObj.putArray("parts");
                parts.addObject().put("text", prompt);

                String jsonRequest = objectMapper.writeValueAsString(requestRoot);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(geminiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonNode responseRoot = objectMapper.readTree(response.body());
                    String aiResponse = responseRoot
                            .path("candidates").get(0)
                            .path("content")
                            .path("parts").get(0)
                            .path("text").asText();

                    return ResponseEntity.ok(Collections.singletonMap("response", aiResponse));
                } else {
                    System.err.println("Gemini API returned error: " + response.statusCode() + " - " + response.body());
                }
            } catch (Exception e) {
                System.err.println("Exception calling Gemini API: " + e.getMessage());
                e.printStackTrace();
            }
        }

        String mockResponse = generateSmartMockResponse(problemTitle, studentCode, userMessage);
        return ResponseEntity.ok(Collections.singletonMap("response", mockResponse));
    }

    
    private String generateSmartMockResponse(String problemTitle, String studentCode, String userMessage) {
        boolean isFollowUp = (userMessage != null && !userMessage.trim().isEmpty());

        if (isFollowUp) {
            return "[Mock AI Chatbot Mode]\n"
                    + "I analyzed your question: \"" + userMessage + "\". Since there's no Google Gemini API Key configured in your application.properties, "
                    + "I'm running in local tutoring mode!\n\n"
                    + "Make sure your code logic correctly addresses the inputs/outputs. For example, if you are working on loops, check the boundary conditions (e.g. index < length). "
                    + "Once you set a real `gemini.api.key` in your application properties, I will give you full AI insights!";
        }

        StringBuilder feedback = new StringBuilder();
        feedback.append("**AI Code Reviewer (Local Mode)**\n");
        feedback.append("*(Note: To enable live AI intelligence, configure `gemini.api.key` in application.properties)*\n\n");

        boolean hasClass = studentCode.contains("class");
        boolean hasMethod = false;
        
        if (problemTitle.toLowerCase().contains("reverse")) {
            hasMethod = studentCode.contains("reverse");
            feedback.append("**Analysis for Reverse String:**\n");
            if (studentCode.contains("StringBuilder") && studentCode.contains("reverse")) {
                feedback.append("• Great job! You are using Java's built-in `StringBuilder.reverse()` method. This is the simplest way to solve this in an interview.\n");
            } else if (studentCode.contains("charAt") || studentCode.contains("toCharArray")) {
                feedback.append("• Excellent! You are reversing the string manually by swapping characters or iterating backwards. This shows a deep algorithmic understanding!\n");
            } else {
                feedback.append("• Suggestion: Try using a `StringBuilder` to reverse the string, or convert it to a character array and loop backwards from `length() - 1` down to 0.\n");
            }
        } 
        else if (problemTitle.toLowerCase().contains("palindrome")) {
            hasMethod = studentCode.contains("isPalindrome");
            feedback.append("**Analysis for Palindrome Check:**\n");
            if (studentCode.contains("charAt") && (studentCode.contains("while") || studentCode.contains("for"))) {
                feedback.append("• Correct approach! Using two pointers (left and right) and comparing characters is the most optimal (O(N) time, O(1) space) solution.\n");
            } else {
                feedback.append("• Tip: Keep it simple. Use two indices starting from both ends of the string. Move them inwards comparing `charAt(left) == charAt(right)`. If any character mismatches, return `false`.\n");
            }
        }
        else {
            feedback.append("**General Java Review:**\n");
            if (studentCode.contains("for") || studentCode.contains("while")) {
                feedback.append("• Good use of loops to process your data structure.\n");
            } else {
                feedback.append("• Note: Ensure your method has the correct return type and processes the inputs properly.\n");
            }
        }

        long openBraces = studentCode.chars().filter(ch -> ch == '{').count();
        long closeBraces = studentCode.chars().filter(ch -> ch == '}').count();
        if (openBraces != closeBraces) {
            feedback.append("\n**Syntax Check**: You have mismatched curly brackets! (Found " + openBraces + " `{` and " + closeBraces + " `}`). Please fix your braces.");
        } else {
            feedback.append("\n**Brace Match**: All opening and closing curly braces match perfectly.");
        }

        return feedback.toString();
    }
}
