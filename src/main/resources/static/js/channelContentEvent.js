$(".table").on("click", ".delete", function(event) { 
	event.stopPropagation();
	
	var serviceName = $('input[name=serviceName]').val();
	var tr = $(this).closest("tr");
	var children = tr[0].children;
	var contentId = children[4].attributes[1].textContent;

	remove(serviceName, contentId, function(data){
		
		location.href="/remains/myfeed/channelContent/" + children[0].outerText;
	});

});

function remove(serviceName, contentId, callback){
	$.ajax({
		type : "POST",
		url : "/remains/myfeed/content/deleteContent/" + serviceName + "/" + contentId ,
		contentType : "application/json; charset=UTF-8",
		success : function(data) {
			callback(data);
			
		},
		error : function(request,status,error) {
			 alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}