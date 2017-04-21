//$("#navCategory").hover(
//  function(){
//	  $(".btnCategory").fadeIn();
//  },
//  function(){
//	  $(".btnCategory").fadeOut();
//  }
//);

$(function(){
	
	
    ko.applyBindings(new TasksViewModel());
    $(".showCompleted").click(function(){
    	$("#completedSlider").slideToggle();
    });

});

ko.bindingHandlers.checkBoxToggle = {
    init: function(element,valueAccessor){
//        console.log(element);
    }
};
ko.bindingHandlers.sortableList = {
	    init: function(element, valueAccessor,allBindingAccessor,viewModel) {
	        var list = (valueAccessor());
	        $(element).sortable({
	            update: function(event, ui) {
	                //retrieve our actual data item
	                var item = ui.item.tmplItem().data;
	                //figure out its new position
	                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
	                //remove the item and add it back in the right spot
	                if(item instanceof todoCategory){
	                	for(var x=0; x< list().length; x++){
	                		var categoryItem = list()[x];
	                		if(categoryItem.order == position){
	                			categoryItem.order=item.order;
	                			item.order=position;
	                			$("#categoryLoader").slideDown();
                                viewModel.ajax(viewModel.tasksURI+"/categoryUpdate",'POST', categoryItem).done(function(){
                                        viewModel.ajax(viewModel.tasksURI+"/categoryUpdate",'POST', item).done(function(){
                                        	$("#categoryLoader").slideUp();
                                        });
                                });
	                			break;
	                		}
	                	}
	                }
	                if (position >= 0) {
	                    list.remove(item);
	                    list.splice(position, 0, item);
	                }
	                ui.item.remove();
	            }
	        });
	    }
	};
function drag(ev) {
    ev.dataTransfer.setData("text", ev);
}
function allowDrop(ev) {
    ev.preventDefault();
}
function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    var regexp=/\/(\d+)/;
    var taskId = regexp.exec(data)[1];
    var categoryName = $( $(ev.target).find('span')[0] ).text();
    
    var username = window.localStorage.getItem('username');
    var password = window.localStorage.getItem('password');
    var baseUrl = getBaseUrl();
	
    baseUrl = 'http://'+baseUrl+':8080/TaskPaper/rest/todos';
    var request = {
          url: baseUrl+"/modify",
          type: "POST",
          cache: false,
          data: {"taskId":taskId,"categoryName":categoryName},
          beforeSend: function (xhr) {
          	if(username && password) {
                      xhr.setRequestHeader("Authorization", 
                          "Basic " + btoa(username + ":" + password));
          		
          	}
          },
          error: function(jqXHR) {
//              self.handleError(jqXHR);
          },
          complete: function(){
        	  location.hash="#/"+categoryName;
          }
    };
      
      $.ajax(request).done(function(){
    	  console.log("operation complete");
    	  
      });
      

}
ko.bindingHandlers.preventBubble = {
        init: function (element, valueAccessor) {
            var eventName = ko.utils.unwrapObservable(valueAccessor());
            var arr = eventName;
            if (!eventName.pop) {
                arr = [arr];
            }
            for (var p in arr) {
                ko.utils.registerEventHandler(element, arr[p], function (event) {
                    event.cancelBubble = true;
                    if (event.stopPropagation) {
                        event.stopPropagation();
                    }
                });
            }
        }
    };

ko.bindingHandlers.fadeVisible = {
		
		init: function(element, valueAccessor){},
		update: function(element, valueAccessor){
			var shouldDisplay = valueAccessor();
			shouldDisplay ? $(element).fadeIn() : $(element).hide();
		}
};
ko.bindingHandlers.fadeVisibleDelay = {
		
		init: function(element, valueAccessor){},
		update: function(element, valueAccessor){
			var shouldDisplay = valueAccessor();
			if(shouldDisplay){
				setTimeout(function(){
					$(element).fadeIn()
				},700);
			}else{
				$(element).hide()
			}
		}
};

//Unused
ko.bindingHandlers.narrow = {
		init: function(element, value){},
		update: function(element,value){
			var shouldDisplay = value();
			if(shouldDisplay){
					$(element).removeClass("col-md-9");
					$(element).addClass("col-md-6");
			}else{
					$(element).removeClass("col-md-6");
					$(element).addClass("col-md-9");
				
			}
		}
};

var progressBar = new BootstrapDialog({
	closable:false,
	message: 'Please Wait...'
}); 
progressBar.realize();
progressBar.getModalHeader().hide();
progressBar.getModalFooter().hide();
var todoTask = function(description,rawDate){
    var self = this; 
    self.description =  ko.observable( description );
    self.done = ko.observable(false);
    self.favorite = ko.observable(false);
    self.note = ko.observable('');
    self.rawCompleteDate = ko.observable();
    self.subTaskCount = ko.observable();
    self.date = ko.computed(function(){
        var day = rawDate.getDate();
        var month = rawDate.getMonth()+1;
        var year = rawDate.getFullYear();
        var dateFull = month + '/' + day + '/' + year;
        return dateFull;
    });
    self.completeDate = ko.computed(function(){
    	
    	if(!self.rawCompleteDate())
    		return;
    	
        var day = self.rawCompleteDate().getDate();
        var month = self.rawCompleteDate().getMonth()+1;
        var year = self.rawCompleteDate().getFullYear();
        var dateFull = month + '/' + day + '/' + year;
        return dateFull;
    });
    var converter = new Markdown.Converter();
	self.parsedNote = ko.computed(function(){
		
		var note = self.note();
		if(!note)
			return;
		note = note.replace(/\n/g,"<br />");
//
//		return converter.makeHtml(note);
		if(isUrl(note)){
			return parseUrl(note);
		}else{
			return note;

		}
	});

}

var todoTask2 = function(data){
	var self = this;
	self.id = data.id;
	self.description = data.description;
	self.task = data.task;
	self.enabled = data.enabled;
	self.note = data.note;
	self.done = ko.observable(data.done);
	self.favorite = ko.observable(data.favorite);
	self.date = new Date(data.date);
	self.computedDate = ko.computed(function(){
        var day = self.date.getDate();
        var month = self.date.getMonth()+1;
        var year = self.date.getFullYear();
        var dateFull = month + '/' + day + '/' + year;
        return dateFull;
	});
	
}

var todoController = function(name,id,event){
	var self = this;
	self.name=name;
	self.id=id;
	self.event = event;
	
	
}

var Repeater = function (name,id){
	var self = this;
	self.name = name;
	self.id = id;
}

var todoCategory = function(data){
	var self = this;
	self.id = data.id;
	self.name = data.name;
	self.rawDate = new Date(data.date);
	self.taskCount=ko.observable(data.taskCount);
	self.completedTaskCount=ko.observable(data.completedTaskCount);
	self.enabled = data.enabled;
    self.repeater = ko.observable(data.repeater);
    self.order=data.order;
    self.date = ko.computed(function(){
        var day = self.rawDate.getDate();
        var month = self.rawDate.getMonth()+1;
        var year = self.rawDate.getFullYear();
        var dateFull = month + '/' + day + '/' + year;
        return dateFull;
    });
    
    self.totalTaskCount = ko.computed(function(){
    	return self.taskCount()+self.completedTaskCount();
    	
    });
    

	
}

/**
 * order by favorite
 */
var customPush = function(element,array){
	if(element.favorite()){
		array.unshift(element);
	}else{
		array.push(element);
	}
}

function TasksViewModel() {
    var self = this;
    self.tasks = ko.observableArray([]);
    self.subtasks = ko.observableArray([]);
    self.completeTasks = ko.observableArray([]);
    self.completeSubtasks = ko.observableArray([]);
    self.newTaskDescription = ko.observable();
    self.newSubtaskDescription = ko.observable();
    self.newCategoryName = ko.observable();
    self.categories = ko.observableArray([]);
//    Çapraz köken isteği engellendi: Aynı Köken İlkesi,
//    http://192.168.1.102:8080/TaskPaper/rest/todos/getCategories?_=1432321023986
//    üzerindeki uzak kaynağın okunmasına izin vermiyor. (Sebep: CORS üstbilgisi
//    'Access-Control-Allow-Origin' eksik.)
    var baseUrl = getBaseUrl();
    	
    baseUrl = 'http://'+baseUrl+':8080/TaskPaper/rest/todos';
    self.tasksURI = baseUrl;
    self.username = window.localStorage.getItem('username');
    self.password = window.localStorage.getItem('password');
    self.selectedCategoryName = ko.observable();
    self.selectedTaskId=ko.observable();
    self.selectedCategory= ko.observable();
    self.selectedController = ko.observable();
    self.selectedTask = ko.observable('');
    self.selectedNote= ko.observable('');
    self.showCompleted = ko.observable(false);
    self.handleError = function(jqXHR){
         console.log("ajax error " + jqXHR.status);
//         for(var props in jqXHR){
//        	self.error(self.error()+"__"+props); 
//         }
         if(jqXHR.status == 401){

        	 if(!self.username) 
        		 location.href = "login.html";
        	 else 
        		 location.href = "login.html#authFailed";
         }
         if(jqXHR.status == 0){
        	 //handle CORS
                 console.log("handle CORS");
                 location.href = "login.html#CORS";
         }
    }
    self.ajax = function(uri, method, data) {
//        progressBar.open();
        var request = {
            url: uri,
            type: method,
//            contentType: "application/json",
//            accepts: "application/json",
            cache: false,
//            dataType: 'json',
            data: data,
            beforeSend: function (xhr) {
            	if(self.username && self.password) {
                        xhr.setRequestHeader("Authorization", 
                            "Basic " + btoa(self.username + ":" + self.password));
            		
            	}
            },
            error: function(jqXHR) {
                self.handleError(jqXHR);
            },
            complete: function(){
//            	progressBar.close();
            }
        };
        return $.ajax(request);
    }

    self.getTasks = function(categoryId){
    	self.tasks([]);
    	self.completeTasks([]);
    	if(self.selectedCategory()){
    		var taskCount = self.selectedCategory().totalTaskCount();
    		if(!taskCount || taskCount == 0)
    			return;
    	}
    	console.log("getting tasks for categoryId: " + categoryId);
    	var preservedHtml = $("#task-container").html();
    	$("#task-container").fadeOut();
    	$("#taskLoader").slideDown();
    	self.ajax(self.tasksURI+"/getByCategory/"+categoryId, 'GET').done(function(data) {
    		
    		for(var i = 0; i<data.length; i++){
    			var taskElem = new todoTask(data[i].description,new Date(data[i].date));
    			taskElem.id = data[i].id;
    			taskElem.done(data[i].done);
    			taskElem.favorite(data[i].favorite);
    			taskElem.rawCompleteDate(new Date(data[i].completeDate));
    			taskElem.note(data[i].note);
    			taskElem.subTaskCount(data[i].subTaskCount);
    			if(taskElem.done()){
    				self.completeTasks.push(taskElem);
    			}else{
    				self.tasks.push(taskElem);
    			}

    		}

    		console.log("fetching tasks completed...");
    		$("#task-container").fadeIn();
    		$("#taskLoader").slideUp();
    	});
    	
    }
    //Category functions

    self.gotoCategory = function(category){


      self.selectedTask('');
      self.selectedTaskId('');
      self.selectedCategory(category);
      location.hash = "#/"+category.name;
    };
    
    self.getCategories = function(){
    	$("#categoryLoader").fadeIn();
    	self.ajax(self.tasksURI+"/getCategories",'GET').done(function(data){
            $("#categoryLoader").slideUp();
    		self.categories([]);
    		var cat;
    		var selectedCatIndex;
    		for(var i = 0; i<data.length; i++){
    			cat = new todoCategory(data[i]);

    			self.categories.push(cat);
    			if(self.selectedCategoryName()){
    				if(self.selectedCategoryName() == cat.name){
    					selectedCatIndex = i;
    				}


    			}
    		}
    		self.selectedCategory(self.getCategoryByName(self.categories(), self.selectedCategoryName()));

//    		if (!self.selectedCategoryName()){
//    			self.gotoCategory(self.categories()[0]);
//    		} else {
//    			self.gotoCategory(self.categories()[selectedCatIndex]);
//
//    		}
    	});
    }
    

    self.addTask = function(){
        
        var taskElem = new todoTask(self.newTaskDescription(),new Date());
        self.newTaskDescription('');
        taskElem.categoryName = self.selectedCategoryName();
        
            //self.tasks.unshift(taskElem);
            //self.selectedCategory().taskCount(self.selectedCategory().taskCount()+1);
        

        self.ajax(self.tasksURI, 'POST',taskElem).done(function(data) {
                if(data.id){
                    taskElem.id=data.id;
                    self.tasks.unshift(taskElem);
                    self.selectedCategory().taskCount(self.selectedCategory().taskCount()+1);
                }
        });
        console.info("New task '"+taskElem.description()+"' added.");
    }

    
    self.addSubtask = function(){
    	var subTaskElem = new todoTask(self.newSubtaskDescription(),new Date());
    	self.newSubtaskDescription('');
    	subTaskElem.taskId = self.selectedTask().id;
    	self.ajax(self.tasksURI+"/subtask",'POST',subTaskElem).done(function(data){
    		if(data.id){
    			subTaskElem.id = data.id;
    			self.subtasks.unshift(subTaskElem);
    			self.selectedTask().subTaskCount(self.selectedTask().subTaskCount()+1);
    		}
    	});
    	
    }
    
    self.addCategory = function(){
    	var size = self.categories().length;
    	var pos = self.categories()[size-1].order;
    	pos = pos+1
    	var categoryElement={name: self.newCategoryName(), rawDate:new Date(), order: pos};// = new todoCategory(self.newCategoryName(), new Date());
    	self.newCategoryName("");
    	self.ajax(self.tasksURI+"/category",'POST',categoryElement)
    	.done(function(data){
    		categoryElement = new todoCategory(data);
//    		categoryElement.id = data.id;
    		self.categories.push(categoryElement);
                location.hash = "#/"+categoryElement.name;
    		
    	});
    	
    	
    }

    self.removeTask = function(){ 
    	var taskElem = this;
    	BootstrapDialog.confirm('Are you sure?',function(result){
    		
    		if(result){
    			if(self.selectedTask() === taskElem)
    				self.selectedTask('');

    			self.ajax(self.tasksURI+"/delete", 'POST',taskElem).done(function(data) { 
    				if(taskElem.done()){
    					self.completeTasks.remove(taskElem);
    					self.selectedCategory().completedTaskCount(self.selectedCategory().completedTaskCount()-1);
    				}else{
    					self.tasks.remove(taskElem); 
    					self.selectedCategory().taskCount(self.selectedCategory().taskCount()-1);
    				} 
    			});
    		}
    		
    	});
    

    }
    
    self.updateTaskNote = function(task){
    	self.ajax(self.tasksURI+"/note",'POST',task).done(function(){
    		
    	});
    }
    
    self.checkDone=function(){
    	var taskElem = this;
    	taskElem.rawCompleteDate(taskElem.done() ? new Date() : null);
    	if(taskElem.done()){
    		self.tasks.remove(taskElem);
    		customPush(taskElem, self.completeTasks); 
    		self.selectedCategory().taskCount(self.selectedCategory().taskCount()-1);
    		self.selectedCategory().completedTaskCount(self.selectedCategory().completedTaskCount()+1);
    	}else{

    		self.completeTasks.remove(taskElem);
    		customPush(taskElem, self.tasks);
    		self.selectedCategory().taskCount(self.selectedCategory().taskCount()+1);
    		self.selectedCategory().completedTaskCount(self.selectedCategory().completedTaskCount()-1);
    		
    	} 
    	self.ajax(self.tasksURI+"/complete", 'POST',taskElem).done(function(data) {
    	});
    	
    	self.selectedTask('');
            
    	return true;
    }
    
    
    self.getSubtasks = function(task){
    	self.subtasks([]);
    	self.completeSubtasks([]);
    	$("#subtask-loader").fadeIn();
    	self.ajax(self.tasksURI + "/subtask",'GET',task).done(function(data){
            $("#subtask-loader").slideUp();
    		for(var i = 0; i<data.length; i++){ 
    			var subTaskElem = new todoTask2(data[i]);
    			if(!subTaskElem.done()){ 
    				self.subtasks.push(subTaskElem);
    			}else{ 
    				self.completeSubtasks.push(subTaskElem);
    			}
    			
    		}
    		
    	});
    }
    
    self.checkSubtaskDone = function(){
    	var subtaskElem = this;
    	if(subtaskElem.done()){
    		self.subtasks.remove(subtaskElem);
    		customPush(subtaskElem,self.completeSubtasks);
    		self.selectedTask().subTaskCount(self.selectedTask().subTaskCount()-1);
    	}else{
    		self.completeSubtasks.remove(subtaskElem);
    		customPush(subtaskElem, self.subtasks);
    		self.selectedTask().subTaskCount(self.selectedTask().subTaskCount()+1);
    	}
    	
    	self.ajax(self.tasksURI+"/subtask/complete","POST",subtaskElem);
    	
    	return true;
    	
    }
    
    self.subtaskClick = function(){
    	
    }
    
    self.subtaskRatingClick = function(){
    	var elem = this;
    	elem.favorite(!elem.favorite());
    	self.ajax(self.tasksURI+'/subtask/favorite','POST',elem).done(function(data){
    		if(!elem.done() ){
    			//make first element in array
    			self.subtasks.remove(elem);
    			customPush(elem, self.subtasks);
    		}
    		
    	});
    }
    
    self.removeSubTask = function(){
    	
    	var taskElem = this;
    	BootstrapDialog.confirm('Are you sure?',function(result){
    		
    		if(result){

    			self.ajax(self.tasksURI+"/subtask/delete", 'POST',taskElem).done(function(data) { 
    				
    				if(taskElem.done()){
    					self.completeSubtasks.remove(taskElem); 
    				}else{
    					self.subtasks.remove(taskElem); 
    					self.selectedTask().subTaskCount(self.selectedTask().subTaskCount()-1);
    				} 

    					
    			});
    		}
    		
    	});
    
    }

    /**
    self.categories = ["Inbox","Business"];
    self.categories.push("Logout");
    */

    self.logout = function(){
//    	delCookie("username");
//    	delCookie("password");
    	window.localStorage.removeItem("username");
    	window.localStorage.removeItem("password");
    	location.href = "login.html";
    	self.selectedController(this.name);
    }
    
    self.addNewItem = function(){
    	 BootstrapDialog.show({
    		 title: 'Add New Item',
             message: '<input type="text" class="form-control"  required autofocus data-bind="value:newCategoryName " placeholder="Add New Category" />',
             buttons: [{
                 id: 'btn-ok',   
                 icon: 'glyphicon glyphicon-check',       
                 label: 'OK',
                 cssClass: 'btn-primary', 
                 autospin: false,
                 hotkey:13,//enter
                 action: function(dialogRef){    
                	 var categoryName = dialogRef.getModalBody().find('input').val();
                	 if(categoryName.length==0)
                		 return
                	 self.newCategoryName(categoryName);
                	 self.addCategory();
                     dialogRef.close();
                 }
             }],
             onshown: function(dialogRef){
            	 var element = dialogRef.getModalBody().find('input');
            	 element.focus();
            	 element[0].setSelectionRange(0,element.val().length);
             }
         });
    	self.selectedController(this.name);
    }
    
    self.controllers = [];
    self.controllers.push(new todoController("Add New Item",1,self.addNewItem));
    //self.controllers.push(new todoController("Logout",2,self.logout));

    self.repeaters = [];
    self.repeaters.push("NO_REPEAT");
    self.repeaters.push("DAILY");
    self.repeaters.push("WEEKLY");
    self.repeaters.push("MONTHLY");

    self.setRepeat = function(repeater){
        console.log(self.selectedCategory());
        if(self.selectedCategory()){
        	self.selectedCategory().repeater(repeater);
        	self.ajax(self.tasksURI+'/repeater','POST',self.selectedCategory()).done(function(data){
        	});
        	
        }
    	
    }


    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = 'Information';
    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = 'Information';
    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = 'Information';
    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = 'Success';
    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = 'Warning';
    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = 'Danger';
    BootstrapDialog.DEFAULT_TEXTS['OK'] = 'OK';
    BootstrapDialog.DEFAULT_TEXTS['CANCEL'] = 'Cancel';
    BootstrapDialog.DEFAULT_TEXTS['CONFIRM'] = 'Confirmation';
    
    //self.getCategories();

    self.ratingClick = function(){
    	var elem = this;
    	elem.favorite(!elem.favorite());
    	self.ajax(self.tasksURI+'/favorite','POST',elem).done(function(data){
    		if(!elem.done() ){
    			//make first element in array
    			self.tasks.remove(elem);
    			customPush(elem, self.tasks);
    		}
    		
    	});
    	
    }
    
    self.taskClick = function(item,event){ 
    	$(".task").removeClass("activeTask");
    	
    	if(this.description === self.selectedTask().description){
    		self.selectedTask(''); 
            location.hash="#/"+self.selectedCategoryName();
    	}else{ 
    		self.selectedTask(this); 
            $( event.target ).closest("tr").addClass("activeTask");
            location.hash="#/"+self.selectedCategoryName()+"/" + this.id;
    	}
    }
    
    self.noteClick = function(item,event){
    	if(event.target.nodeName==='A'){
    		window.open(event.target.href);
    		return;
    	}
    	var task = self.selectedTask();
    	var tasknote = task.note() ? task.note() : ''; 
    	tasknote = tasknote.replace(/<br\s*\/?>/mg,"&#13;&#10;");
    	tasknote = tasknote.replace(/(\r\n|\n|\r)/gm,"&#13;&#10;");
    	 BootstrapDialog.show({
    		 title: 'Add Note',
             message: '<textarea class="form-control"  placeholder="Add Note..." >'+tasknote+'</textarea>',
             buttons: [{
                 id: 'btn-ok',   
                 icon: 'glyphicon glyphicon-check',       
                 label: 'OK',
                 cssClass: 'btn-primary', 
                 autospin: false,
                 action: function(dialogRef){    
                	 var note = dialogRef.getModalBody().find('textarea').val();
                	 self.selectedTask().note(note); 
                	 self.updateTaskNote(self.selectedTask()); 
                	 dialogRef.close();
                 }
             }],
             onshown: function(dialogRef){
            	 var element = dialogRef.getModalBody().find('textarea');
            	 element.focus();
            	 element[0].setSelectionRange(0,element.val().length);
             }
         });
    }
    
    $("#txtNewTask").focus();
    $('[autofocus]:first').focus();
    
    self.updateCategory = function(){
    	
//    	self.categories.remove(self.selectedCategory());
    	self.selectedCategory().name=self.selectedCategoryName();
    	self.ajax(self.tasksURI+"/categoryUpdate",'POST',self.selectedCategory()).done(function(){
    		self.getCategories();
//    		self.categories.push(self.selectedCategory());
    		
    	});
    }
    
    
    self.editCategory = function(){
    	if(self.selectedCategoryName()=='Inbox') 
    	{ 
    		alert("You can not edit Inbox!");
    		return;
    	}
    	var oldCategoryName= self.selectedCategoryName();
    	
             BootstrapDialog.show({
                     title: 'Edit Category',
             message: '<input type="text" class="form-control"   required autofocus value="'+self.selectedCategoryName()+'" placeholder="Edit Category" />',
             buttons: [{
                 id: 'btn-ok',   
                 icon: '',       
                 label: 'OK',
                 cssClass: 'btn-primary', 
                 autospin: true,
                 hotkey:13,//enter
                 action: function(dialogRef){    
                         var categoryName = dialogRef.getModalBody().find('input').val(); 
                         if(categoryName.length==0)
                                 return;
                         self.selectedCategoryName(categoryName);
                         self.updateCategory(); 
                         dialogRef.close();
                 }
             }],
             onshown: function(dialogRef){
                     var element = dialogRef.getModalBody().find('input');
                     element.focus();
                     element[0].setSelectionRange(0,element.val().length);
             }
         });
    }
    
    self.updateTask = function(task){
    	self.ajax(self.tasksURI+"/taskUpdate",'POST',task);
    }
    
    self.editTask = function(){
    	var taskElem = this;
    	var oldTaskDescription = taskElem.description; 
    	BootstrapDialog.show({
    		 title: 'Edit Task',
             message: '<input type="text" class="form-control"  required autofocus value="'+taskElem.description()+'" placeholder="Edit Task" />',
             buttons: [{
                 id: 'btn-ok',   
                 icon: '',       
                 label: 'OK',
                 cssClass: 'btn-primary', 
                 autospin: true,
                 hotkey:13,//enter
                 action: function(dialogRef){    
                	 var newDescription = dialogRef.getModalBody().find('input').val(); 
                	 if(newDescription.length==0)
                		 return;
                	 taskElem.description( newDescription );
                	 self.updateTask(taskElem);
                	 dialogRef.close();
                 }
             }],
             onshown: function(dialogRef){
            	 var element = dialogRef.getModalBody().find('input');
            	 element.focus();
            	 element[0].setSelectionRange(0,element.val().length);
             }
        });
    }
    
    self.deleteCategory = function(){
    	if(self.selectedCategoryName()=='Inbox') 
    	{ 
    		alert("You can not remove Inbox!");
    		return;
    	}
    	BootstrapDialog.confirm('Are you sure?',function(result){
    		
    		if(!result){
    			return;
    		} 
    		self.removeCategory(self.selectedCategory());
    		
    	});
    }
    
    self.removeCategory = function(category){
    	self.ajax(self.tasksURI+"/categoryDelete",'POST',category).done(function(){
            self.categories.remove(self.selectedCategory()); 
            for(var x = 0; x<self.categories().length;x++){
            	var categoryItem = self.categories()[x];
            	categoryItem.order=x;
            	self.ajax(self.tasksURI+"/categoryUpdate",'POST',categoryItem).done(function(){
            	});
            	
            }
            self.gotoCategory(self.categories()[0])
        
        });
    }
    
    self.noteFullScreen = function(){

    	alert(self.selectedTask().parsedNote());
    }
    
    
    self.modalVisible = true; 
    self.toggleCompleted = function(){
    	self.showCompleted(!self.showCompleted());
    }
    
    function sleep(milliseconds) {
    	var start = new Date().getTime();
    	for (var i = 0; i < 1e7; i++) {
    		if ((new Date().getTime() - start) > milliseconds){
    			break;
    		}
    	}
    }
    
    self.getCategoryByName = function(categories,name) {
    	
    	if(!categories && categories.length == 0)
    	{
    		return undefined;
    	}
    	for(var i = 0; i<categories.length; i++){
    		var tempCat = categories[i];
    		if(tempCat.name === name){
    			return tempCat;
    		}
    	}

    }
    
    self.getTaskById = function(tasks, id){
    	if(!tasks && tasks.length == 0)
    	{
    		console.log("no element");
    		return undefined;
    	}
    	for(var i = 0; i<tasks.length; i++){
    		var tempTask = tasks[i];
    		if(tempTask.id === id){
    			return tempTask;
    		}
    	}
    	
    }

    Sammy(function(){
    	this.get("#/:category",function(){
    		  self.selectedTask('');
    		  self.selectedTaskId('');
              var categoryName = this.params.category;
    		
              self.selectedCategoryName(categoryName);
              if(self.categories().length == 0) {
                      listCategories(categoryName);
              } else {
            	  
            	  self.selectedCategory(self.getCategoryByName(self.categories(), categoryName));
              }
              if (self.selectedCategoryName() != categoryName || self.tasks().length == 0) {
            	  self.getTasks(categoryName);
            	  
              }
              return false;
            	  
    	});
    	
    	this.get("#/:category/:taskId",function(){
    		var categoryName = this.params.category;
    		var taskId = ( this.params.taskId );
    		if (taskId == "undefined") {
    			location.hash = "#/"+categoryName;
    			return false;
    			
    		}
    		taskId = parseInt(taskId);
    		self.selectedTaskId(taskId);


    		if (self.categories().length < 1) {
    			listCategories(categoryName);
    		}  else {
    			
            	  self.selectedCategory(self.getCategoryByName(self.categories(), categoryName));
    		}
    		if(self.tasks().length == 0 && self.completeTasks().length == 0)
    		{ 
    			self.ajax(self.tasksURI+"/getByCategory/"+categoryName, 'GET').done(function(data) {

    				for(var i = 0; i<data.length; i++){
    					var taskElem = new todoTask(data[i].description,new Date(data[i].date));
    					taskElem.id = data[i].id;
    					taskElem.done(data[i].done);
    					taskElem.favorite(data[i].favorite);
    					taskElem.rawCompleteDate(new Date(data[i].completeDate));
    					taskElem.note(data[i].note);
    					taskElem.subTaskCount(data[i].subTaskCount);
    					if(taskElem.done()){
    						self.completeTasks.push(taskElem);
    					}else{
    						self.tasks.push(taskElem);
    					}

    				}	
    				var currentTask = self.getTaskById(self.tasks(), taskId);
    				if ( !currentTask ) {
    					currentTask = self.getTaskById(self.completeTasks(), taskId);
    				}


    				if (currentTask.done()){

    					$("#completedSlider").show();
    				}

    				$(".task").removeClass("activeTask");

    				self.selectedTask(currentTask);
    				self.getSubtasks(currentTask);
    				$(event.target).closest("tr").addClass("activeTask");
    			});
    		} else {
    			var currentTask = self.getTaskById(self.tasks(), taskId);
    			if ( !currentTask ) {
    				currentTask = self.getTaskById(self.completeTasks(), taskId);
    			}

    			if (currentTask.done()){
    				
                    $("#completedSlider").show();
    			}


    			$(".task").removeClass("activeTask");

    			self.selectedTask(currentTask);
    			self.getSubtasks(currentTask);
    			$(event.target).closest("tr").addClass("activeTask");

    		}


    	});
    	
    	this.notFound = function (){
    		
    		console.log("couldn't find category...");
    		location.hash="#/Inbox"
    	}
    	
    	var listCategories = function(categoryName){
              self.selectedCategoryName(categoryName);
              self.selectedTask('');

              if(self.categories().length == 0){
            	  
            	  self.getCategories();
              }else {
            	  
    			self.selectedCategory(self.getCategoryByName(self.categories(), categoryName));
            	  
              }
//              self.getTasks(categoryName);
              $("#completedSlider").slideUp();
    		
    	}

    }).run();
    
}

