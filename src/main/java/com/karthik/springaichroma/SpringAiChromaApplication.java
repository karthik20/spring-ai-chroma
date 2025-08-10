package com.karthik.springaichroma;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SpringAiChromaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiChromaApplication.class, args);
    }

    @Autowired
    VectorStore vectorStore;

    @Bean
    @ConditionalOnProperty(name = "app.interactive-mode", havingValue = "true", matchIfMissing = false)
    CommandLineRunner commandLineRunner() {
        var stopwatch = new StopWatch();
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("Enter your search query: ");
                    String query = scanner.nextLine();
                    List<Document> results = performSimilaritySearch(query, () -> stopwatch);
                    displayResults(results);
                    System.out.println("Performed similarity search in " + stopwatch.getTotalTimeMillis() + " ms");
                    if (stopwatch.isRunning()) {
                        stopwatch.stop();
                    }
                }
            }
        };
    }

    private void displayResults(List<Document> results) throws IllegalStateException {
        if (results == null || results.isEmpty()) {
            System.out.println("No results found for the query.");
            return;
        }
        results.forEach(System.out::println);
    }

    private List<Document> performSimilaritySearch(String query, Supplier<StopWatch> stopwatchSupplier) {
        stopwatchSupplier.get().start("Performing similarity search");
        return this.vectorStore
                .similaritySearch(SearchRequest
                        .builder()
                        .query(query)
                        .topK(3)
                        .build());
    }

    @PostConstruct
    void prePopulateData() {
        var stopwatch = new StopWatch();
        stopwatch.start("Start pre-populating data");
        var documentList = List.of(
                new Document("1", "Banking Basics - This document covers the basics of banking, including account types, interest rates, and more.", Map.of("category", "finance")),
                new Document("2", "Loan Management - Learn about different types of loans, how to manage them, and tips for repayment.", Map.of("category", "loans")),
                new Document("3", "Investment Strategies - Explore various investment strategies to grow your wealth over   time.", Map.of("category", "investments")),
                new Document("4", "Credit Scores Explained - Understand what a credit score is and how it affects your financial health.", Map.of("category", "credit")),
                new Document("5", "Savings Accounts - A guide to different types of savings accounts and how to choose the right one for you.", Map.of("category", "savings")),
                new Document("6", "Retirement Planning - Tips and strategies for planning your retirement effectively.", Map.of("category", "retirement")),
                new Document("7", "Mortgage Basics - An overview of mortgages, how they work, and what to consider when applying for one.", Map.of("category", "mortgages")),
                new Document("8", "Financial Planning - How to create a financial plan that aligns with your goals and aspirations.", Map.of("category", "planning")),
                new Document("9", "Insurance Essentials - Understanding different types of insurance and their importance in financial planning.", Map.of("category", "insurance")),
                new Document("10", "Taxation Basics - A beginner's guide to understanding taxes and how to file them correctly.", Map.of("category", "taxes")),
                new Document("11", "Budgeting 101 - Learn how to create and stick to a budget that works for you.", Map.of("category", "budgeting")),
                new Document("12", "Debt Management - Strategies for managing and reducing personal debt effectively.", Map.of("category", "debt")),
                new Document("13", "Investment Basics - An introduction to investing, including stocks, bonds, and mutual funds.", Map.of("category", "investments")),
                new Document("14", "Understanding Interest Rates - How interest rates affect your loans and savings.", Map.of("category", "interest")),
                new Document("15", "Financial Literacy - The importance of financial literacy and how to improve it.", Map.of("category", "literacy")),
                new Document("16", "Personal Finance - Tips for managing your personal finances effectively.", Map.of("category", "personal finance")),
                new Document("17", "Wealth Management - Strategies for managing and growing your wealth over time.", Map.of("category", "wealth")),
                new Document("18", "Financial Markets - An overview of financial markets, how they operate, and their impact on the economy.", Map.of("category", "markets")),
                new Document("19", "Economic Indicators - Understanding key economic indicators and their significance in financial decision-making.", Map.of("category", "economics")),
                new Document("20", "Financial Regulations - A guide to financial regulations and their impact on consumers and businesses.", Map.of("category", "regulations")),
                new Document("21", "Cryptocurrency Basics - An introduction to cryptocurrencies, how they work, and their potential impact on the financial system.", Map.of("category", "cryptocurrency")),
                new Document("22", "Financial Technology - Exploring the role of technology in transforming the financial industry.", Map.of("category", "fintech")),
                new Document("23", "Risk Management - Understanding risk management strategies in finance and investments.", Map.of("category", "risk management")),
                new Document("24", "Global Finance - An overview of global financial systems, markets, and institutions.", Map.of("category", "global finance")),
                new Document("25", "Financial Planning for Small Businesses - Tips and strategies for managing finances in small businesses.", Map.of("category", "business finance")),
                new Document("26", "Estate Planning - Understanding the importance of estate planning and how to create an effective plan.", Map.of("category", "estate planning")),
                new Document("27", "Financial Crisis Management - How to prepare for and manage financial crises.", Map.of("category", "crisis management")),
                new Document("28", "Investment Risk - Understanding different types of investment risks and how to mitigate them.", Map.of("category", "investment risk")),
                new Document("29", "Financial Ethics - The importance of ethics in finance and how to uphold ethical standards.", Map.of("category", "ethics")),
                new Document("30", "Behavioral Finance - Exploring how psychology affects financial decision-making.", Map.of("category", "behavioral finance")),
                new Document("31", "Financial Reporting - Understanding financial statements and their significance in business operations.", Map.of("category", "reporting")),
                new Document("32", "Corporate Finance - An overview of corporate finance, including capital structure, funding, and financial management.", Map.of("category", "corporate finance")),
                new Document("33", "Financial Auditing - The role of auditing in ensuring financial accuracy and compliance.", Map.of("category", "auditing")),
                new Document("34", "Financial Forecasting - Techniques for predicting future financial performance and trends.", Map.of("category", "forecasting")),
                new Document("35", "Financial Modeling - How to create financial models for decision-making and analysis.", Map.of("category", "modeling"))
        );

        vectorStore.add(documentList);
        stopwatch.stop();
        System.out.println("Pre-populated data in " + stopwatch.getTotalTimeMillis() + " ms");
    }

}
