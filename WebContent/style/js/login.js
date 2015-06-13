/**
 * 
 */

var progressBar = new BootstrapDialog({
	closable:false,
	message: 'Please Wait...'
}); 
progressBar.realize();
progressBar.getModalHeader().hide();
progressBar.getModalFooter().hide();

var LoginElement = function(){
	var self = this;
	self.username = ko.observable();
	self.password = ko.observable();
}


var SignElement = function(){
	var self = this;
	self.lgUsername = ko.observable();
	self.lgEmail = ko.observable();
	self.lgFirstName = ko.observable();
	self.lgLastName = ko.observable();
	self.lgPassword = ko.observable();
}

var isValidSignUp = function(){ 
	if(signElement.lgUsername()==undefined)
        return false;
	if(signElement.lgEmail()==undefined)
        return false;
	if(signElement.lgFirstName()==undefined)
        return false;
	if(signElement.lgLastName()==undefined)
        return false;
	if(signElement.lgPassword()==undefined)
        return false;
	
	return true;
		
}


function LoginViewModel() {
    self.handleError = function(jqXHR){
         console.log("ajax error " + jqXHR.status);
         if(jqXHR.status == 401){
        	 location.href = "login.html";
         }
    }
	self.checkFailAuth = function(){ 
		if(location.hash=='#authFailed'){ 
			alert("Wrong username or password..","Authentication Failed",BootstrapDialog.TYPE_DANGER);
		}
		location.hash='';
//    	delCookie("username");
//    	delCookie("password");
		window.localStorage.removeItem("username");
		window.localStorage.removeItem("password");
	}
    self.ajax = function(uri, method, data) {
        progressBar.open();
        var request = {
            url: uri,
            type: method,
//            contentType: "application/json",
//            accepts: "application/json",
            cache: false,
//            dataType: 'json',
            data: data,
//            beforeSend: function (xhr) {
//                xhr.setRequestHeader("Authorization", 
//                    "Basic " + btoa(self.username + ":" + self.password));
//            },
            error: function(jqXHR) {
                self.handleError(jqXHR);
            },
            complete: function(){
            	progressBar.close();
            }
        };
        return $.ajax(request);
    }
	self.loginElement = new LoginElement();	
	self.signElement = new SignElement();
	var baseUrl = getBaseUrl();
	self.checkFailAuth();
	
    if(baseUrl.search("47.168.150.224")!=-1){
    	baseUrl = 'http://'+baseUrl+':9080/TaskPaper/rest/login';
    	
    }else{
    	
    	baseUrl = 'http://'+baseUrl+':8080/TaskPaper/rest/login';
    }
    self.loginURI = baseUrl;
    
	
	self.loginEvent = function(){
		if(!loginElement.username() || !loginElement.password()){
			alert("no username or password");
			return;
		}

//		setCookie("username",loginElement.username(),30);
//		setCookie("password",loginElement.password(),30);
		window.localStorage.setItem("username",loginElement.username());
		window.localStorage.setItem("password",loginElement.username());
		location.href = "index.html";
	}
	
	self.signUpEvent = function(){
		if(!isValidSignUp()){ 
			alert("Form is not valid!");
			return; 
		}
		self.ajax(self.loginURI,'POST',signElement).done(function(res){
			if(res==true){
				loginElement.username(signElement.lgUsername());
				loginElement.password(signElement.lgPassword());
				self.loginEvent();
			}
		});
	}
	

}
ko.applyBindings(new LoginViewModel());