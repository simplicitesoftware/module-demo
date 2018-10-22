Simplicite.ModelHooks.DemoModel = {

onLoadTemplate: function(template) {
	template.pictures = {};
},

onLoadNode: function(node) {
	var t = this.template;
	if (node.template.name == "DemoModel_Supplier") {
		delete t.pictures[node.object + "-" + node.id];
		node.picture = Simplicite.Application.documentURL("DemoSupplier", "demoSupLogo", node.id, node.data.demoSupLogo);
		node.bound = function() { 
			node.w = 100;
			var i = t.pictures[node.object + "-" + node.id];
			node.h = i && i.width>0 ? i.height*node.w/i.width : 100;
			return { x:node.x, y:node.y, w:node.w, h:node.h };
		};
	}
	else if (node.template.name == "DemoModel_Product") {
		delete t.pictures[node.object + "-" + node.id];
		node.picture = Simplicite.Application.documentURL("DemoProduct", "demoPrdPicture", node.id, node.data.demoPrdPicture);
		node.bound = function() { 
			node.w = 100;
			var i = t.pictures[node.object + "-" + node.id];
			node.h = i && i.width>0 ? i.height*node.w/i.width : 100;
			return { x:node.x, y:node.y, w:node.w, h:node.h };
		};
	} else if (node.template.name == "DemoModel_Client") {
		node.setIcon("user");
	}
},

onDrawNode: function(node) {
	if (node.picture) {
		var t = this.template;
		var c = this.desktop.ctx;
		var img = t.pictures[node.object + "-" + node.id];
		if (!img) {
			img = new Image();
			img.src = node.picture;
			img.onload = function() { c.drawImage(this, node.x, node.y, node.w, node.h); };
			t.pictures[node.object + "-" + node.id] = img;
		} else {
			c.drawImage(img, node.x, node.y, node.w, node.h);
		}
	} else {
		node.draw();
	}
}

};