package com.careerforge.careerforge.entity;

import java.util.Arrays;
import java.util.List;

public enum AptitudeCategory {
    QUANTITATIVE("Quantitative Aptitude", Arrays.asList(
            "Percentages",
            "Profit and Loss",
            "Ratio and Proportion",
            "Averages",
            "Time and Work",
            "Time, Speed and Distance",
            "Simple and Compound Interest",
            "Number Systems",
            "Probability",
            "Permutations and Combinations"
    )),
    LOGICAL("Logical Reasoning", Arrays.asList(
            "Number Series",
            "Coding and Decoding",
            "Blood Relations",
            "Direction Sense",
            "Seating Arrangement",
            "Syllogisms",
            "Puzzles",
            "Analogies"
    )),
    VERBAL("Verbal Ability", Arrays.asList(
            "Reading Comprehension",
            "Grammar",
            "Vocabulary",
            "Sentence Correction",
            "Para Jumbles",
            "Synonyms and Antonyms"
    ));

    private final String displayName;
    private final List<String> topics;

    AptitudeCategory(String displayName, List<String> topics) {
        this.displayName = displayName;
        this.topics = topics;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getTopics() {
        return topics;
    }
}
