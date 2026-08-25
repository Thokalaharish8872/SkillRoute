package com.example.ai_service.service;

import com.example.ai_service.dto.AiServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    public AiServiceResponse generateRecommendations(Set<String> skills) throws Exception {
        String prompt = """
                    You are a career advisor.
                    
                    Based on the following skills:
                    %s
                    
                    Recommend 4-5 roles
                    Return ONLY valid JSON.
                    
                    {
                      "roles": [
                        {
                          "matchScore": 95.0,
                          "growth": "High",
                          "title": "Example",
                          "salaryRange": "Example",
                          "skillsRequired": ["Example"]
                        }
                      ]
                    }
                    """.formatted(String.join(", ", skills));

        System.out.println("completed");
        String response = generateContent(prompt);
        System.out.println("generated");

        String text = extractResponse(response);

        System.out.println(text);

        return mapper.readValue(text, AiServiceResponse.class);
    }

    private String extractResponse(String response) {
        JsonNode root = mapper.readTree(response);

        if (root.has("error")) {
            String message = root.path("error").path("message").asText();
            throw new RuntimeException("Gemini API Error: " + message);
        }
        System.out.println(root);
        String text = root
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        text = text
                .replace("```json", "")
                .replace("```", "")
                .trim();
        return text;
    }

    private String generateContent(String prompt) throws Exception {

        ObjectNode root = mapper.createObjectNode();

        ArrayNode contents = root.putArray("contents");
        ObjectNode content = contents.addObject();

        ArrayNode parts = content.putArray("parts");
        parts.addObject().put("text", prompt);

        String body = mapper.writeValueAsString(root);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                                + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public AiServiceResponse generateRoadMap(String roleTitle) throws Exception {

        String prompt = """
            Create a detailed learning roadmap for %s.
    
            Return ONLY valid JSON.
            - title: should be exactly the roleTitle no extra words
            Description of max 10 words
        
                    Do not include markdown.
                                Do not include explanations.
                                Do not include thinking.
                                Do not wrap JSON in backticks.
        
                                JSON Schema:
        
                                {
                                  "roleId": 1,
                                  "title": "string",
                                  "description": "string",
                                  "totalPhases": number,
                                  "phases": [
                                    {
                                      "phaseNumber": number,
                                      "phaseName": "string",
                                      "description": "string",
                                      "modules": [
                                        {
                                          "durationWeeks": number,
                                          "title": "string",
                                          "skills": [
                                            "string"
                                          ]
                                        }
                                      ]
                                    }
                                  ]
                                }
        
                            Rules:
                            1. Generate between 5 and 15 phases.
                            2. Each phase must contain 3-8 modules.
                            3. Each module must contain 5-10 skills.
                            4. durationWeeks must be a positive integer.
                            5. phaseNumber must start from 1 and increase sequentially.
                            6. totalPhases must equal the number of phases.
                            7. Use realistic learning progression from beginner to advanced.
                            8. IDs must be unique integers.
                            9. Skills must be concise technology or concept names.
                            10. Ensure roadmap is specific to the requested role.
        
                            Example role:
                            Android Developer
        
                            Example phase names:
                            - Foundations
                            - Java & Kotlin
                            - Android Fundamentals
                            - Architecture
                            - Networking
                            - Advanced Android
                            - Deployment
        
                            Return ONLY the JSON object.
        """.formatted(roleTitle);

        String response = generateContent(prompt);
        System.out.println(response);
        String text = extractResponse(response);

        return mapper.readValue(text, AiServiceResponse.class) ;
    }

    public Object generateSkillResource(List<String> skills) throws Exception {
        String prompt = """
            Generate information for the skills: %s
            
            Return ONLY valid JSON.
            
            Rules:
            - title: should be exactly the skill name no extra words
            - description: maximum 20 words
            - generate 2 types of resources 1 is documentation and one is video resource
            - also generate 2 video resources
            - prerequisites: comma-separated list
            - No markdown.
            - No explanation.
            - Do not wrap in ```json.
            
            [
              {
                "title":"",
                "category":"",
                "difficulty":"",
                "estimatedHours":0,
                "description":"",
                "DocumentationResource":"",
                "videoResource": [
                      "https://youtube.com/...",
                      "https://youtube.com/..."
                    ],
                "certificationUrl":"",
                "prerequisites":"",
                "demandLevel":""
              }
            ]
            """.formatted(skills);

        String response = generateContent(prompt);
        System.out.println("Response Generated" + response);

        String text = extractResponse(response);
        System.out.println(response);

        return
                mapper.readValue(
                        text,
                        Object.class
                );
    }
}