package edu.vu.streamapiapp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Button loadDataButton;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button dateFilterButton;
    @FXML private Button resetAllFiltersButton;
    @FXML private ComboBox stringComboBox;
    @FXML private Button stringFilterButton;
    @FXML private ComboBox numericalComboBox;
    @FXML private Button numericalFilterButton;
    @FXML private TableView<Person> dataTableView;
    @FXML private TableColumn<Person, Integer> idColumn;
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;
    @FXML private TableColumn<Person, String> emailColumn;
    @FXML private TableColumn<Person, String> genderColumn;
    @FXML private TableColumn<Person, String> countryColumn;
    @FXML private TableColumn<Person, String> domainColumn;
    @FXML private TableColumn<Person, String> birthDateColumn;
    @FXML private RadioButton aToZRadioButton;
    @FXML private RadioButton zToARadioButton;
    @FXML private RadioButton ascRadioButton;
    @FXML private RadioButton descRadioButton;

    @Override public void initialize(URL location, ResourceBundle resources) {
        initComboBoxes();
        initTableView();
        initToggleGroups();
    }

    private void initComboBoxes(){
        numericalComboBox.setItems(FXCollections.observableArrayList("ID"));
        stringComboBox.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Email", "Gender", "Country", "Domain", "Birth Date"));
    }
    private void initTableView(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        domainColumn.setCellValueFactory(new PropertyValueFactory<>("domain"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
    }
    private void initToggleGroups(){
        ToggleGroup stringGroup = new ToggleGroup();
        aToZRadioButton.setToggleGroup(stringGroup);
        zToARadioButton.setToggleGroup(stringGroup);
        aToZRadioButton.setSelected(true);

        ToggleGroup numericGroup = new ToggleGroup();
        ascRadioButton.setToggleGroup(numericGroup);
        descRadioButton.setToggleGroup(numericGroup);
        ascRadioButton.setSelected(true);
    }
}