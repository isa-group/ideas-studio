
var ctxMenu;

//jQuery(function() {
//
//	ctxMenu = $('<div id="contextMenu"><ul class="dropdown-menu" role="context-menu"><li><a>Sample</a></li></ul></div>');
//	$("body").append(ctxMenu);
//	
//	$(document).bind('contextmenu', function(e) {
//		e.preventDefault();
//		ContextAction.show(e, e.target);
//	});
//	
//	$(document).click(function () {
//		hideMenu();
//	});
//	
//	
//});

var showMenu = function (e) {
	ctxMenu.addClass("open");
	ctxMenuUl = $(ctxMenu.children()[0]);
	var posx = ctxMenuUl.outerWidth();
	var posy = ctxMenuUl.outerHeight();

	if (e.pageX + posx + 20 > $(window).width()) {
		ctxMenu.css({left: e.pageX - posx});
	} else {
		ctxMenu.css({left: e.pageX});
	}
	
	if (e.pageY + posy + 50 > $(window).height()) {
		ctxMenu.css({top: e.pageY - posy});
	} else {
		ctxMenu.css({top: e.pageY});
	}        
};

var hideMenu = function () {
	$("#contextMenu").removeClass("open");        
};

var createMenu = function() {
};

var ContextAction = {

	register : function(element, actions) {
		
	},

	show : function(e, element) {
		showMenu(e);
		
	}
};