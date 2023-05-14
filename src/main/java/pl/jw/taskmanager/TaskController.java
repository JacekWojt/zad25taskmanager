package pl.jw.taskmanager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) Phase phase,
                       @RequestParam(required = false) String order) {
        List<Task> tasks;
        if (phase == Phase.DONE) {
            tasks = taskRepository.findByPhase(Phase.DONE);
        } else if (phase == Phase.TODO || phase == null) {
            tasks = taskRepository.findByPhase(Phase.TODO);
        } else {
            tasks = taskRepository.findAll();
        }

        List<Task> taskList = new ArrayList<>(tasks);

        if (order != null) {
            int asc = (order.equals("ASC")) ? 1 : -1;
            taskList.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getName().compareTo(o2.getName()) * asc;
                }
            });
        }

        model.addAttribute("taskList", taskList);
        model.addAttribute("phase", phase);
        return "home";
    }

    @GetMapping("/zadanie/{id}")
    public String showTask(@PathVariable Long id, Model model) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            model.addAttribute("task", task);
            return "task";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/zadanie/{id}/edit")
    public String showTaskEditForm(@PathVariable Long id, Model model) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            model.addAttribute("taskToEdit", task);
            return "taskEdit.html";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/zadanie/{id}/edit")
    public String editTask(@PathVariable Long id, Task task) {
        Task task1 = taskRepository.findById(id).orElseThrow();

        task1.setName(task.getName());
        task1.setDescription(task.getDescription());
        task1.setCategory(task.getCategory());
        task1.setPhase(task.getPhase());

        taskRepository.save(task1);

        return "redirect:/zadanie/" + task.getId();
    }

    @GetMapping("/dodaj")
    public String addForm(Model model) {
        model.addAttribute("task", new Task());
        return "add";
    }

    @PostMapping("/dodaj")
    public String add(Task task) {
        task.setPhase(Phase.TODO);
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/zadanie/{id}/delete")
    public String add(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}
