package com.group1.myvitals.controller;

import com.group1.myvitals.model.dao.VitalsDAO;
import com.group1.myvitals.model.Session;
import com.group1.myvitals.util.ReportGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CreateReportPageController {

    @FXML private Button   CreateReportButton;
    @FXML private Label    CreateReportStatusLabel;
    @FXML private TextArea CreateReportPreviewArea;

    @FXML
    private void handleGenerate() {
        int userId = Session.getInstance().getCurrentUserId();
        VitalsDAO db = Session.getInstance().getDb();

        String[] user = db.get_user(userId);
        String name = user != null ? user[1] : "user";
        String nhi  = (user != null && user[6] != null && !user[6].isBlank()) ? user[6] : String.valueOf(userId);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String pdfPath = "MyVitals_" + name.replaceAll("\\s+", "_") + "_" + nhi + "_" + timestamp + ".pdf";

        // Attempt PDF generation via Typst
        try {
            ReportGenerator gen = new ReportGenerator(db, userId);
            gen.generatePdf(pdfPath);
            CreateReportPreviewArea.setText(buildTextPreview(db, userId));
            CreateReportStatusLabel.setText("PDF saved: " + pdfPath);
        } catch (Exception e) {
            // Typst unavailable at runtime — fall back to plain text
            String txtPath = pdfPath.replace(".pdf", ".txt");
            String report  = buildTextPreview(db, userId);
            CreateReportPreviewArea.setText(report);
            try (FileWriter fw = new FileWriter(txtPath)) {
                fw.write(report);
                CreateReportStatusLabel.setText("Text report saved: " + txtPath);
            } catch (IOException ioe) {
                CreateReportStatusLabel.setText("Preview shown above (could not save file).");
            }
        }
    }

    private String buildTextPreview(VitalsDAO db, int userId) {
        StringBuilder sb = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        sb.append("=== MyVitals Health Report ===\n");
        sb.append("Generated: ").append(timestamp).append("\n\n");

        String[] user = db.get_user(userId);
        if (user != null) {
            sb.append("--- Personal Details ---\n");
            sb.append("Name:   ").append(user[1]).append("\n");
            sb.append("Age:    ").append(user[2]).append("\n");
            sb.append("DOB:    ").append(user[3]).append("\n");
            sb.append("Height: ").append(user[4]).append(" m\n");
            sb.append("Gender: ").append(user[5]).append("\n");
            sb.append("NHI:    ").append(user[6]).append("\n\n");
        }

        HashMap<String, Integer> meds = db.getMedications(userId);
        sb.append("--- Medications ---\n");
        if (meds.isEmpty()) {
            sb.append("None recorded.\n");
        } else {
            meds.forEach((n, d) -> sb.append("  ").append(n).append(": ").append(d).append(" mg\n"));
        }
        sb.append("\n");

        HashSet<String> allergies = db.getAllergies(userId);
        sb.append("--- Allergies ---\n");
        if (allergies.isEmpty()) {
            sb.append("None recorded.\n");
        } else {
            allergies.forEach(a -> sb.append("  ").append(a).append("\n"));
        }
        sb.append("\n");

        ArrayList<String[]> dataTypes = db.getDataTypes();
        sb.append("--- Health Data ---\n");
        for (String[] dt : dataTypes) {
            int typeId  = Integer.parseInt(dt[0]);
            String tName = dt[1];
            String unit  = dt[2];
            ArrayList<String[]> points = db.getDataPoints(typeId, userId);
            if (points.isEmpty()) continue;
            sb.append("\n").append(tName).append(" (").append(unit).append("):\n");
            for (String[] p : points) {
                sb.append("  ").append(p[2]).append("  →  ").append(p[1]).append(" ").append(unit).append("\n");
            }
        }

        return sb.toString();
    }
}
