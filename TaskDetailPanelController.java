package catasm1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TaskDetailPanelController {

    @FXML private Label lblDetailTitle;
    @FXML private Label lblDetailDescription;
    @FXML private Label lblDetailDueDate;
    @FXML private Label lblDetailCategory;
    @FXML private Label lblDetailPriority;
    @FXML private Label lblDetailStatus;

    private MainViewController mainController;
    private Task task;

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

    @FXML
    private void handleComplete() {
        mainController.markTaskAsCompleted(task);
        showAlert("Success", "Task marked as completed!");
        closeWindow();
    }

    @FXML
    private void handleEdit() {
        mainController.deleteTask(task);
        closeWindow();
    }

    @FXML
    private void handleDelete() {
        mainController.deleteTask(task);
        showAlert("Success", "Task deleted successfully!");
        closeWindow();
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