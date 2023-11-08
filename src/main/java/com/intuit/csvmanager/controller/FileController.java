package com.intuit.csvmanager.controller;

import com.intuit.csvmanager.exception.PlayerNotFoundException;
import com.intuit.csvmanager.messages.Response;
import com.intuit.csvmanager.model.Player;
import com.intuit.csvmanager.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Endpoint to handle file upload and saving players
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Received file: name={}, size={}, contentType={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).body("Empty file. Please select a new file to upload");
        }
        if (!fileService.isCSVFormat(file)) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new Response("Please upload new CSV file"));
        }
        try {
            // Start the asynchronous processing
            String jobId = fileService.processFileAsync(file);
            // Return a response entity with the job ID for tracking
            return ResponseEntity.accepted().body(Map.of("message", "File upload received and is being processed.", "jobId", jobId));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload the file: " + e.getMessage());
        }
    }

    // Endpoint to get all players
    @GetMapping("/all")
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = fileService.getAllPlayers();
        return ResponseEntity.ok(players); // Returns the list of all players
    }

    // Endpoint to get a single player by ID
    @GetMapping("/{playerID}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String playerID) {
        Player player = fileService.getPlayerById(playerID)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerID));
        return ResponseEntity.ok(player);
    }


}
