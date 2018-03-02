$(document).ready(function() {
	$(document).scroll(function() {
		var maxHeight = $(document).height()-0.5;
		var currentScroll = $(window).scrollTop() + $(window).height();

		if (maxHeight <= currentScroll) {
			var next = $('input[name=next]').val();
			var userId = $('input[name=userId]').val();
			var channelGroup = $('input[name=channelGroup]').val();
			var serviceName = $('input[name=serviceName]').val();
			if (next) {
				getNextList(serviceName,channelGroup, userId, next, function(data) {
					
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

function getNextList(serviceName, channelGroup, userId, next, callback) {
	$.ajax({
		type : "POST",
		url : "/remains/myfeed/content/contentList/" + serviceName + "/" + channelGroup + "/" + userId + "/" + next,
		contentType : "application/json; charset=UTF-8",
		success : function(data) {
			callback(data);

		},
		error : function(request, status, error) {
			alert("code:" + request.status + "\n" + "message:"
					+ request.responseText + "\n" + "error:" + error);
		}
	});
}

function renderDom(list) {
	var followDom = "";
	
	list.forEach(function(data) {
		followDom += makeDom(data);
	});

	$('div li:last').after(followDom);
}

function makeDom(getContent) {

	var dom = '<li><a href="#"	class="list-group-item"> <span class="glyphicon glyphicon-star-empty"></span><span class="name" style="min-width: 120px; display: inline-block;">'
			+ getContent.getContentResponse.channel.id
			+ '</span> <span class="">'
			+ getContent.getContentResponse.contentBody
			+ '</span> <span class="text-muted" style="font-size: 11px;"></span><span class="badge">'
			+ getContent.getContentResponse.createDate[0] + '-0' + getContent.getContentResponse.createDate[1] + '-' + getContent.getContentResponse.createDate[2] + 'T' + getContent.getContentResponse.createDate[3] + ':' + getContent.getContentResponse.createDate[4] + ':' + getContent.getContentResponse.createDate[5] + '.' + getContent.getContentResponse.createDate[6].toString().substr(0,3)
			+ '</span> <span class="pull-right"><span class="glyphicon glyphicon-paperclip"> </span></span></a></li>';
	
	return dom;
}