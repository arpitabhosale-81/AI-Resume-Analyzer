package com.resumeai.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResumeAnalyzerService {

    public Map<String, Object> analyzeResume(
            String resumeText,
            String jobRole,
            String jobDescription) {

        String text = normalizeText(resumeText);
        String jdText = normalizeText(jobDescription);

        Map<String, List<String>> skillKeywords =
                getSkillKeywords(jobRole);

        List<String> foundSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry :
                skillKeywords.entrySet()) {

            boolean found = false;

            for (String keyword : entry.getValue()) {

                if (containsSkill(text, keyword)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                foundSkills.add(entry.getKey());
            } else {
                missingSkills.add(entry.getKey());
            }
        }

        int skillScore = 0;

        if (!skillKeywords.isEmpty()) {
            skillScore =
                    (foundSkills.size() * 50)
                            / skillKeywords.size();
        }

        int educationScore = 0;
        int projectScore = 0;
        int summaryScore = 0;
        int experienceScore = 0;
        int githubScore = 0;

        if (containsAny(
                text,
                "education",
                "bca",
                "b.tech",
                "btech",
                "mca",
                "m.tech",
                "degree")) {

            educationScore = 10;
        }

        if (containsAny(
                text,
                "project",
                "projects",
                "developed",
                "built",
                "application")) {

            projectScore = 15;
        }

        if (containsAny(
                text,
                "summary",
                "objective",
                "profile",
                "career objective")) {

            summaryScore = 10;
        }

        if (containsAny(
                text,
                "experience",
                "internship",
                "intern",
                "work experience",
                "employment")) {

            experienceScore = 10;
        }

        if (containsAny(
                text,
                "github",
                "gitlab",
                "portfolio",
                "linkedin")) {

            githubScore = 5;
        }

        int atsScore =
                skillScore
                + educationScore
                + projectScore
                + summaryScore
                + experienceScore
                + githubScore;

        if (atsScore > 100) {
            atsScore = 100;
        }

        int jobMatchScore =
                calculateJobMatchScore(
                        text,
                        jdText
                );

        List<String> matchingKeywords =
                getMatchingKeywords(
                        text,
                        jdText
                );

        List<String> missingJobKeywords =
                getMissingJobKeywords(
                        text,
                        jdText
                );

        List<String> strengths =
                generateStrengths(
                        text,
                        foundSkills
                );

        List<String> suggestions =
                generateSuggestions(
                        text,
                        missingSkills,
                        missingJobKeywords
                );

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put("jobRole", jobRole);
        result.put("atsScore", atsScore);
        result.put("jobMatchScore", jobMatchScore);
        result.put("foundSkills", foundSkills);
        result.put("missingSkills", missingSkills);
        result.put("matchingKeywords", matchingKeywords);
        result.put("missingJobKeywords", missingJobKeywords);
        result.put("strengths", strengths);
        result.put("suggestions", suggestions);

        return result;
    }

    private String normalizeText(String text) {

        if (text == null) {
            return "";
        }

        return text
                .toLowerCase()
                .replace("–", "-")
                .replace("—", "-")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean containsSkill(
            String text,
            String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return false;
        }

        return text.contains(
                keyword.toLowerCase().trim()
        );
    }

    private boolean containsAny(
            String text,
            String... keywords) {

        for (String keyword : keywords) {

            if (text.contains(
                    keyword.toLowerCase())) {

                return true;
            }
        }

        return false;
    }

    private int calculateJobMatchScore(
            String resumeText,
            String jobDescription) {

        if (jobDescription == null ||
                jobDescription.isEmpty()) {

            return 0;
        }

        Set<String> words =
                extractKeywords(jobDescription);

        if (words.isEmpty()) {
            return 0;
        }

        int matched = 0;

        for (String word : words) {

            if (resumeText.contains(word)) {
                matched++;
            }
        }

        int score =
                (matched * 100) / words.size();

        return Math.min(score, 100);
    }

    private List<String> getMatchingKeywords(
            String resumeText,
            String jobDescription) {

        List<String> matching =
                new ArrayList<>();

        if (jobDescription == null ||
                jobDescription.isEmpty()) {

            return matching;
        }

        Set<String> words =
                extractKeywords(jobDescription);

        for (String word : words) {

            if (resumeText.contains(word)) {
                matching.add(word);
            }
        }

        return matching;
    }

    private List<String> getMissingJobKeywords(
            String resumeText,
            String jobDescription) {

        List<String> missing =
                new ArrayList<>();

        if (jobDescription == null ||
                jobDescription.isEmpty()) {

            return missing;
        }

        Set<String> words =
                extractKeywords(jobDescription);

        for (String word : words) {

            if (!resumeText.contains(word)) {
                missing.add(word);
            }
        }

        return missing;
    }

    private Set<String> extractKeywords(String text) {

        Set<String> words =
                new LinkedHashSet<>();

        if (text == null || text.isEmpty()) {
            return words;
        }

        String cleaned =
                text.replaceAll(
                        "[^a-zA-Z0-9+#.]",
                        " "
                );

        String[] parts =
                cleaned.split("\\s+");

        Set<String> stopWords =
                new HashSet<>(Arrays.asList(
                        "the",
                        "and",
                        "for",
                        "with",
                        "you",
                        "your",
                        "our",
                        "are",
                        "this",
                        "that",
                        "will",
                        "from",
                        "have",
                        "has",
                        "can",
                        "must",
                        "should",
                        "into",
                        "about",
                        "their",
                        "they",
                        "them",
                        "using",
                        "used",
                        "use",
                        "work",
                        "working",
                        "role",
                        "job",
                        "team",
                        "skills",
                        "skill",
                        "years",
                        "year",
                        "looking",
                        "candidate",
                        "required",
                        "requirements",
                        "responsibilities",
                        "ability",
                        "strong",
                        "good",
                        "knowledge"
                ));

        for (String part : parts) {

            String word =
                    part.toLowerCase().trim();

            if (word.length() >= 3 &&
                    !stopWords.contains(word)) {

                words.add(word);
            }
        }

        return words;
    }

    private Map<String, List<String>>
    getSkillKeywords(String jobRole) {

        Map<String, List<String>> skills =
                new LinkedHashMap<>();

        if (jobRole == null) {
            return skills;
        }

        switch (jobRole) {

            case "Java Full Stack Developer":

                skills.put(
                        "Java",
                        Arrays.asList(
                                "java",
                                "core java",
                                "advanced java"
                        )
                );

                skills.put(
                        "Spring Boot",
                        Arrays.asList(
                                "spring boot",
                                "springboot"
                        )
                );

                skills.put(
                        "SQL",
                        Arrays.asList(
                                "sql",
                                "mysql",
                                "postgresql",
                                "oracle"
                        )
                );

                skills.put(
                        "HTML",
                        Arrays.asList(
                                "html",
                                "html5"
                        )
                );

                skills.put(
                        "CSS",
                        Arrays.asList(
                                "css",
                                "css3"
                        )
                );

                skills.put(
                        "JavaScript",
                        Arrays.asList(
                                "javascript",
                                "java script",
                                "js"
                        )
                );

                skills.put(
                        "React",
                        Arrays.asList(
                                "react",
                                "react.js",
                                "reactjs"
                        )
                );

                skills.put(
                        "Git",
                        Arrays.asList(
                                "git",
                                "github",
                                "gitlab"
                        )
                );

                break;

            case "Frontend Developer":

                skills.put(
                        "HTML",
                        Arrays.asList(
                                "html",
                                "html5"
                        )
                );

                skills.put(
                        "CSS",
                        Arrays.asList(
                                "css",
                                "css3"
                        )
                );

                skills.put(
                        "JavaScript",
                        Arrays.asList(
                                "javascript",
                                "java script",
                                "js"
                        )
                );

                skills.put(
                        "React",
                        Arrays.asList(
                                "react",
                                "react.js",
                                "reactjs"
                        )
                );

                skills.put(
                        "Git",
                        Arrays.asList(
                                "git",
                                "github",
                                "gitlab"
                        )
                );

                break;

            case "Backend Developer":

                skills.put(
                        "Java",
                        Arrays.asList(
                                "java",
                                "core java"
                        )
                );

                skills.put(
                        "Spring Boot",
                        Arrays.asList(
                                "spring boot",
                                "springboot"
                        )
                );

                skills.put(
                        "SQL",
                        Arrays.asList(
                                "sql",
                                "mysql",
                                "postgresql"
                        )
                );

                skills.put(
                        "REST API",
                        Arrays.asList(
                                "rest api",
                                "restful",
                                "rest"
                        )
                );

                skills.put(
                        "Git",
                        Arrays.asList(
                                "git",
                                "github",
                                "gitlab"
                        )
                );

                break;

            case "Software Developer":

                skills.put(
                        "Java",
                        Arrays.asList(
                                "java",
                                "core java"
                        )
                );

                skills.put(
                        "Python",
                        Arrays.asList(
                                "python"
                        )
                );

                skills.put(
                        "SQL",
                        Arrays.asList(
                                "sql",
                                "mysql",
                                "postgresql"
                        )
                );

                skills.put(
                        "Git",
                        Arrays.asList(
                                "git",
                                "github",
                                "gitlab"
                        )
                );

                skills.put(
                        "Data Structures",
                        Arrays.asList(
                                "data structures",
                                "data structure"
                        )
                );

                break;

            case "GenAI Developer":

                skills.put(
                        "Python",
                        Arrays.asList(
                                "python"
                        )
                );

                skills.put(
                        "Machine Learning",
                        Arrays.asList(
                                "machine learning",
                                "machine-learning"
                        )
                );

                skills.put(
                        "Generative AI",
                        Arrays.asList(
                                "generative ai",
                                "genai",
                                "generative artificial intelligence"
                        )
                );

                skills.put(
                        "LLM",
                        Arrays.asList(
                                "llm",
                                "large language model",
                                "large language models"
                        )
                );

                skills.put(
                        "RAG",
                        Arrays.asList(
                                "rag",
                                "retrieval augmented generation"
                        )
                );

                skills.put(
                        "Vector Database",
                        Arrays.asList(
                                "vector database",
                                "vector databases",
                                "vectordb"
                        )
                );

                break;

            case "Data Analyst":

                skills.put(
                        "Python",
                        Arrays.asList(
                                "python"
                        )
                );

                skills.put(
                        "SQL",
                        Arrays.asList(
                                "sql",
                                "mysql",
                                "postgresql"
                        )
                );

                skills.put(
                        "Excel",
                        Arrays.asList(
                                "excel",
                                "microsoft excel",
                                "ms excel"
                        )
                );

                skills.put(
                        "Power BI",
                        Arrays.asList(
                                "power bi",
                                "powerbi"
                        )
                );

                skills.put(
                        "Pandas",
                        Arrays.asList(
                                "pandas"
                        )
                );

                break;

            default:
                break;
        }

        return skills;
    }

    private List<String> generateStrengths(
            String text,
            List<String> foundSkills) {

        List<String> strengths =
                new ArrayList<>();

        if (!foundSkills.isEmpty()) {

            strengths.add(
                    "Technical skills detected: "
                            + String.join(
                            ", ",
                            foundSkills
                    )
            );
        }

        if (containsAny(
                text,
                "education",
                "bca",
                "mca",
                "degree")) {

            strengths.add(
                    "Education information is present."
            );
        }

        if (containsAny(
                text,
                "project",
                "projects",
                "developed",
                "built")) {

            strengths.add(
                    "Project information is present."
            );
        }

        if (containsAny(
                text,
                "objective",
                "summary",
                "profile")) {

            strengths.add(
                    "Professional objective or summary detected."
            );
        }

        if (containsAny(
                text,
                "problem solving",
                "problem-solving")) {

            strengths.add(
                    "Problem-solving skill detected."
            );
        }

       if (text.contains("communication")) {

            strengths.add(
                    "Communication skill detected."
            );
        }

        if (containsAny(
                text,
                "github",
                "gitlab",
                "portfolio")) {

            strengths.add(
                    "Online portfolio or code profile detected."
            );
        }

        return strengths;
    }

    // =========================================================
    // GENERATE SUGGESTIONS
    // =========================================================

    private List<String> generateSuggestions(
            String text,
            List<String> missingSkills,
            List<String> missingJobKeywords) {

        List<String> suggestions =
                new ArrayList<>();

        if (!missingSkills.isEmpty()) {

            suggestions.add(
                    "Add relevant missing technical skills "
                    + "if you have actually learned or used them."
            );

            suggestions.add(
                    "Mention specific technologies used "
                    + "in your academic or personal projects."
            );
        }

        if (!missingJobKeywords.isEmpty()) {

            suggestions.add(
                    "Review the job description and naturally "
                    + "include relevant keywords that genuinely "
                    + "match your skills and experience."
            );
        }

        if (!containsAny(
                text,
                "project",
                "projects")) {

            suggestions.add(
                    "Add 2-3 relevant projects with "
                    + "technologies and your contribution."
            );
        }

        if (!containsAny(
                text,
                "summary",
                "objective",
                "profile")) {

            suggestions.add(
                    "Add a short professional summary."
            );
        }

        if (!containsAny(
                text,
                "github",
                "gitlab",
                "portfolio")) {

            suggestions.add(
                    "Add your GitHub or portfolio profile "
                    + "if you have relevant projects."
            );
        }

        if (!containsAny(
                text,
                "experience",
                "internship",
                "intern")) {

            suggestions.add(
                    "Add internship or practical experience "
                    + "if applicable."
            );
        }

        return suggestions;
    }
}