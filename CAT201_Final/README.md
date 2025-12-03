# To-Do List Application

A feature-rich task management application built with Java and JavaFX, designed to help users organize and track their daily responsibilities efficiently.

## 📋 Overview

This To-Do List application provides an intuitive interface for managing tasks with comprehensive features including task creation, filtering, searching, and detailed task management. The application uses JSON for data persistence, ensuring all tasks are reliably saved and retrieved across sessions.

## ✨ Features

### Core Functionality
- **Task Management**
  - Create new tasks with detailed information
  - Edit existing tasks
  - Delete tasks
  - Mark tasks as completed
  - Double-click tasks to view detailed information

### Task Properties
Each task includes:
- Title (required)
- Description
- Due Date (required)
- Category (required): Work, Personal, Study, Health, Shopping, Other
- Priority (required): High, Medium, Low
- Status: Pending or Completed

### Search & Filter
- **Search**: Find tasks by title keywords
- **Filter by**:
  - Completion status (All, Completed, Pending)
  - Category (Work, Personal, Study, Health, Shopping, Other)
  - Priority (High, Medium, Low)

### Statistics Dashboard
Real-time tracking of:
- Total number of tasks
- Completed tasks count
- Pending tasks count

## 🎨 User Interface

The application features a modern, elegant design with:
- Purple-themed color scheme (#756AB6, #AC87C5, #E0AED0)
- Gradient headers and backgrounds
- Responsive table view for task display
- Modal dialogs for task operations
- Clean, intuitive layout

## 🛠️ Technical Stack

### Technologies
- **Java 21**: Core programming language
- **JavaFX 21/25**: UI framework
- **JSON**: Data persistence
- **Gradle/Maven**: Build tools

### Dependencies
```gradle
dependencies {
    implementation 'org.openjfx:javafx-controls:21'
    implementation 'org.openjfx:javafx-fxml:21'
    implementation 'org.json:json:20231013'
}
```

## 📁 Project Structure
```
CAT201_Final/
├── src/main/java/catasm1/
│   ├── Main.java                      # Application entry point
│   ├── MainViewController.java        # Main window controller
│   ├── Task.java                      # Task model class
│   ├── TaskOperations.java            # Task filtering/search operations
│   ├── AddTaskDialogController.java   # Add task dialog controller
│   ├── TaskDetailPanelController.java # Task details controller
│   └── AboutController.java           # About dialog controller
├── src/main/resources/catasm1/
│   ├── mainView.fxml                  # Main window layout
│   ├── AddTaskDialog.fxml             # Add task dialog layout
│   ├── TaskDetailPanel.fxml           # Task details layout
│   ├── About.fxml                     # About dialog layout
│   ├── mainView.css                   # Main window styles
│   └── AddTaskDialog.css              # Dialog styles
└── tasks.json                         # Data storage file
```

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Gradle or Maven

### Building with Gradle
```bash
./gradlew build
```

### Building with Maven
```bash
mvn clean install
```

### Running the Application

**With Gradle:**
```bash
./gradlew run
```

**With Maven:**
```bash
mvn javafx:run
```

**Direct execution:**
```bash
java -jar target/catasm1-1.0-SNAPSHOT.jar
```

## 💾 Data Persistence

Tasks are automatically saved to `tasks.json` in the application directory. The file is created automatically on first run and updated whenever tasks are added, edited, or deleted.

### JSON Structure
```json
[
  {
    "title": "Task Title",
    "description": "Task Description",
    "dueDate": "2025-11-24",
    "category": "Work",
    "priority": "High",
    "status": "Pending",
    "isCompleted": false
  }
]
```

## 📖 Usage Guide

### Adding a Task
1. Click "Add New Task" button in the header or use File → Add New Task menu
2. Fill in the required fields (Title, Category, Priority, Due Date)
3. Optionally add a description
4. Click "Add" to save

### Viewing Task Details
- Double-click any task in the table to open the detail panel
- View all task information
- Options to mark as completed, edit, or delete

### Editing a Task
1. Open task details by double-clicking
2. Click "Edit Task" button
3. Modify the fields as needed
4. Click "ADD" to save changes or "CANCEL" to discard

### Searching Tasks
1. Enter keywords in the search field
2. Click "Search" button or press Enter
3. Results will filter based on title matches

### Filtering Tasks
- Use the filter dropdown to select a category, priority, or status
- Table updates automatically to show matching tasks

## 🎯 Key Classes

### Task.java
Model class representing a task with JavaFX properties for UI binding.

### MainViewController.java
- Main application controller
- Handles task list display
- Manages search and filter operations
- Coordinates with dialog controllers
- Handles data persistence

### TaskOperations.java
Utility class providing static methods for:
- Filtering by category, priority, status, and completion
- Searching by keyword
- Date-based filtering

### AddTaskDialogController.java
- Manages the add task dialog
- Validates user input
- Creates new task objects

### TaskDetailPanelController.java
- Displays complete task information
- Switches between view and edit modes
- Handles task updates and deletion

## 🐛 Known Limitations

- No task recurrence feature
- No task attachments support
- No cloud synchronization
- Single-user application (no multi-user support)

## 📝 License

This project was developed as part of CAT201 Assignment 1.

## 👥 Contributing

GOOI HOOI QI        23304682
ONG AN YI           23304671
AKARSH SRIVASTAVA   23300053

**Version**: 1.0-SNAPSHOT  
**Java Version**: 21  
**JavaFX Version**: 21/25