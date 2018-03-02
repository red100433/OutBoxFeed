$(".table").on("click", ".delete", function(event) { 
	event.stopPropagation();
	var userId = $('input[name=userId]').val();
	var serviceName = $('input[name=service]').val();
	var tr = $(this).closest("tr");
	var children = tr[0].children;
	
	remove(serviceName,children[0].outerText, children[1].outerText, userId, function(data){
		location.href="/remains/tservice"
	});
});

function remove(serviceName, channelGroup, channelId, userId, callback){
	$.ajax({
		type : "POST",
		url : "/remains/tservice/follows/unfollow/" + serviceName + "/" + channelGroup + "/" + channelId + "/" + userId,
		contentType : "application/json; charset=UTF-8",
		success : function(data) {
			callback(data);
			
		},
		error : function(request,status,error) {
			 alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

$(".table").on("click", ".channel", function(event) { 
	event.stopPropagation();
	var userId = $('input[name=userId]').val();
	var serviceName = $('input[name=service]').val();
	var tr = $(this).closest("tr");
	var children = tr[0].children;
	var channelId = children[1].outerText;

	location.href="/remains/myfeed/channelContent/" + channelId;
});