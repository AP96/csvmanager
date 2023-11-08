package com.intuit.csvmanager.service;

import com.intuit.csvmanager.model.Player;
import com.intuit.csvmanager.util.CSVUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.intuit.csvmanager.repository.PlayerRepository;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class FileServiceImpl implements FileService {
    private final PlayerRepository repository;
    private final Executor executor;
    private final CSVUtil csvUtil;

    public FileServiceImpl(PlayerRepository repository, CSVUtil csvUtil, Executor executor) {
        this.repository = repository;
        this.csvUtil = csvUtil;
        this.executor = executor;
    }

    @Override
    public boolean isCSVFormat(MultipartFile file) {
        return Objects.equals(file.getContentType(), "text/csv");
    }

    @Override
    public String processFileAsync(MultipartFile file) {
        // Generate a unique job ID
        String jobId = UUID.randomUUID().toString();

        // Run the processing in a separate thread
        CompletableFuture.runAsync(() -> {
            try {
                List<Player> players = csvUtil.parseCsvToPlayers(file.getInputStream());
                repository.saveAll(players);
            } catch (IOException e) {
                // Log the error and handle it appropriately
                throw new RuntimeException("Error processing CSV file: " + e.getMessage());
            }
        }, executor);

        // Return the job ID immediately to the client
        return jobId;
    }

    @Override
    public Optional<Player> getPlayerById(String playerId) {
        return repository.findById(playerId);
    }

    @Override
    public List<Player> getAllPlayers() {
        return repository.findAll();
    }


}
