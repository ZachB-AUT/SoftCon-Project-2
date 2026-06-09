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
import java.util.LinkedHashMap;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CreateReportPageController {

    @FXML private Button   CreateReportButton;
    @FXML private Label    CreateReportStatusLabel;
    @FXML private TextArea CreateReportPreviewArea;
    @FXML private CheckBox cbPersonal;
    @FXML private CheckBox cbMedications;
    @FXML private CheckBox cbAllergies;
    @FXML private VBox     dataTypeCheckboxes;

    // Maps each dynamically created checkbox to its data type ID
    private final LinkedHashMap<CheckBox, Integer> dataTypeSelections = new LinkedHashMap<>();

    @FXML
    private void initialize() {
        VitalsDAO db = Session.getInstance().getDb();
        for (String[] dt : db.getDataTypes()) {
            int typeId = Integer.parseInt(dt[0]);
            CheckBox cb = new CheckBox(dt[1]);
            cb.setSelected(true);
            cb.setTextFill(javafx.scene.paint.Color.web("#575279"));
            cb.setFont(Font.font("Aptos Mono", 13));
            dataTypeCheckboxes.getChildren().add(cb);
            dataTypeSelections.put(cb, typeId);
        }
    }

    @FXML
    private void handleGenerate() {
        int userId = Session.getInstance().getCurrentUserId();
        VitalsDAO db = Session.getInstance().getDb();

        boolean inclPersonal    = cbPersonal.isSelected();
        boolean inclMedications = cbMedications.isSelected();
        boolean inclAllergies   = cbAllergies.isSelected();
        Set<Integer> selectedTypeIds = new HashSet<>();
        dataTypeSelections.forEach((cb, id) -> { if (cb.isSelected()) selectedTypeIds.add(id); });

        String[] user = db.get_user(userId);
        String name = user != null ? user[1] : "user";
        String nhi  = (user != null && user[6] != null && !user[6].isBlank()) ? user[6] : String.valueOf(userId);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String pdfPath = "MyVitals_" + name.replaceAll("\\s+", "_") + "_" + nhi + "_" + timestamp + ".pdf";

        try {
            ReportGenerator gen = new ReportGenerator(db, userId);
            gen.generatePdf(pdfPath, inclPersonal, inclMedications, inclAllergies, selectedTypeIds);
            CreateReportPreviewArea.setText(buildTextPreview(db, userId, inclPersonal, inclMedications, inclAllergies, selectedTypeIds));
            CreateReportStatusLabel.setText("PDF saved: " + pdfPath);
        } catch (Exception e) {
            String txtPath = pdfPath.replace(".pdf", ".txt");
            String report  = buildTextPreview(db, userId, inclPersonal, inclMedications, inclAllergies, selectedTypeIds);
            CreateReportPreviewArea.setText(report);
            try (FileWriter fw = new FileWriter(txtPath)) {
                fw.write(report);
                CreateReportStatusLabel.setText("Text report saved: " + txtPath);
            } catch (IOException ioe) {
                CreateReportStatusLabel.setText("Preview shown above (could not save file).");
            }
        }
    }

    private String buildTextPreview(VitalsDAO db, int userId,
                                    boolean inclPersonal, boolean inclMedications,
                                    boolean inclAllergies, Set<Integer> selectedTypeIds) {
        StringBuilder sb = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        sb.append("=== MyVitals Health Report ===\n");
        sb.append("Generated: ").append(timestamp).append("\n\n");

        if (inclPersonal) {
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
        }

        if (inclMedications) {
            HashMap<String, Integer> meds = db.getMedications(userId);
            sb.append("--- Medications ---\n");
            if (meds.isEmpty()) {
                sb.append("None recorded.\n");
            } else {
                meds.forEach((n, d) -> sb.append("  ").append(n).append(": ").append(d).append(" mg\n"));
            }
            sb.append("\n");
        }

        if (inclAllergies) {
            HashSet<String> allergies = db.getAllergies(userId);
            sb.append("--- Allergies ---\n");
            if (allergies.isEmpty()) {
                sb.append("None recorded.\n");
            } else {
                allergies.forEach(a -> sb.append("  ").append(a).append("\n"));
            }
            sb.append("\n");
        }

        if (!selectedTypeIds.isEmpty()) {
            ArrayList<String[]> dataTypes = db.getDataTypes();
            sb.append("--- Health Data ---\n");
            for (String[] dt : dataTypes) {
                int typeId  = Integer.parseInt(dt[0]);
                if (!selectedTypeIds.contains(typeId)) continue;
                String tName = dt[1];
                String unit  = dt[2];
                ArrayList<String[]> points = db.getDataPoints(typeId, userId);
                if (points.isEmpty()) continue;
                sb.append("\n").append(tName).append(" (").append(unit).append("):\n");
                for (String[] p : points) {
                    sb.append("  ").append(p[2]).append("  →  ").append(p[1]).append(" ").append(unit).append("\n");
                }
            }
        }

        return sb.toString();
    }
}
