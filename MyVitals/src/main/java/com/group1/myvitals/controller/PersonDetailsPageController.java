package com.group1.myvitals.controller;


import com.group1.myvitals.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PersonDetailsPageController {

    @FXML private TextField PersonalDetailsFirstNameTextBox;
    @FXML private TextField PersonalDetailsLastNameTextBox;
    @FXML private TextField PersonalDetailsNHITextBox;
    @FXML private TextField PersonalDetailsGenderTextBox;
    @FXML private TextField PersonalDetailsDOBTextBox;
    @FXML private TextField PersonalDetailsAgeTextBox;
    @FXML private TextField PersonalDetailsHeightTextBox;

    @FXML private Button PersonalDetailsEditFirstNameButton;
    @FXML private Button PersonalDetailsEditLastNameButton;
    @FXML private Button PersonalDetailsEditNHIButton;
    @FXML private Button PersonalDetailsEditGenderButton;
    @FXML private Button PersonalDetailsEditDOBButton;
    @FXML private Button PersonalDetailsEditAgeButton;
    @FXML private Button PersonalDetailsEditHeightTextBox;
    @FXML private Button PersonalDetailsAddMedicationButton;
    @FXML private Button PersonalDetailsEditMedicationButton;
    @FXML private Button PersonalDetailsRemoveMedicationButton;
    @FXML private Button PersonalDetailsAddAllergyButton;
    @FXML private Button PersonalDetailsRemoveAllergyButton;

    @FXML private TableView PersonalDetailsMedicationsTable;
    @FXML private TableView PersonalDetailsAllergiesTable;

}
