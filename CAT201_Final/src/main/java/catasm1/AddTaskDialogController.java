package catasm1;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTaskDialogController {

    @FXML private TextField txtTitle;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboCategory;
    @FXML private ChoiceBox<String> choicePriority;
    @FXML private Button btnAdd;
    @FXML private Button btnCancel;

    private MainViewController mainController;

    public void initialize() {
        setupControls();
        setupEventHandlers();
    }

    private void setupControls() {
        comboCategory.getItems().addAll("Work", "Personal", "Study", "Health", "Shopping", "Other");
        choicePriority.getItems().addAll("High", "Medium", "Low");
        
        datePicker.setValue(LocalDate.now());
    }

    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddTask());
        btnCancel.setOnAction(e -> closeDialog());
    }

    public void setMainController(MainViewController mainController) {
        this.mainController = mainController;
    }

    private void handleAddTask() {
        if (validateInput()) {
            Task task = new Task(
                txtTitle.getText(),
                txtDescription.getText(),
                datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                comboCategory.getValue(),
                choicePriority.getValue(),
                "Pending", false
            );
            
            mainController.addTask(task);
            closeDialog();
        }
    }

    private boolean validateInput() {
        if (txtTitle.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Title is required");
            return false;
        }
        if (comboCategory.getValue() == null) {
            showAlert("Validation Error", "Category is required");
            return false;
        }
        if (choicePriority.getValue() == null) {
            showAlert("Validation Error", "Priority is required");
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Validation Error", "Due date is required");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}