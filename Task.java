package catasm1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty dueDate;
    private final StringProperty category;
    private final StringProperty priority;
    private final StringProperty status;
    private final boolean isCompleted;

    public Task(String title, String description, String dueDate, String category, String priority, String status, boolean isCompleted) {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.category = new SimpleStringProperty(category);
        this.priority = new SimpleStringProperty(priority);
        this.status = new SimpleStringProperty(status);
        this.isCompleted = isCompleted;
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public StringProperty titleProperty() { return title; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public String getDueDate() { return dueDate.get(); }
    public void setDueDate(String dueDate) { this.dueDate.set(dueDate); }
    public StringProperty dueDateProperty() { return dueDate; }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }

    public String getPriority() { return priority.get(); }
    public void setPriority(String priority) { this.priority.set(priority); }
    public StringProperty priorityProperty() { return priority; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }

    public boolean isCompleted() { return isCompleted; }
}