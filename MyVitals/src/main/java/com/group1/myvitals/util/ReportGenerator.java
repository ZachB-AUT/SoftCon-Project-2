package com.group1.myvitals.util;

import com.group1.myvitals.model.DB_DataInterface;
import io.github.fatihcatalkaya.javatypst.JavaTypst;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ReportGenerator {

    private static final String PREAMBLE = """
        #let userName  = [%s]
        #let userDOB   = [%s]
        #let height    = [%s m]
        #let gender    = [%s]
        #let nhi       = [%s]
        #let meds      = [%s]
        #let allergies = [%s]

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
          [Name:],          table.cell(colspan: 2)[#userName],
          [Date of Birth:], table.cell(colspan: 2)[#userDOB],
        )

        #outline()
        #pagebreak()

        = Personal Data

        #table(
          columns: (1fr, 2fr),
          [*NHI Number:*], [#nhi],
          [*Height:*],     [#height],
          [*Gender:*],     [#gender],
          [*Medications:*],[#meds],
          [*Allergies:*],  [#allergies],
        )

        #set table(fill: (x, y) => if y == 0 { rgb("#f2e9e1") })
        """;

    private final DB_DataInterface db;
    private final int userId;

    public ReportGenerator(DB_DataInterface db, int userId) {
        this.db = db;
        this.userId = userId;
    }

    private String buildTypstDocument() {
        String[] user = db.get_user(userId);
        String name = user != null ? user[1] : "Unknown";
        String dob = user != null ? user[3] : "—";
        String height = user != null ? user[4] : "—";
        String gender = user != null ? user[5] : "—";
        String nhi = user != null ? user[6] : "—";

        HashMap<String, Integer> meds = db.getMedications(userId);
        HashSet<String> allrgs = db.getAllergies(userId);

        String medsStr = meds.isEmpty()
            ? "None"
            : meds
                  .entrySet()
                  .stream()
                  .map(e -> e.getKey() + " " + e.getValue() + " mg")
                  .reduce((a, b) -> a + ", " + b)
                  .orElse("None");
        String allergyStr = allrgs.isEmpty()
            ? "None"
            : String.join(", ", allrgs);

        StringBuilder doc = new StringBuilder();
        doc.append(
            String.format(
                PREAMBLE,
                name,
                dob,
                height,
                gender,
                nhi,
                medsStr,
                allergyStr
            )
        );

        for (String[] dt : db.getDataTypes()) {
            int typeId = Integer.parseInt(dt[0]);
            String typeName = dt[1];
            String unit = dt[2];
            doc.append(
                buildDataSection(
                    typeName,
                    unit,
                    db.getDataPoints(typeId, userId)
                )
            );
        }

        return doc.toString();
    }

    private String buildDataSection(
        String typeName,
        String unit,
        ArrayList<String[]> points
    ) {
        if (points.isEmpty()) {
            return String.format(
                """

                #pagebreak()
                = %s
                #v(2em)
                #align(center)[#text(size: 16pt)[No data recorded.]]
                """,
                typeName
            );
        }

        StringBuilder sb = new StringBuilder();
        sb.append(
            String.format(
                """

                #pagebreak()
                = %s
                #table(
                    columns: (1fr, 3fr),
                    table.header([Date], [%s (%s)]),
                """,
                typeName,
                typeName,
                unit
            )
        );

        for (String[] p : points) {
            sb.append(String.format("    [%s], [%s],\n", p[2], p[1]));
        }
        sb.append(")\n");
        return sb.toString();
    }

    /**
     * Renders the report to PDF and writes it to outputPath.
     * @return the absolute path of the written file
     */
    public String generatePdf(String outputPath) throws IOException {
        String typst = buildTypstDocument();
        byte[] pdf = JavaTypst.render(typst);
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(pdf);
        }
        return outputPath;
    }
}
