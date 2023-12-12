package edu.vu.streamapiapp;

import javafx.beans.InvalidationListener;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.net.URL;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML private Button loadDataButton;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button dateFilterButton;
    @FXML private Button resetAllFiltersButton;
    @FXML private ComboBox stringComboBox;
    @FXML private Button stringSortButton;
    @FXML private ComboBox numericalComboBox;
    @FXML private Button numericalSortButton;
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

    private final ObservableList<Person> initialData = FXCollections.observableArrayList();

    @Override public void initialize(URL location, ResourceBundle resources) {
        initComboBoxes();
        initTableView();
        initToggleGroups();
        initSetOnActions();



    }
    private void initSetOnActions(){
        loadDataButton.setOnAction(e -> loadCsvFiles());
        dateFilterButton.setOnAction(e -> filterByDate());
        resetAllFiltersButton.setOnAction(e -> resetAllFiltersAndSorts());
        stringSortButton.setOnAction(e -> sortByString());
        numericalSortButton.setOnAction(e -> sortNumeric());
    }
    private void initComboBoxes(){
        numericalComboBox.setItems(FXCollections.observableArrayList("ID"));
        numericalComboBox.getSelectionModel().selectFirst();
        stringComboBox.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Email", "Gender", "Country", "Domain", "Birth Date"));
        stringComboBox.getSelectionModel().selectFirst();
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
    private void loadCsvFiles() {
        FileLoadService service1 = new FileLoadService("C:\\Users\\nikit\\Desktop\\VU (Uni) stuff\\JAVA\\Projects\\StreamAPIApp\\data\\data1.csv");
        FileLoadService service2 = new FileLoadService("C:\\Users\\nikit\\Desktop\\VU (Uni) stuff\\JAVA\\Projects\\StreamAPIApp\\data\\data2.csv");
        FileLoadService service3 = new FileLoadService("C:\\Users\\nikit\\Desktop\\VU (Uni) stuff\\JAVA\\Projects\\StreamAPIApp\\data\\data3.csv");

        service1.setOnSucceeded(e -> {
            dataTableView.setItems(service1.getValue());
            service2.start();
        });

        service2.setOnSucceeded(e -> {
            dataTableView.getItems().addAll(service2.getValue());
            service3.start();
        });

        service3.setOnSucceeded(e -> {
            dataTableView.getItems().addAll(service3.getValue());
            initialData.addAll(dataTableView.getItems());
        });

        service1.start();
    }
    private void filterByDate(){
        if(dataTableView.getItems().isEmpty()){
            customError("Cannot operate while data is not set.");
            return;
        }
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            customError("Start date and end date must both be picked.");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        try {
            ObservableList<Person> filteredData = dataTableView.getItems().stream()
                    .filter(person -> {
                        LocalDate birthDate = LocalDate.parse(person.getBirthDate());
                        return !birthDate.isBefore(startDate) && !birthDate.isAfter(endDate);
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            dataTableView.setItems(filteredData);
        } catch (DateTimeParseException e) {
            customError("Birth date is not in the correct format: " + e.getMessage());
        }
    }

    private void sortByString(){
        if(dataTableView.getItems().isEmpty()){
            customError("Cannot operate while data is not set.");
            return;
        }

        String sortField = stringComboBox.getValue().toString();
        boolean ascending = aToZRadioButton.isSelected();

        ObservableList<Person> sortedData = dataTableView.getItems().stream()
                .sorted((p1, p2) -> {
                    String value1 = "", value2 = "";
                    switch (sortField) {
                        case "First Name":
                            value1 = p1.getFirstName();
                            value2 = p2.getFirstName();
                            break;
                        case "Last Name":
                            value1 = p1.getLastName();
                            value2 = p2.getLastName();
                            break;
                        case "Email":
                            value1 = p1.getEmail();
                            value2 = p2.getEmail();
                            break;
                        case "Gender":
                            value1 = p1.getGender();
                            value2 = p2.getGender();
                            break;
                        case "Country":
                            value1 = p1.getCountry();
                            value2 = p2.getCountry();
                            break;
                        case "Domain":
                            value1 = p1.getDomain();
                            value2 = p2.getDomain();
                            break;
                        case "Birth Date":
                            value1 = p1.getBirthDate();
                            value2 = p2.getBirthDate();
                            break;
                    }
                    return ascending ? value1.compareTo(value2) : value2.compareTo(value1);
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        dataTableView.setItems(sortedData);
    }
    private void sortNumeric(){
        if(dataTableView.getItems().isEmpty()){
            customError("Cannot operate while data is not set.");
            return;
        }

        String sortField = numericalComboBox.getValue().toString();
        boolean ascending = ascRadioButton.isSelected();

        ObservableList<Person> sortedData = dataTableView.getItems().stream()
                .sorted((p1, p2) -> {
                    int value1 = 0, value2 = 0;
                    switch (sortField) {
                        case "ID":
                            value1 = p1.getId();
                            value2 = p2.getId();
                            break;
                    }
                    return ascending ? Integer.compare(value1, value2) : Integer.compare(value2, value1);
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        dataTableView.setItems(sortedData);
    }

    private void customError(String errorMsg){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(errorMsg);
        errorAlert.showAndWait();
    }
    private void resetAllFiltersAndSorts(){
        if(dataTableView.getItems().isEmpty()){
            customError("Cannot operate while data is not set.");
            return;
        }
        dataTableView.getItems().setAll(initialData);
    }
}