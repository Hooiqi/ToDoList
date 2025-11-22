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
// import javafx.scene.control.Alert;
import javafx.scene.control.Button;
// import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
// import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
// import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController {

    @FXML private Button btnAddNewTask;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> comboFilter;
    @FXML private Button btnSearch;
    @FXML private Button btnDelete;
    @FXML private Button btnMarkComplete;
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
        
        btnAddNewTask.setOnAction(e -> openAddTaskDialog());
        btnSearch.setOnAction(e -> handleSearch());
        txtSearch.setOnAction(e -> handleSearch());
        comboFilter.setOnAction(e -> handleFilter());
        
        tableViewTasks.setItems(taskList);
        tableViewTasks.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showTaskDetails(newSelection);
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

    private void openAddTaskDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTaskDialog.fxml"));
            Parent root = loader.load();
            
            AddTaskDialogController dialogController = loader.getController();
            dialogController.setMainController(this);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Task");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        taskList.add(task);
        saveTasksToFile();
        updateStats();
    }

    public void updateTask(Task oldTask, Task updatedTask) {
        int index = taskList.indexOf(oldTask);
        if (index >= 0) {
            taskList.set(index, updatedTask);
            saveTasksToFile();
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
        saveTasksToFile();
        updateStats();
        tableViewTasks.refresh();
    }

    private void showTaskDetails(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskDetailPanel.fxml"));
            Parent root = loader.load();
            
            TaskDetailPanelController detailController = loader.getController();
            detailController.setMainController(this);
            detailController.setTask(task);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Task Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        
        if (keyword.isEmpty()) {
            handleFilter();
            return;
        }
        
        java.util.List<Task> searchResults = TaskOperations.searchByKeyword(taskList, keyword);
        
        filteredTaskList.clear();
        filteredTaskList.addAll(searchResults);
        tableViewTasks.setItems(filteredTaskList);
    }

    @FXML
    private void handleFilter() {
        String selectedFilter = comboFilter.getValue();
        
        if (selectedFilter == null || selectedFilter.equals("All Tasks")) {
            refreshFilteredList();
            return;
        }

    java.util.List<Task> filtered;
        
    switch (selectedFilter){
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
        tableViewTasks.setItems(filteredTaskList);
    }

    private void refreshFilteredList() {
        filteredTaskList.clear();
        filteredTaskList.addAll(taskList);
        tableViewTasks.setItems(filteredTaskList);
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
                jsonArray.put(jsonObject);
            }
            
            Files.write(Paths.get(DATA_FILE), jsonArray.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
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
                    Task task = new Task(
                        jsonObject.getString("title"),
                        jsonObject.getString("description"),
                        jsonObject.getString("dueDate"),
                        jsonObject.getString("category"),
                        jsonObject.getString("priority"),
                        jsonObject.getString("status"),
                        jsonObject.getBoolean("isCompleted")
                    );
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}