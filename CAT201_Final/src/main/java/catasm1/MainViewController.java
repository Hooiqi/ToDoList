package catasm1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController {

    @FXML private MenuItem menuAddTask;
    @FXML private MenuItem menuAbout;
    @FXML private MenuItem menuExit;
    @FXML private Button btnAddNewTask;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> comboFilter;
    @FXML private Button btnSearch;
    @FXML private TableView<Task> tableViewTasks;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDescription;
    @FXML private TableColumn<Task, String> colDueDate;
    @FXML private TableColumn<Task, String> colCategory;
    @FXML private TableColumn<Task, String> colPriority;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private Label lblTotalTasks;
    @FXML private Label lblCompleted;
    @FXML private Label lblPending;

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private ObservableList<Task> filteredTaskList = FXCollections.observableArrayList();
    private static final String DATA_FILE = "tasks.json";

    public void initialize() {
        setupTableColumns();
        setupFilterComboBox();
        loadTasksFromFile();
        updateStats();

        menuAddTask.setOnAction(e -> openAddTaskDialog());
        menuAbout.setOnAction(e-> openAbout());
        menuExit.setOnAction(e -> System.exit(0));
        btnAddNewTask.setOnAction(e -> openAddTaskDialog());
        btnSearch.setOnAction(e -> handleSearch());
        txtSearch.setOnAction(e -> handleSearch());
        comboFilter.setOnAction(e -> handleFilter());
        tableViewTasks.setItems(filteredTaskList);

        tableViewTasks.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableViewTasks.getSelectionModel().getSelectedItem() != null) {
                showTaskDetails(tableViewTasks.getSelectionModel().getSelectedItem());
            }
        });

        refreshFilteredList();
    }

    private void setupTableColumns() {
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        colDueDate.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
        colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        colPriority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    }

    private void setupFilterComboBox() {
        comboFilter.setItems(FXCollections.observableArrayList(
            "All Tasks",
            "Completed",
            "Pending",
            "Work",
            "Personal",
            "Study",
            "Health",
            "Shopping",
            "Other",
            "High Priority",
            "Medium Priority",
            "Low Priority"
        ));
        comboFilter.setValue("All Tasks");
    }

    private void openAbout(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("About");
            stage.showAndWait();
        } catch ( IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open About dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void openAddTaskDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTaskDialog.fxml"));
            Parent root = loader.load();
            
            AddTaskDialogController dialogController = loader.getController();
            dialogController.setMainController(this);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Task");
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Add Task dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void addTask(Task task) {
        taskList.add(task);
        saveTasksToFile();
        refreshFilteredList();
        updateStats();
        showAlert("Success", "Task added successfully!", Alert.AlertType.INFORMATION);
    }

    public void updateTask(Task oldTask, Task updatedTask) {
        int index = taskList.indexOf(oldTask);
        if (index >= 0) {
            taskList.set(index, updatedTask);
            saveTasksToFile();
            refreshFilteredList();
            updateStats();
        }
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
        saveTasksToFile();
        refreshFilteredList();
        updateStats();
    }

    public void markTaskAsCompleted(Task task) {
        task.setStatus("Completed");
        task.setCompleted(true);
        saveTasksToFile();
        updateStats();
        tableViewTasks.refresh();
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        
        if (keyword.isEmpty()) {
            handleFilter();
            return;
        }
        
        java.util.List<Task> searchResults = TaskOperations.searchByKeyword(taskList, keyword);
        
        filteredTaskList.clear();
        filteredTaskList.addAll(searchResults);
    }

    private void handleFilter() {
        String selectedFilter = comboFilter.getValue();
        
        if (selectedFilter == null || selectedFilter.equals("All Tasks")) {
            refreshFilteredList();
            return;
        }

        java.util.List<Task> filtered;
        
        switch (selectedFilter) {
            case "Completed":
                filtered = TaskOperations.filterByCompletionStatus(taskList, true);
                break;

            case "Pending":
                filtered = TaskOperations.filterByCompletionStatus(taskList, false);
                break;

            case "High Priority":
                filtered = TaskOperations.filterByPriority(taskList, "High");
                break;
            
            case "Medium Priority":
                filtered = TaskOperations.filterByPriority(taskList, "Medium");
                break;
            
            case "Low Priority":
                filtered = TaskOperations.filterByPriority(taskList, "Low");
                break;

            default:
                filtered = TaskOperations.filterByCategory(taskList, selectedFilter);
                break;
        }
        
        filteredTaskList.clear();
        filteredTaskList.addAll(filtered);
    }

    private void refreshFilteredList() {
        filteredTaskList.clear();
        filteredTaskList.addAll(taskList);
    }

    private void showTaskDetails(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskDetailPanel.fxml"));
            Parent root = loader.load();
            
            TaskDetailPanelController detailController = loader.getController();
            detailController.setMainController(this);
            detailController.setTask(task);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Task Details");
            stage.showAndWait();
            
            refreshFilteredList();
            updateStats();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Task Details: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateStats() {
        int total = taskList.size();
        long completed = taskList.stream().filter(task -> "Completed".equals(task.getStatus())).count();
        long pending = total - completed;
        
        lblTotalTasks.setText("Total Tasks: " + total);
        lblCompleted.setText("Completed: " + completed);
        lblPending.setText("Pending: " + pending);
    }

    private void saveTasksToFile() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Task task : taskList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", task.getTitle());
                jsonObject.put("description", task.getDescription());
                jsonObject.put("dueDate", task.getDueDate());
                jsonObject.put("category", task.getCategory());
                jsonObject.put("priority", task.getPriority());
                jsonObject.put("status", task.getStatus());
                jsonObject.put("isCompleted", task.isCompleted());
                jsonArray.put(jsonObject);
            }
            
            Files.write(Paths.get(DATA_FILE), jsonArray.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not save tasks: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadTasksFromFile() {
        try {
            if (Files.exists(Paths.get(DATA_FILE))) {
                String content = new String(Files.readAllBytes(Paths.get(DATA_FILE)));
                JSONArray jsonArray = new JSONArray(content);
                
                taskList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    
                    boolean isCompleted = jsonObject.optBoolean("isCompleted", 
                        "Completed".equals(jsonObject.optString("status", "Pending")));
                    
                    Task task = new Task(
                        jsonObject.getString("title"),
                        jsonObject.optString("description", ""),
                        jsonObject.getString("dueDate"),
                        jsonObject.getString("category"),
                        jsonObject.getString("priority"),
                        jsonObject.getString("status"),
                        isCompleted
                    );
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load tasks: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}