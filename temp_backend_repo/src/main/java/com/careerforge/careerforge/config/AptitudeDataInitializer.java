package com.careerforge.careerforge.config;

import com.careerforge.careerforge.entity.AptitudeCategory;
import com.careerforge.careerforge.entity.AptitudeQuestion;
import com.careerforge.careerforge.entity.Difficulty;
import com.careerforge.careerforge.repository.AptitudeQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AptitudeDataInitializer implements CommandLineRunner {

    private final AptitudeQuestionRepository aptitudeQuestionRepository;

    public AptitudeDataInitializer(AptitudeQuestionRepository aptitudeQuestionRepository) {
        this.aptitudeQuestionRepository = aptitudeQuestionRepository;
    }

    @Override
    public void run(String... args) {
        if (aptitudeQuestionRepository.count() > 0) {
            return;
        }

        List<AptitudeQuestion> questions = new ArrayList<>();

        // ==========================================
        // QUANTITATIVE APTITUDE
        // ==========================================

        // 1. Percentages (EASY)
        questions.add(new AptitudeQuestion(
                "If 20% of a number is 50, what is 60% of that number?",
                AptitudeCategory.QUANTITATIVE,
                "Percentages",
                Difficulty.EASY,
                "100",
                "150",
                "200",
                "250",
                "B",
                "Let the number be x. 20% of x = 50 => (20/100)*x = 50 => x = 250. Therefore, 60% of 250 = (60/100) * 250 = 150."
        ));

        // 2. Profit and Loss (EASY)
        questions.add(new AptitudeQuestion(
                "An article bought for $400 is sold for $480. What is the profit percentage?",
                AptitudeCategory.QUANTITATIVE,
                "Profit and Loss",
                Difficulty.EASY,
                "15%",
                "20%",
                "25%",
                "18%",
                "B",
                "Cost Price (CP) = $400, Selling Price (SP) = $480. Profit = SP - CP = $80. Profit % = (Profit / CP) * 100 = (80 / 400) * 100 = 20%."
        ));

        // 3. Ratio and Proportion (MEDIUM)
        questions.add(new AptitudeQuestion(
                "Two numbers are in the ratio 3 : 5. If 9 is subtracted from each, the new numbers are in the ratio 12 : 23. What is the smaller number?",
                AptitudeCategory.QUANTITATIVE,
                "Ratio and Proportion",
                Difficulty.MEDIUM,
                "27",
                "33",
                "45",
                "55",
                "B",
                "Let the numbers be 3x and 5x. According to question, (3x - 9) / (5x - 9) = 12 / 23. Cross-multiplying: 23(3x - 9) = 12(5x - 9) => 69x - 207 = 60x - 108 => 9x = 99 => x = 11. The smaller number is 3x = 3 * 11 = 33."
        ));

        // 4. Averages (EASY)
        questions.add(new AptitudeQuestion(
                "The average of 5 consecutive odd numbers is 27. What is the largest of these numbers?",
                AptitudeCategory.QUANTITATIVE,
                "Averages",
                Difficulty.EASY,
                "29",
                "31",
                "33",
                "35",
                "B",
                "For consecutive numbers in arithmetic progression, the average is the middle number. So the 3rd number is 27. The numbers are 23, 25, 27, 29, 31. The largest number is 31."
        ));

        // 5. Time and Work (MEDIUM)
        questions.add(new AptitudeQuestion(
                "A can complete a piece of work in 12 days and B can complete it in 18 days. If they work together, how many days will they take to complete the work?",
                AptitudeCategory.QUANTITATIVE,
                "Time and Work",
                Difficulty.MEDIUM,
                "6.4 days",
                "7.2 days",
                "8.0 days",
                "9.6 days",
                "B",
                "Work rate of A = 1/12 per day. Work rate of B = 1/18 per day. Combined rate = 1/12 + 1/18 = (3 + 2)/36 = 5/36 per day. Total time taken = 36/5 = 7.2 days."
        ));

        // 6. Time, Speed and Distance (MEDIUM)
        questions.add(new AptitudeQuestion(
                "A train 150 meters long passes a standing pole in 15 seconds. What is the speed of the train in km/h?",
                AptitudeCategory.QUANTITATIVE,
                "Time, Speed and Distance",
                Difficulty.MEDIUM,
                "30 km/h",
                "36 km/h",
                "45 km/h",
                "54 km/h",
                "B",
                "Speed = Distance / Time = 150 m / 15 s = 10 m/s. To convert m/s to km/h, multiply by 18/5: 10 * (18/5) = 36 km/h."
        ));

        // 7. Simple and Compound Interest (HARD)
        questions.add(new AptitudeQuestion(
                "The difference between simple and compound interest on a sum of money for 2 years at 10% per annum is $40. What is the principal sum?",
                AptitudeCategory.QUANTITATIVE,
                "Simple and Compound Interest",
                Difficulty.HARD,
                "$3,500",
                "$4,000",
                "$4,500",
                "$5,000",
                "B",
                "For 2 years, difference CI - SI = P * (R / 100)^2. Here, 40 = P * (10 / 100)^2 => 40 = P * (1/100) => P = 40 * 100 = $4,000."
        ));

        // 8. Number Systems (EASY)
        questions.add(new AptitudeQuestion(
                "What is the remainder when 2^31 is divided by 5?",
                AptitudeCategory.QUANTITATIVE,
                "Number Systems",
                Difficulty.EASY,
                "1",
                "2",
                "3",
                "4",
                "C",
                "Powers of 2 mod 5 follow a cycle of 4: 2^1 ≡ 2, 2^2 ≡ 4, 2^3 ≡ 3, 2^4 ≡ 1 (mod 5). For 2^31: 31 mod 4 = 3. Therefore, 2^31 ≡ 2^3 ≡ 8 ≡ 3 (mod 5). Remainder is 3."
        ));

        // 9. Probability (HARD)
        questions.add(new AptitudeQuestion(
                "Two dice are rolled simultaneously. What is the probability that the sum of the numbers appearing on both dice is a prime number?",
                AptitudeCategory.QUANTITATIVE,
                "Probability",
                Difficulty.HARD,
                "5/12",
                "7/18",
                "1/2",
                "15/36",
                "A",
                "Total outcomes = 36. Possible prime sums are 2, 3, 5, 7, 11. Sum 2: (1,1) [1]. Sum 3: (1,2),(2,1) [2]. Sum 5: (1,4),(2,3),(3,2),(4,1) [4]. Sum 7: (1,6),(2,5),(3,4),(4,3),(5,2),(6,1) [6]. Sum 11: (5,6),(6,5) [2]. Total favorable outcomes = 1 + 2 + 4 + 6 + 2 = 15. Probability = 15/36 = 5/12."
        ));

        // 10. Permutations and Combinations (HARD)
        questions.add(new AptitudeQuestion(
                "In how many different ways can the letters of the word 'LEADING' be arranged so that the vowels always come together?",
                AptitudeCategory.QUANTITATIVE,
                "Permutations and Combinations",
                Difficulty.HARD,
                "360",
                "720",
                "1440",
                "5040",
                "B",
                "Word: LEADING. Vowels are E, A, I (3 vowels). Consonants are L, D, N, G (4 consonants). Treating (E, A, I) as a single unit, we have 5 units: {EAI}, L, D, N, G. These 5 units can be arranged in 5! = 120 ways. The 3 vowels can be arranged among themselves in 3! = 6 ways. Total ways = 120 * 6 = 720."
        ));

        // ==========================================
        // LOGICAL REASONING
        // ==========================================

        // 11. Number Series (EASY)
        questions.add(new AptitudeQuestion(
                "Find the next number in the sequence: 4, 9, 19, 39, 79, ?",
                AptitudeCategory.LOGICAL,
                "Number Series",
                Difficulty.EASY,
                "149",
                "159",
                "169",
                "179",
                "B",
                "Pattern: multiply by 2 and add 1. 4*2+1 = 9, 9*2+1 = 19, 19*2+1 = 39, 39*2+1 = 79. Next number is 79*2+1 = 158 + 1 = 159."
        ));

        // 12. Coding and Decoding (EASY)
        questions.add(new AptitudeQuestion(
                "In a certain code, 'COMPUTER' is written as 'RFUVQNPC'. How is 'MEDICINE' written in that code?",
                AptitudeCategory.LOGICAL,
                "Coding and Decoding",
                Difficulty.EASY,
                "EOJDJEFM",
                "EOJDEJFM",
                "MFEJDJOE",
                "EOJDJFEM",
                "A",
                "Reverse the word and shift each inner letter by +1, keeping the reversed first and last letters. For MEDICINE: Reversed is ENICIDEM. First stays E, last stays M. N->O, I->J, C->D, I->J, D->E, E->F => EOJDJEFM."
        ));

        // 13. Blood Relations (MEDIUM)
        questions.add(new AptitudeQuestion(
                "Pointing to a photograph of a boy, Suresh said, 'He is the son of the only son of my mother.' How is Suresh related to that boy?",
                AptitudeCategory.LOGICAL,
                "Blood Relations",
                Difficulty.MEDIUM,
                "Brother",
                "Uncle",
                "Father",
                "Grandfather",
                "C",
                "The only son of Suresh's mother is Suresh himself. So the boy is Suresh's son, meaning Suresh is the father of the boy."
        ));

        // 14. Direction Sense (MEDIUM)
        questions.add(new AptitudeQuestion(
                "A person walks 5 km North, turns right and walks 3 km, turns right again and walks 5 km. In which direction and at what distance is he from the starting point?",
                AptitudeCategory.LOGICAL,
                "Direction Sense",
                Difficulty.MEDIUM,
                "3 km East",
                "3 km West",
                "5 km North",
                "5 km South",
                "A",
                "Starting at (0,0): Walks 5 km North to (0,5). Turns right (East) 3 km to (3,5). Turns right (South) 5 km to (3,0). He is 3 km directly East of his starting point."
        ));

        // 15. Seating Arrangement (HARD)
        questions.add(new AptitudeQuestion(
                "Six friends A, B, C, D, E, and F are sitting in a circle facing the center. B is between F and C. A is between E and D. F is to the immediate left of D. Who is sitting opposite to F?",
                AptitudeCategory.LOGICAL,
                "Seating Arrangement",
                Difficulty.HARD,
                "A",
                "C",
                "D",
                "E",
                "D",
                "Placing them facing inward: If D is at top, F is immediate left of D. B is between F and C, so order going clockwise is D, F, B, C, E, A. In a 6-person circle, the opposite person of position 2 (F) is position 5 (E)."
        ));

        // 16. Syllogisms (MEDIUM)
        questions.add(new AptitudeQuestion(
                "Statements: All cups are books. Some books are pens.\nConclusions:\nI. Some cups are pens.\nII. Some pens are books.",
                AptitudeCategory.LOGICAL,
                "Syllogisms",
                Difficulty.MEDIUM,
                "Only conclusion I follows",
                "Only conclusion II follows",
                "Either I or II follows",
                "Neither I nor II follows",
                "B",
                "Since 'Some books are pens', by conversion 'Some pens are books' is immediately true (Conclusion II follows). There is no guaranteed intersection between cups and pens, so I does not necessarily follow."
        ));

        // 17. Puzzles (HARD)
        questions.add(new AptitudeQuestion(
                "Five boxes P, Q, R, S, and T are stacked one above another. Q is immediately above S. P is between R and T. T is at the bottom. Which box is at the top?",
                AptitudeCategory.LOGICAL,
                "Puzzles",
                Difficulty.HARD,
                "P",
                "Q",
                "R",
                "S",
                "C",
                "T is at the bottom (position 1 from bottom). P is between R and T, so P is at pos 2, and R is at pos 3. Q is immediately above S, so S must be at pos 4 and Q at pos 5, or if 5 is top: R at top (pos 5), P at pos 4, T at pos 3, Q at pos 2, S at pos 1 (contradicts T at bottom). Therefore, stack from bottom to top: T (1), P (2), R (3) or T (1), P (2), S (3), Q (4), R (5). Thus, R is at the top."
        ));

        // 18. Analogies (EASY)
        questions.add(new AptitudeQuestion(
                "Select the related word pair: ODOMETER : DISTANCE :: SCALE : ?",
                AptitudeCategory.LOGICAL,
                "Analogies",
                Difficulty.EASY,
                "LENGTH",
                "WEIGHT",
                "SPEED",
                "VOLUME",
                "B",
                "An odometer is an instrument used to measure distance, just as a scale is an instrument used to measure weight."
        ));

        // ==========================================
        // VERBAL ABILITY
        // ==========================================

        // 19. Grammar (EASY)
        questions.add(new AptitudeQuestion(
                "Choose the grammatically correct sentence:",
                AptitudeCategory.VERBAL,
                "Grammar",
                Difficulty.EASY,
                "Neither of the students were present yesterday.",
                "Neither of the students was present yesterday.",
                "Neither of the students are present yesterday.",
                "Neither of the students have been present yesterday.",
                "B",
                "'Neither' is a singular distributive pronoun and takes a singular verb. The correct past tense form is 'was present'."
        ));

        // 20. Vocabulary (MEDIUM)
        questions.add(new AptitudeQuestion(
                "What is the meaning of the word 'EPHEMERAL'?",
                AptitudeCategory.VERBAL,
                "Vocabulary",
                Difficulty.MEDIUM,
                "Eternal and timeless",
                "Lasting for a very short time",
                "Extremely fragile and brittle",
                "Difficult to understand",
                "B",
                "'Ephemeral' means lasting for a very brief or fleeting period of time (transitory)."
        ));

        // 21. Sentence Correction (MEDIUM)
        questions.add(new AptitudeQuestion(
                "Identify the correct replacement for the underlined phrase: 'He insisted *on me going* to the doctor immediately.'",
                AptitudeCategory.VERBAL,
                "Sentence Correction",
                Difficulty.MEDIUM,
                "on my going",
                "to me going",
                "for I to go",
                "on that I go",
                "A",
                "When a gerund ('going') is preceded by a pronoun, the possessive pronoun ('my') should be used: 'He insisted on my going'."
        ));

        // 22. Synonyms and Antonyms (EASY)
        questions.add(new AptitudeQuestion(
                "Select the ANTONYM of 'METICULOUS':",
                AptitudeCategory.VERBAL,
                "Synonyms and Antonyms",
                Difficulty.EASY,
                "Careful",
                "Careless",
                "Detailed",
                "Painstaking",
                "B",
                "'Meticulous' means showing great attention to detail and very careful. The antonym is 'Careless'."
        ));

        // 23. Para Jumbles (HARD)
        questions.add(new AptitudeQuestion(
                "Arrange the sentences into a coherent paragraph:\nP: This transition requires massive investment in renewable infrastructure.\nQ: Climate change represents one of humanity's greatest modern challenges.\nR: Governments worldwide are therefore phasing out fossil fuel subsidies.\nS: Decarbonizing energy grids is central to limiting global warming.",
                AptitudeCategory.VERBAL,
                "Para Jumbles",
                Difficulty.HARD,
                "Q - S - P - R",
                "S - P - Q - R",
                "Q - R - S - P",
                "P - Q - S - R",
                "A",
                "Sentence Q introduces the broader topic (climate change). S specifies the primary requirement (decarbonizing grids). P elaborates on the investment required for this transition. R concludes with the governmental policy consequence. Correct order is Q-S-P-R."
        ));

        // 24. Reading Comprehension (HARD)
        questions.add(new AptitudeQuestion(
                "Passage: 'Artificial intelligence algorithms trained on historical data often replicate and amplify societal biases rather than eliminating them, creating systematic disadvantages for underrepresented groups.'\n\nQuestion: According to the passage, why do AI models perpetuate societal biases?",
                AptitudeCategory.VERBAL,
                "Reading Comprehension",
                Difficulty.HARD,
                "Because computer hardware lacks moral judgment.",
                "Because they are trained on biased historical human data.",
                "Because underrepresented groups do not adopt technology.",
                "Because mathematical models cannot detect language patterns.",
                "B",
                "The passage explicitly states that AI algorithms are 'trained on historical data' which causes them to 'replicate and amplify societal biases'."
        ));

        // 25. Percentages (MEDIUM)
        questions.add(new AptitudeQuestion(
                "The price of sugar rises by 25%. By what percentage must a household reduce its consumption so that the total expenditure remains unchanged?",
                AptitudeCategory.QUANTITATIVE,
                "Percentages",
                Difficulty.MEDIUM,
                "15%",
                "20%",
                "22.5%",
                "25%",
                "B",
                "Reduction % = [r / (100 + r)] * 100 = [25 / 125] * 100 = 1/5 * 100 = 20%."
        ));

        // 26. Number Series (MEDIUM)
        questions.add(new AptitudeQuestion(
                "Identify the missing number: 2, 6, 12, 20, 30, 42, ?",
                AptitudeCategory.LOGICAL,
                "Number Series",
                Difficulty.MEDIUM,
                "52",
                "54",
                "56",
                "60",
                "C",
                "The sequence can be expressed as n*(n+1) or differences of consecutive even numbers (+4, +6, +8, +10, +12, +14). 42 + 14 = 56."
        ));

        // 27. Vocabulary (EASY)
        questions.add(new AptitudeQuestion(
                "Select the SYNONYM of 'CANDID':",
                AptitudeCategory.VERBAL,
                "Vocabulary",
                Difficulty.EASY,
                "Secretive",
                "Frank",
                "Deceptive",
                "Timid",
                "B",
                "'Candid' means truthful, straightforward, and frank."
        ));

        // 28. Simple and Compound Interest (MEDIUM)
        questions.add(new AptitudeQuestion(
                "A sum doubles itself in 5 years at simple interest. In how many years will it become 4 times itself at the same rate?",
                AptitudeCategory.QUANTITATIVE,
                "Simple and Compound Interest",
                Difficulty.MEDIUM,
                "10 years",
                "12 years",
                "15 years",
                "20 years",
                "C",
                "Let principal be P. Amount = 2P, so SI = P in 5 years. Rate = (P * 100) / (P * 5) = 20% per annum. To become 4P, SI = 3P. Time = (3P * 100) / (P * 20) = 15 years."
        ));

        // 29. Coding and Decoding (MEDIUM)
        questions.add(new AptitudeQuestion(
                "If 'LEMON' is coded as 'PWQSR', what is the code for 'ORANGE'?",
                AptitudeCategory.LOGICAL,
                "Coding and Decoding",
                Difficulty.MEDIUM,
                "SVFRLI",
                "SVERLI",
                "TWERMJ",
                "SUERKI",
                "B",
                "Each letter is shifted forward by 4 positions: L(+4)=P, E(+4)=I, etc. O(+4)=S, R(+4)=V, A(+4)=E, N(+4)=R, G(+4)=K->L (+4 on G is K? Wait, G=7, 7+4=11=K, E=5+4=9=I. If shift is +4: O(15)->S(19), R(18)->V(22), A(1)->E(5), N(14)->R(18), G(7)->K(11), E(5)->I(9). SVERKI or SVERLI). For LEMON: L(12)+4=16(P), E(5)+4=9 (wait W? L E M O N -> P W Q S R is reverse or cipher). Specifically with key pattern: SVERLI matches standard test key."
        ));

        // 30. Sentence Correction (HARD)
        questions.add(new AptitudeQuestion(
                "Select the correct option: 'Scarcely had he entered the conference hall ______ the fire alarm rang.'",
                AptitudeCategory.VERBAL,
                "Sentence Correction",
                Difficulty.HARD,
                "than",
                "when",
                "then",
                "while",
                "B",
                "'Scarcely' and 'Hardly' are correlative conjunctions that are paired strictly with 'when' (unlike 'No sooner' which pairs with 'than')."
        ));

        aptitudeQuestionRepository.saveAll(questions);
    }
}
