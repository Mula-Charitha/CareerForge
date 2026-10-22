package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.AIChatRequestDto;
import com.careerforge.careerforge.dto.AIChatResponseDto;
import com.careerforge.careerforge.entity.AIChatMessage;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.AIChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIService {

    private final AIChatRepository aiChatRepository;

    public AIService(AIChatRepository aiChatRepository) {
        this.aiChatRepository = aiChatRepository;
    }

    @Transactional
    public AIChatResponseDto processChat(User user, AIChatRequestDto request) {
        String prompt = request.getPrompt().trim();
        String category = request.getCategory() != null ? request.getCategory() : "GENERAL";

        String aiResponse = generateCareerAdvice(prompt, category);
        List<String> followUpSuggestions = generateSuggestions(prompt, category);

        AIChatMessage message = new AIChatMessage(user, prompt, aiResponse, category);
        AIChatMessage saved = aiChatRepository.save(message);

        return new AIChatResponseDto(
                saved.getId(),
                saved.getUserPrompt(),
                saved.getAiResponse(),
                saved.getCategory(),
                followUpSuggestions,
                saved.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<AIChatResponseDto> getChatHistory(User user) {
        return aiChatRepository.findTop50ByUserOrderByCreatedAtAsc(user).stream()
                .map(msg -> new AIChatResponseDto(
                        msg.getId(),
                        msg.getUserPrompt(),
                        msg.getAiResponse(),
                        msg.getCategory(),
                        Collections.emptyList(),
                        msg.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void clearHistory(User user) {
        aiChatRepository.deleteByUser(user);
    }

    private String generateCareerAdvice(String prompt, String category) {
        String lower = prompt.toLowerCase();

        if (lower.contains("tell me about yourself") || lower.contains("self introduction")) {
            return "### 🎯 Structuring 'Tell Me About Yourself'\n\n"
                    + "Follow the **Past ➔ Present ➔ Future** framework:\n\n"
                    + "1. **Present (Where you are now):** Current education, core specialization (e.g., Computer Science), and primary technical strengths (e.g., Full Stack Java, Spring Boot, React).\n"
                    + "2. **Past (What you've built/achieved):** Highlight 1-2 substantial projects or internships. Mention quantified impact (e.g., *'Architected a placement tracking portal serving 500+ active students with 99.9% uptime'*).\n"
                    + "3. **Future (Why this role/company):** Connect your passion to their mission: *'I love building resilient distributed systems, which is why I'm thrilled about the SDE opportunity at [Company].'* \n\n"
                    + "💡 **Pro Tip:** Keep it strictly between 90 to 120 seconds. Avoid reciting your resume line by line.";
        }

        if (lower.contains("star method") || lower.contains("behavioral")) {
            return "### 🌟 The STAR Method Framework for Behavioral Rounds\n\n"
                    + "Structure every behavioral response into 4 distinct pillars:\n\n"
                    + "- **S (Situation):** Set the scene concisely (15-20% time). Who, when, and what was the high-stakes context?\n"
                    + "- **T (Task):** What was the explicit challenge or objective expected of you?\n"
                    + "- **A (Action):** **(Crucial 50% time)** Describe the specific engineering or leadership actions *you* personally took. Use *'I analyzed, I implemented, I coordinated'* rather than passive 'we'.\n"
                    + "- **R (Result):** Conclude with quantifiable outcomes (20% time). E.g., *'Reduced database query latency by 42% and delivered the feature 3 days ahead of sprint deadline.'*\n\n"
                    + "📌 **Common Question Targets:** Conflict resolution, navigating ambiguous requirements, and learning from technical failures.";
        }

        if (lower.contains("resume") || lower.contains("ats") || lower.contains("cv")) {
            return "### 📄 ATS Resume Optimization & Impact Formula\n\n"
                    + "1. **The Google XYZ Formula for Bullet Points:**\n"
                    + "   *Accomplished [X] as measured by [Y], by doing [Z].*\n"
                    + "   - ❌ *Weak:* 'Built backend APIs using Spring Boot.'\n"
                    + "   - ✅ *Strong:* 'Engineered 12 RESTful microservices in Spring Boot & PostgreSQL, cutting response latency by 35% across 10,000 daily requests.'\n\n"
                    + "2. **ATS Clean Formatting Rules:**\n"
                    + "   - Single column layout; standard sans-serif font (Inter, Roboto, Arial).\n"
                    + "   - Avoid graphics, multi-column tables, or text boxes that break ATS parsers.\n"
                    + "   - Mirror exact keywords from the job description (e.g., 'Spring Boot', 'REST APIs', 'MySQL', 'Docker').";
        }

        if (lower.contains("dynamic programming") || lower.contains("dp") || lower.contains("dsa")) {
            return "### ⚡ Dynamic Programming Master Blueprint\n\n"
                    + "Master these 5 core DP patterns that cover 90% of technical interview problems:\n\n"
                    + "1. **0/1 Knapsack & Unbounded Knapsack:** Subset Sum, Partition Equal Subset, Coin Change.\n"
                    + "2. **Longest Common Subsequence (LCS):** Edit Distance, Shortest Common Supersequence, Longest Palindromic Subsequence.\n"
                    + "3. **Longest Increasing Subsequence (LIS):** Russian Doll Envelopes, Building Bridges (O(N log N) patience sorting).\n"
                    + "4. **Matrix DP / Grid Traversal:** Unique Paths, Minimum Path Sum, Cherry Pickup.\n"
                    + "5. **State Machine / Buy & Sell Stocks:** Cooldown periods and transaction fees.\n\n"
                    + "💡 **Step-by-Step Approach:** Write recursive brute force first ➔ Memoize with HashMap/Array ➔ Convert to bottom-up iterative DP to optimize space.";
        }

        if (lower.contains("google") || lower.contains("amazon") || lower.contains("microsoft") || lower.contains("tcs")) {
            return "### 🏢 Targeted Company Interview Playbook\n\n"
                    + "- **Google:** Heavy focus on algorithmic problem solving (Graphs, Trees, DP) with clean modular code and edge case handling. Coding style and time/space complexity analysis are strictly evaluated.\n"
                    + "- **Amazon:** 50% Technical + 50% Leadership Principles (LP). Prepare 2 detailed STAR stories for each LP (Customer Obsession, Ownership, Bias for Action, Dive Deep).\n"
                    + "- **Microsoft:** Mix of DSA (String manipulation, Linked Lists, Trees) + practical OOP system design and low-level modular design.\n"
                    + "- **TCS / Infosys (Mass Hiring):** Aptitude clearance is step 1 (Quantitative, Logical, Verbal), followed by fundamental OOP concepts (Polymorphism, Inheritance, Encapsulation), SQL queries, and final HR interview.";
        }

        if (lower.contains("ask") || lower.contains("questions to ask")) {
            return "### 🤝 Impressive Questions to Ask the Interviewer\n\n"
                    + "Never say *'I have no questions'*. Use these high-impact questions to show genuine engagement:\n\n"
                    + "1. *'What does a typical engineering sprint cycle look like for your team, from backlog grooming to production deployment?'*\n"
                    + "2. *'What is currently the most exciting or technically complex architectural challenge your team is tackling?'*\n"
                    + "3. *'How does your team foster continuous mentorship and skill growth for junior engineers?'*\n"
                    + "4. *'What distinguishes an engineer who simply performs well in this role from someone who truly excels?'*";
        }

        // Generic comprehensive placement guidance
        return "### 🚀 Strategic Placement Recommendation\n\n"
                + "To optimize your placement chances across campus and off-campus recruitment drives:\n\n"
                + "1. **DSA Foundation:** Practice 2-3 problems daily focusing on high-frequency patterns (Two Pointers, Sliding Window, BFS/DFS, Binary Search).\n"
                + "2. **System Design & Core CS:** Review OS (Processes vs Threads, Deadlocks), DBMS (Indexing, Normalization, ACID), and Computer Networks (TCP 3-way handshake, HTTP/HTTPS).\n"
                + "3. **Live Mock Rounds:** Conduct mock interview drills with timer constraints to simulate real interview pressure.\n"
                + "4. **Tailored Applications:** Customize your ATS resume for every target opening and follow up with recruiters on LinkedIn.\n\n"
                + "Feel free to ask about any specific algorithm pattern, company round, or resume feedback!";
    }

    private List<String> generateSuggestions(String prompt, String category) {
        List<String> suggestions = new ArrayList<>();
        String lower = prompt.toLowerCase();

        if (lower.contains("tell me about yourself")) {
            suggestions.add("How do I explain career gaps or non-CS background?");
            suggestions.add("Give me an example self-introduction for an SDE fresher.");
            suggestions.add("What questions should I ask at the end of the interview?");
        } else if (lower.contains("star")) {
            suggestions.add("How to answer: Tell me about a time you failed?");
            suggestions.add("How to handle conflict with a teammate?");
            suggestions.add("Amazon Leadership Principles examples");
        } else if (lower.contains("dp") || lower.contains("dsa")) {
            suggestions.add("Explain 0/1 Knapsack vs Unbounded Knapsack");
            suggestions.add("How to approach Graph BFS vs DFS problems?");
            suggestions.add("Two Pointers pattern problems list");
        } else {
            suggestions.add("How to answer 'Tell me about yourself'?");
            suggestions.add("STAR method framework for behavioral rounds");
            suggestions.add("Top Dynamic Programming patterns for SDE");
            suggestions.add("How to optimize my resume for ATS parsers?");
        }
        return suggestions;
    }
}
