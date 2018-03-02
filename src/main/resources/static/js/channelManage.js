$(document).ready(function() {
	$(document).scroll(function() {
		var maxHeight = $(document).height();
		var currentScroll = $(window).scrollTop() + $(window).height();

		if (maxHeight <= currentScroll) { 
			var userId = $('input[name=userId]').val();
			var serviceName = $('input[name=service]').val();
			var next = $('input[name=next]').val();
			var tr = $("tbody tr:first");
			var children = tr[0].children;
			
			if(next){
				getNextList(serviceName, children[0].outerText, userId, next, function(data){
					
					renderDom(data.channels.contents);
					$('input[name=next]').val(data.channels.next);
				});
			}
		} 
	});
});


function getNextList(serviceName, channelGroup, userId, next, callback){
	$.ajax({
		type : "GET",
		url : "/remains/tservice/follows/followList/" + serviceName + "/" + channelGroup + "/" + userId + "/" + next,
		contentType : "application/json; charset=UTF-8",
		success : function(data) {
			callback(data);
			
		},
		error : function(request,status,error) {
			 alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}


function renderDom(list) {
    var followDom = "";
    list.forEach(function(data){
    	followDom += makeDom(data);
    });
    $('table tr:last').after(followDom);
    
}

function makeDom(follow) {
    var dom = '<tr><td>' + follow.channel.group + '</td><td>' + follow.channel.id + '</td><td><button class="btn btn-primary btn-xs channel"><span class="glyphicon glyphicon-edit"></span>채널정보</button></td><td><button class="btn btn-danger btn-xs delete"><span class="glyphicon glyphicon-remove-circle"></span> 구독취소 </button></td></tr>';
    return dom;
}