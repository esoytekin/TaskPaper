package com.bau.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bau.rest.entity.Category;
import com.bau.rest.entity.SubTask;
import com.bau.rest.entity.Task;
import com.bau.rest.service.TaskpaperServices;

@Component
@RequestMapping("/todos")
public class TodoRest {
	
	@Autowired
	private TaskpaperServices taskPaperServices;
	
	@RequestMapping(value="/getCategories",method=RequestMethod.GET)
	@ResponseBody
	public List<Category> getCategories(){
		List<Category> categories = taskPaperServices.getCategoriesByUser();
		return categories;
	}
	
	@RequestMapping(value="/getByCategory/{name}",method=RequestMethod.GET)
	@ResponseBody
	public List<Task> getTasksByCategory(@PathVariable String name){
		Category c = taskPaperServices.getCategoryByName(name);
		return taskPaperServices.getTasksByCategory(c);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Task addTask(@RequestParam String description,@RequestParam String categoryName){
		Task t = new Task();
		t.setDescription(description);
		Category category = taskPaperServices.getCategoryByName(categoryName);
		if(category == null){
			return null;
		}
		t.setCategory(category);
		t.setDate(new Date());
		t.setDone(false);
		t.setEnabled(true);
		t.setFavorite(false);
		taskPaperServices.saveTask(t);
		return t;
	}
	
	@RequestMapping(value="/subtask",method=RequestMethod.POST)
	@ResponseBody
	public SubTask addSubtask(@RequestParam String description, @RequestParam Long taskId){
		SubTask s = new SubTask();
		s.setDescription(description);
		Task t = taskPaperServices.getTaskById(taskId);
		if(t == null){
			return null;
		}
		s.setTask(t);
		s.setDone(false);
		s.setDate(new Date());
		s.setEnabled(true);
		
		taskPaperServices.saveSubtask(s);

		return s;
		
	}
	
	@RequestMapping(value="/subtask",method=RequestMethod.GET)
	@ResponseBody
	public List<SubTask> getSubtasksByTask(@RequestParam Long id){
		Task t = taskPaperServices.getTaskById(id);
		return taskPaperServices.getSubtasksByTask(t);
	}
	
	@RequestMapping(value="/category",method=RequestMethod.POST)
	@ResponseBody
	public Category addCategory(@RequestParam String name){
		Category c = new Category();
		c.setName(name);
		c.setDate(new Date());
		c.setEnabled(true);
		c.setUser(taskPaperServices.getUser());
		taskPaperServices.saveCategory(c);
		return c;
	}

	@RequestMapping(value="/complete",method=RequestMethod.POST)
	@ResponseBody
	public void setCompletion(@RequestParam Long id,@RequestParam boolean done){
		Task t = taskPaperServices.getTaskById(id);
		t.setDone(done);
		t.setCompleteDate(done ? new Date() : null);
		taskPaperServices.updateTask(t);
		
	}
	
	@RequestMapping(value="/subtask/complete",method=RequestMethod.POST)
	@ResponseBody
	public void setSubCompletion(@RequestParam Long id, @RequestParam boolean done){
		SubTask s = taskPaperServices.getSubtaskById(id);
		s.setDone(done);
		taskPaperServices.updateSubtask(s);
		
	}
		
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public void removeTask(@RequestParam Long id){
		if(id == null)
			return;
		Task t = taskPaperServices.getTaskById(id);
		taskPaperServices.deleteTask(t);
	}
	
	@RequestMapping(value="/taskUpdate",method=RequestMethod.POST)
	@ResponseBody
	public void updateTask(@RequestParam Long id, @RequestParam String description){
		if(id==null)
			return;
		Task t = taskPaperServices.getTaskById(id);
		t.setDescription(description);
		taskPaperServices.updateTask(t);
		
	}
	
	
	@RequestMapping(value="/subtask/delete",method=RequestMethod.POST)
	@ResponseBody
	public void removeSubTask(@RequestParam Long id){
		if(id == null)
			return;
		SubTask s = taskPaperServices.getSubtaskById(id);
		taskPaperServices.deleteSubtask(s);
	}
	
	@RequestMapping(value="/favorite",method=RequestMethod.POST)
	@ResponseBody
	public void setFavorite(@RequestParam Long id, @RequestParam boolean favorite){
		Task t = taskPaperServices.getTaskById(id);
		t.setFavorite(favorite);
		taskPaperServices.updateTask(t);
	}

	@RequestMapping(value="/subtask/favorite",method=RequestMethod.POST)
	@ResponseBody
	public void setSubtaskFavorite(@RequestParam Long id, @RequestParam boolean favorite){
		SubTask s = taskPaperServices.getSubtaskById(id);
		s.setFavorite(favorite);
		taskPaperServices.updateSubtask(s);
	}
	
	@RequestMapping(value="/note",method=RequestMethod.POST)
	@ResponseBody
	public void updateTaskNote(@RequestParam Long id, @RequestParam String note){
		
		note = note.replaceAll("\n", "<br />");
		Task t = taskPaperServices.getTaskById(id);
		t.setNote(note);
		taskPaperServices.updateTask(t);
	}
	
	
	@RequestMapping(value="/categoryUpdate",method=RequestMethod.POST)
	@ResponseBody
	public void updateCategory(@RequestParam Long id, @RequestParam String name){
		Category category = taskPaperServices.getCategoryById(id);
		category.setName(name);
		taskPaperServices.updateCategory(category);
		
	}
	
	@RequestMapping(value="/categoryDelete",method=RequestMethod.POST)
	@ResponseBody
	public void deleteCategory(@RequestParam Long id){
		Category category = taskPaperServices.getCategoryById(id);
		taskPaperServices.deleteCategory(category);
	}
	

}
