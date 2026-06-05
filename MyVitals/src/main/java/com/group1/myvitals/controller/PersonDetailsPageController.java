package com.group1.myvitals.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PersonDetailsPageController {
    // Main Edit/Save Button
    @FXML private Button PersonalDetailsMainEditSaveButton;

    // Personal details Text Fields
    @FXML private TextField PersonalDetailsFirstNameTextBox;
    @FXML private TextField PersonalDetailsLastNameTextBox;
    @FXML private TextField PersonalDetailsNHITextBox;
    @FXML private TextField PersonalDetailsGenderTextBox;
    @FXML private TextField PersonalDetailsDOBTextBox;
    @FXML private TextField PersonalDetailsAgeTextBox;
    @FXML private TextField PersonalDetailsHeightTextBox;

    // Medication Table and Buttons
    @FXML private TableView<?> PersonalDetailsMedicationsTable;
    @FXML private Button PersonalDetailsAddMedicationButton;
    @FXML private Button PersonalDetailsEditMedicationButton;
    @FXML private Button PersonalDetailsRemoveMedicationButton;

    // Allergy Table and Buttons
    @FXML private TableView<?> PersonalDetailsAllergiesTable;
    @FXML private Button PersonalDetailsAddAllergyButton;
    @FXML private Button PersonalDetailsRemoveAllergyButton;

    // Current page state
    private boolean editMode = false;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Initialise into read only
        setMode(false);

        // Load details from database
        loadDetails();
    }

    @FXML
    public void mainEditSaveButtonPressed() {
        if (editMode) {
            // Update age incase DOB was changed
            updateAgeUsingDOB();

            // Extract user input for height
            String heightUserInput = PersonalDetailsHeightTextBox.getText().trim();

            // Remove anything which is not a number or decimal point
            String plainHeight = heightUserInput.replaceAll("[^0-9.]", "");

            if (!plainHeight.isEmpty()) {
                try {
                    double height = Double.parseDouble(plainHeight);
                    
                    // TO DO: save height as a raw double/float to database! 
                    
                    // Add m to end of height again and display in the textbox
                    PersonalDetailsHeightTextBox.setText(String.format("%.2f m", height));
                } catch (NumberFormatException e) {
                    PersonalDetailsHeightTextBox.setText("Invalid format");
                }
            }

            // Extract details from text boxes ready to save to database
            String firstNameUserInput = PersonalDetailsFirstNameTextBox.getText().trim();
            String lastNameUserInput = PersonalDetailsLastNameTextBox.getText().trim();
            String nhiUserInput = PersonalDetailsNHITextBox.getText().trim();
            String genderUserInput = PersonalDetailsGenderTextBox.getText().trim();
            String dobUserInput = PersonalDetailsDOBTextBox.getText().trim();
            String ageUserInput = PersonalDetailsAgeTextBox.getText().trim();
            

            // TO DO save variables created above to database here!!


            // Set to read mode
            setMode(false);
        } else {
            // Remove m from height ready for user to edit
            String currentHeight = PersonalDetailsHeightTextBox.getText();
            String plainHeight = currentHeight.replace(" m", "").trim();
            PersonalDetailsHeightTextBox.setText(plainHeight);

            // Set to edit mode
            setMode(true);
        }
    }

    private void setMode(boolean editMode) {
        this.editMode = editMode;

        // Set text box editability
        PersonalDetailsFirstNameTextBox.setEditable(editMode);
        PersonalDetailsLastNameTextBox.setEditable(editMode);
        PersonalDetailsNHITextBox.setEditable(editMode);
        PersonalDetailsGenderTextBox.setEditable(editMode);
        PersonalDetailsDOBTextBox.setEditable(editMode);
        PersonalDetailsAgeTextBox.setEditable(false); // Always keep as false since automatically calculating value
        PersonalDetailsHeightTextBox.setEditable(editMode);

        // Set table button availability
        PersonalDetailsAddMedicationButton.setDisable(!editMode);
        PersonalDetailsEditMedicationButton.setDisable(!editMode);
        PersonalDetailsRemoveMedicationButton.setDisable(!editMode);
        PersonalDetailsAddAllergyButton.setDisable(!editMode);
        PersonalDetailsRemoveAllergyButton.setDisable(!editMode);

        // Update main button text
        if (editMode) {
            PersonalDetailsMainEditSaveButton.setText("Save");
        } else {
            PersonalDetailsMainEditSaveButton.setText("Edit Profile");
        }
    }

    private void loadDetails(){

        // Fetch details from database 
        String firstName = "John"; // TO DO Replace with call to database!
        String lastName = "Doe"; // TO DO Replace with call to database!
        String nhi = "ABC1234"; // TO DO Replace with call to database!
        String gender = "Male"; // TO DO Replace with call to database!
        String dob = "15/08/1995"; // TO DO Replace with call to database!
        double height = 1.8; // TO DO Replace with call to database!

        // Update values in textboxes to reflect current details
        PersonalDetailsFirstNameTextBox.setText(firstName);
        PersonalDetailsLastNameTextBox.setText(lastName);
        PersonalDetailsNHITextBox.setText(nhi);
        PersonalDetailsGenderTextBox.setText(gender);
        PersonalDetailsDOBTextBox.setText(dob);
        PersonalDetailsHeightTextBox.setText(String.format("%.2f m", height));


        // Update age using automatic calculation
        updateAgeUsingDOB();
    }

    private void updateAgeUsingDOB() {
        try {
            String dobText = PersonalDetailsDOBTextBox.getText().trim();
            if (dobText != null && !dobText.trim().isEmpty()) {
                // Parse the text into a date object
                LocalDate birthDate = LocalDate.parse(dobText.trim(), dateFormatter);
                LocalDate today = LocalDate.now();
                
                // Calculate difference in years
                int age = Period.between(birthDate, today).getYears();
                
                // Display calculated age inside the age text box
                PersonalDetailsAgeTextBox.setText(String.valueOf(age));
            }
        } catch (DateTimeParseException e) {
            PersonalDetailsAgeTextBox.setText("Invalid DOB");
        }
    }
}