package com.group1.myvitals.util;

import com.group1.myvitals.model.dao.VitalsDAO;
import io.github.fatihcatalkaya.javatypst.JavaTypst;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ReportGenerator {

    private static final String SETUP = """
        #let rose      = rgb("#d7827a")
        #let pine_text = rgb("#575279")

        #set text(size: 12pt, fill: pine_text)
        #set page(fill: rgb("#faf4ed"))
        #set table(stroke: pine_text)

        #table(
          columns: (1fr, 1fr, 1fr),
          table.cell(colspan: 3, align: center, inset: 1em)[
            #text(size: 20pt)[*MyVitals Health Data Report*]
          ],
          [Name:],          table.cell(colspan: 2)[%s],
          [Date of Birth:], table.cell(colspan: 2)[%s],
        )

        #outline()
        #pagebreak()

        #set table(fill: (x, y) => if y == 0 { rgb("#f2e9e1") })
        """;

    private final VitalsDAO db;
    private final int userId;

    public ReportGenerator(VitalsDAO db, int userId) {
        this.db = db;
        this.userId = userId;
    }

    private String buildTypstDocument(boolean inclPersonal, boolean inclMedications,
                                      boolean inclAllergies, Set<Integer> selectedTypeIds) {
        String[] user = db.get_user(userId);
        String name   = user != null ? user[1] : "Unknown";
        String dob    = user != null ? user[3] : "—";
        String height = user != null ? user[4] : "—";
        String gender = user != null ? user[5] : "—";
        String nhi    = user != null ? user[6] : "—";

        HashMap<String, Integer> meds  = db.getMedications(userId);
        HashSet<String>          allrgs = db.getAllergies(userId);

        StringBuilder doc = new StringBuilder();
        doc.append(String.format(SETUP, name, dob));

        if (inclPersonal) {
            doc.append(String.format("""

                #pagebreak()
                = Personal Details
                #table(
                  columns: (1fr, 2fr),
                  [*NHI Number:*], [%s],
                  [*Height:*],     [%s m],
                  [*Gender:*],     [%s],
                )
                """, nhi, height, gender));
        }

        if (inclMedications) {
            String medsStr = meds.isEmpty()
                ? "None"
                : meds.entrySet().stream()
                      .map(e -> e.getKey() + " " + e.getValue() + " mg")
                      .reduce((a, b) -> a + ", " + b)
                      .orElse("None");
            doc.append(String.format("""

                #pagebreak()
                = Medications
                #table(
                  columns: (1fr, 2fr),
                  table.header([Medication], [Dosage]),
                  %s
                )
                """, buildMedsRows(meds)));
        }

        if (inclAllergies) {
            doc.append(String.format("""

                #pagebreak()
                = Allergies
                %s
                """, allrgs.isEmpty()
                    ? "#align(center)[#text(size: 16pt)[None recorded.]]"
                    : "#list(" + allrgs.stream().map(a -> "[" + a + "]").reduce((a, b) -> a + ", " + b).orElse("") + ")"));
        }

        for (String[] dt : db.getDataTypes()) {
            int typeId = Integer.parseInt(dt[0]);
            if (!selectedTypeIds.contains(typeId)) continue;
            doc.append(buildDataSection(dt[1], dt[2], db.getDataPoints(typeId, userId)));
        }

        return doc.toString();
    }

    private String buildMedsRows(HashMap<String, Integer> meds) {
        if (meds.isEmpty()) return "[None], [],";
        StringBuilder sb = new StringBuilder();
        meds.forEach((name, dose) ->
            sb.append(String.format("  [%s], [%d mg],\n", name, dose)));
        return sb.toString();
    }

    private String buildDataSection(String typeName, String unit, ArrayList<String[]> points) {
        if (points.isEmpty()) {
            return String.format("""

                #pagebreak()
                = %s
                #v(2em)
                #align(center)[#text(size: 16pt)[No data recorded.]]
                """, typeName);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("""

            #pagebreak()
            = %s
            #table(
                columns: (1fr, 3fr),
                table.header([Date], [%s (%s)]),
            """, typeName, typeName, unit));

        for (String[] p : points) {
            sb.append(String.format("    [%s], [%s],\n", p[2], p[1]));
        }
        sb.append(")\n");
        return sb.toString();
    }

    public String generatePdf(String outputPath, boolean inclPersonal, boolean inclMedications,
                               boolean inclAllergies, Set<Integer> selectedTypeIds) throws IOException {
        String typst = buildTypstDocument(inclPersonal, inclMedications, inclAllergies, selectedTypeIds);
        byte[] pdf = JavaTypst.render(typst);
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(pdf);
        }
        return outputPath;
    }
}
