package com.group1.myvitals.util;

/* import io.github.fatihcatalkaya.javatypst.JavaTypst;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportGenerator {

    private static String documentPreamble = """
        #let userName = [%s]
        #let userDOB = [%s]
        #let gen_date = datetime.today().display()

        #let Height = [%s]
        #let Gender = [%s]
        #let NHI = [%s]
        #let Medications = [%s]
        #let Allergies = [%s]
        #let height = [%s]

        #let rose = rgb("#d7827a")
        #let rose_light = rgb("#ebbcba")
        #let pine = rgb("#286983");
        #let pine_light = rgb("#31748f")
        #let pine_text = rgb("#575279")
        #let pine_divider = rgb("#cecacd")

        // Configuration
        #set text(
          size: 12pt,
          fill: pine_text
        )

        #let backgroundColour = rgb("#faf4ed")

        #set page(
          fill: backgroundColour
        )

        #set table(
          stroke: pine_text
        )

        #table(
          columns: (1fr,1fr,1fr),
          table.cell(colspan: 3, align: center, inset: 1em)[
            #text(size: 20pt)[*Health Data Report*]
          ],
          [Generated:], table.cell(colspan: 2)[#gen_date],
          [Name:], table.cell(colspan: 2)[#userName],
        )

        #outline()

        #pagebreak()

        = Personal Data:

        #table(
          columns: (1fr, 2fr),
          [*NHI Number: *],[#NHI],
          [*Height: *],[#height],
          [*Gender: *],[#Gender],
          [*Medications: *],[#Medications],
          [*Allergies: *],[#Allergies],
        )

        #set table(fill: (x, y) =>
            if y == 0 {
              rgb("#f2e9e1")
            },
          )
        """;

    DataUI dataInterface;
    Person personalDetails;

    public ReportGenerator(DataUI DI, PersonUI personalDetails) {
        this.dataInterface = DI;
        this.personalDetails = personalDetails;
    }

    private String buildReportString() {
        String outputString = "";

        String name = this.personalDetails.getName();
        String nhi = this.personalDetails.getNHI();

        String dob = personalDetails.getDOBFormatted();
        String height = String.format("%.2f", this.personalDetails.getHeight());
        String gender = this.personalDetails.getGender();
        String medications = this.personalDetails.getMedications().toString();
        String allergies = this.personalDetails.getAllergies().toString();

        outputString += String.format(
            documentPreamble,
            name,
            dob,
            height,
            gender,
            nhi,
            medications,
            allergies,
            height
        );

        for (DataType dt : this.dataInterface.getDataTypes()) {
            outputString += makeTableFromDatatype(dt);
        }

        return outputString;
    }

    private String makeTableFromDatatype(DataType dt) {
        String outputString = "";

        if (dt.getNumberOfDatapoints() == 0) {
            outputString += String.format(
                """
                \n
                #pagebreak()
                \n= %s
                #v(2em)
                #align(center)[
                    #text(size: 20pt, stroke: pine_text)[
                        No available data.
                    ]
                ]
                """,
                dt.name
            );
        } else {
            outputString += String.format(
                """
                \n
                #pagebreak()
                = %s
                #table(
                    columns: (1fr, 3fr),
                    table.header([Date],[%s]),
                """,
                dt.name,
                dt.name
            );

            for (Datapoint dp : dt.getDataPoints()) {
                outputString += String.format(
                    """
                    [%s], [%s],\n
                    """,
                    dp.getDateTime(),
                    dp.getValue()
                );
            }
            outputString += "\n)";
        }

        return outputString;
    }

    public void generateReport() {
        String reportContent = this.buildReportString();

        System.out.println(reportContent);

        byte[] pdfBytes = JavaTypst.render(reportContent);

        String ReportName = String.format(
            "MyVitals_%s_%s.pdf",
            this.personalDetails.getName(),
            this.personalDetails.getNHI()
        );

        File outputFile = new File(ReportName);

        try (
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile)
        ) {
            fileOutputStream.write(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 */