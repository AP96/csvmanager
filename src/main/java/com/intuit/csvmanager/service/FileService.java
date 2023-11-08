package com.intuit.csvmanager.service;

import com.intuit.csvmanager.model.Player;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileService {

    boolean isCSVFormat(MultipartFile file);

    String processFileAsync(MultipartFile file);

    Optional<Player> getPlayerById(String playerId);

    List<Player> getAllPlayers();
}
