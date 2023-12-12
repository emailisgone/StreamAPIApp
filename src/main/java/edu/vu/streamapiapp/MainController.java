package edu.vu.streamapiapp;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

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
        });

        service1.start();


        initialData.addAll(dataTableView.getItems());
    }
    private void filterByDate(){
        if(dataTableView.getItems().isEmpty()){
            customError("Cannot operate while data is not set.");
            return;
        }
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            customError("Start date and end date must both be picked.");
        }
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
        dataTableView.getItems().clear();
        dataTableView.getItems().setAll(initialData);
    }
}