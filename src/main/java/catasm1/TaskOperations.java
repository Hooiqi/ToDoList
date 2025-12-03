package catasm1;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskOperations {
    
    //Filter tasks by category
    public static List<Task> filterByCategory(List<Task> tasks, String category) {
        List<Task> result = new ArrayList<>();
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            
            if (task.getCategory().equalsIgnoreCase(category)) {
                result.add(task);
            }
        }
        
        return result;
    }
    
    //Filter tasks by due date
    public static List<Task> filterByDueDate(List<Task> tasks, LocalDate date) {
        List<Task> result = new ArrayList<>();
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            
            if (LocalDate.parse(task.getDueDate()).equals(date)) {
                result.add(task);
            }
        }
        
        return result;
    }
    
    //Filter tasks by completion status
    public static List<Task> filterByCompletionStatus(List<Task> tasks, boolean isCompleted) {
        List<Task> result = new ArrayList<>();
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            
            if (task.isCompleted() == isCompleted) {
                result.add(task);
            }
        }
        
        return result;
    }

    //Filter tasks by status 
    public static List<Task> filterByStatus(List<Task> tasks, String status){
        List<Task> result = new ArrayList<>();

        for(int i =0; i < tasks.size(); i++){
            Task task = tasks.get(i);

            if(task.getStatus().equalsIgnoreCase(status)){
                result.add(task);
            }
        }
        
        return result;
    }

    //Filter tasks by priority
    public static List<Task> filterByPriority(List<Task> tasks, String priority) {
        List<Task> result = new ArrayList<>();
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            
            if (task.getPriority().equalsIgnoreCase(priority)) {
                result.add(task);
            }
        }
        
        return result;
    }

    //Search tasks by keyword
    public static List<Task> searchByKeyword(List<Task> tasks, String keyword) {
        List<Task> result = new ArrayList<>();
        
        String lowerKeyword = keyword.toLowerCase();
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String title = task.getTitle().toLowerCase();
            
            if (title.contains(lowerKeyword)) {
                result.add(task);
            }
        }
        
        return result;
    }

}