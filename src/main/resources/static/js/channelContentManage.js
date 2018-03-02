$(document).ready(function() {
	$(document).scroll(function() {

		if ($(window).scrollTop() == $(document).height() - $(window).height()) { 
			var serviceName = $('input[name=serviceName]').val();
			var next = $('input[name=next]').val();
			var channelGroup = $('input[name=channelGroup]').val();
			var channelId = $('input[name=channelId]').val();
			
			if(next){
				getNextList(serviceName, channelGroup, channelId, next, function(data){
					
					if(next == data[data.length-1].feedId.toString()) {
						$('input[name=next]').val(0);
					}
					else {
						$('input[name=next]').val(data[data.length-1].feedId);
						renderDom(data);
					}
					
				});
			}
		} 
	});
});


function getNextList(serviceName, channelGroup, channelId, next, callback){
	$.ajax({
		type : "POST",
		url : "/remains/myfeed/content/channelContent/" + serviceName + "/" + channelGroup + "/" + channelId + "/" + next,
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

function makeDom(getContent) {
    var dom = '<tr><td>' + getContent.getContentResponse.channel.id + '</td><td>' + getContent.getContentResponse.contentBody + '</td><td>' 
    + getContent.getContentResponse.createDate[0] + '-0' + getContent.getContentResponse.createDate[1] + '-' + getContent.getContentResponse.createDate[2] + 'T' + getContent.getContentResponse.createDate[3] + ':' + getContent.getContentResponse.createDate[4] + ':' + getContent.getContentResponse.createDate[5] + '.' + getContent.getContentResponse.createDate[6].toString().substr(0,3) 
    + '</td><td><button class="btn btn-danger btn-xs delete"><span class="glyphicon glyphicon-remove-circle"></span> 삭제</button></td><td type="hidden" th:text='+ getContent.getContentResponse.contentId + '></td></tr>';
    return dom;
}

