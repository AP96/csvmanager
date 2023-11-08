package com.intuit.csvmanager.util;

import com.intuit.csvmanager.model.Player;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CSVUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_FORMATTER_ALTERNATIVE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Player> parseCsvToPlayers(InputStream inputStream) throws IOException {
        List<Player> players = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        try (fileReader; CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder()
                .setHeader()
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build())) {
            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord record : csvRecords) {
                Player player = parseCsvRecordToPlayer(record);
                players.add(player);
            }
        }
        return players;
    }

    private Player parseCsvRecordToPlayer(CSVRecord record) {
        Player player = new Player();
        player.setPlayerId(record.get("playerID"));
        player.setBirthYear(parseInteger(record.get("birthYear")).orElse(null));
        player.setBirthMonth(parseInteger(record.get("birthMonth")).orElse(null));
        player.setBirthDay(parseInteger(record.get("birthDay")).orElse(null));
        player.setBirthCountry(record.get("birthCountry"));
        player.setBirthState(record.get("birthState"));
        player.setBirthCity(record.get("birthCity"));
        player.setDeathYear(parseInteger(record.get("deathYear")).orElse(null));
        player.setDeathMonth(parseInteger(record.get("deathMonth")).orElse(null));
        player.setDeathDay(parseInteger(record.get("deathDay")).orElse(null));
        player.setDeathCountry(record.get("deathCountry"));
        player.setDeathState(record.get("deathState"));
        player.setDeathCity(record.get("deathCity"));
        player.setNameFirst(record.get("nameFirst"));
        player.setNameLast(record.get("nameLast"));
        player.setNameGiven(record.get("nameGiven"));
        player.setWeight(parseInteger(record.get("weight")).orElse(null));
        player.setHeight(parseInteger(record.get("height")).orElse(null));
        player.setBats(record.get("bats"));
        player.setThrows_(record.get("throws"));
        player.setDebut(parseLocalDate(record.get("debut")).orElse(null));
        player.setFinalGame(parseLocalDate(record.get("finalGame")).orElse(null));
        player.setRetroId(record.get("retroID"));
        player.setBbrefId(record.get("bbrefID"));
        return player;
    }

    private Optional<Integer> parseInteger(String value) {
        try {
            return Optional.ofNullable(value).map(Integer::valueOf);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<LocalDate> parseLocalDate(String value) {
        try {
            return Optional.ofNullable(value).map(v -> LocalDate.parse(v, DATE_FORMATTER));
        } catch (Exception e) {
            // Try alternative format or handle parse exception
            try {
                return Optional.of(value).map(v -> LocalDate.parse(v, DATE_FORMATTER_ALTERNATIVE));
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
    }
}

