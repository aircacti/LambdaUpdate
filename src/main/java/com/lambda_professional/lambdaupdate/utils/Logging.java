package com.lambda_professional.lambdaupdate.utils;

import com.lambda_professional.lambdaupdate.LambdaUpdate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {

    private static final String LOG_FILE_NAME = "logs.txt";

    public static void createLogFileIfNotExists() throws IOException {
        File logFile = new File(LambdaUpdate.getPlugin().getDataFolder(), LOG_FILE_NAME);

        if (!logFile.exists()) {
            if (!logFile.createNewFile()) {
                throw new IOException("Can't create log file");
            }
        }
    }

    public static void log(String message) {
        File logFile = new File(LambdaUpdate.getPlugin().getDataFolder(), LOG_FILE_NAME);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String dateString = dateFormat.format(new Date());

        LambdaUpdate.getPlugin().getLogger().info(message);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write("[" + dateString + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error writing to log file ("+message+"): " + e.getMessage());
        }
    }

}
