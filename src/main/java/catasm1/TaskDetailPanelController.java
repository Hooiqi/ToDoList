package catasm1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TaskDetailPanelController {

    @FXML private Label lblDetailTitle;
    @FXML private Label lblDetailDescription;
    @FXML private Label lblDetailDueDate;
    @FXML private Label lblDetailCategory;
    @FXML private Label lblDetailPriority;
    @FXML private Label lblDetailStatus;

    @FXML private VBox viewBox;
    @FXML private VBox editBox;
    @FXML private VBox buttonBox;

    @FXML private TextField txtEditTitle;
    @FXML private TextArea txtEditDescription;
    @FXML private DatePicker dateEditPicker;
    @FXML private ComboBox<String> comboEditCategory;
    @FXML private ChoiceBox<String> choiceEditPriority;

    @FXML private Button btnComplete;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnCancelEdit;

    private MainViewController mainController;
    private Task task;
    private boolean isEditMode = false;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void initialize() {
        if (comboEditCategory != null) {
            comboEditCategory.getItems().addAll("Work", "Personal", "Study", "Health", "Shopping", "Other");
        }
        if (choiceEditPriority != null) {
            choiceEditPriority.getItems().addAll("High", "Medium", "Low");
        }
        if (editBox != null) {
            editBox.setVisible(false);
            editBox.setManaged(false);
        }
    }

    public void setMainController(MainViewController mainController) {
        this.mainController = mainController;
    }

    public void setTask(Task task) {
        this.task = task;
        updateTaskDetails();
    }

    private void updateTaskDetails() {
        lblDetailTitle.setText(task.getTitle());
        lblDetailDescription.setText(task.getDescription());
        lblDetailDueDate.setText(task.getDueDate());
        lblDetailCategory.setText(task.getCategory());
        lblDetailPriority.setText(task.getPriority());
        lblDetailStatus.setText(task.getStatus());
    }

    private void editFields() {
        if (txtEditTitle != null) txtEditTitle.setText(task.getTitle());
        if (txtEditDescription != null) txtEditDescription.setText(task.getDescription());
        
        if (dateEditPicker != null) {
            try {
                LocalDate date = LocalDate.parse(task.getDueDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dateEditPicker.setValue(date);
            } catch (Exception e) {
                dateEditPicker.setValue(LocalDate.now());
            }
        }
        
        if (comboEditCategory != null) comboEditCategory.setValue(task.getCategory());
        if (choiceEditPriority != null) choiceEditPriority.setValue(task.getPriority());
    }

    @FXML
    private void handleComplete() {
        mainController.markTaskAsCompleted(task);
        showAlert("Success", "Task marked as completed!");
        closeWindow();
    }

    @FXML
    private void handleEdit() {
        isEditMode = true;
        editFields();

        viewBox.setVisible(false);
        viewBox.setManaged(false);
        buttonBox.setVisible(false);
        buttonBox.setManaged(false);

        editBox.setVisible(true);
        editBox.setManaged(true);
    }

    @FXML
    private void handleSave() {
        if (validateEditInput()) {
            Task updatedTask = new Task(
                txtEditTitle.getText(),
                txtEditDescription.getText(),
                dateEditPicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                comboEditCategory.getValue(),
                choiceEditPriority.getValue(),
                task.getStatus(),
                task.isCompleted()
            );
            
            mainController.updateTask(task, updatedTask);
            showAlert("Success", "Task updated successfully!");
            closeWindow();
        }
    }

    @FXML
    private void handleCancelEdit() {
        isEditMode = false;
        
        viewBox.setVisible(true);
        viewBox.setManaged(true);
        buttonBox.setVisible(true);
        buttonBox.setManaged(true);
        
        editBox.setVisible(false);
        editBox.setManaged(false);
    }

    @FXML
    private void handleDelete() {
        mainController.deleteTask(task);
        showAlert("Success", "Task deleted successfully!");
        closeWindow();
    }

    private boolean validateEditInput() {
        if (txtEditTitle.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Title is required");
            return false;
        }
        if (comboEditCategory.getValue() == null) {
            showAlert("Validation Error", "Category is required");
            return false;
        }
        if (choiceEditPriority.getValue() == null) {
            showAlert("Validation Error", "Priority is required");
            return false;
        }
        if (dateEditPicker.getValue() == null) {
            showAlert("Validation Error", "Due date is required");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) lblDetailTitle.getScene().getWindow();
        stage.close();
    }
}