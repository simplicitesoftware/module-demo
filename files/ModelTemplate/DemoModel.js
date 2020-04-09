Simplicite.Diagram.ModelHooks.DemoModel = {

canUseShape: false,

imgFields: { "DemoSupplier": "demoSupLogo", "DemoProduct": "demoPrdPicture" },

// Asynchronous loading of picture
onLoadNode: function(node, cbk) {
	var self = this;
	var imgField = self.template.imgFields[node.object];
	if (imgField && node.data) {
		// Remove previous cached image in SVG definitons
		var imgId = node.data[imgField],
			imgName = "pict_"+node.object+"_"+node.id;
		self.desktop.removeDefImage(imgName, 50, 0);
		// Load picture before rendering
		if (imgId) {
			self.desktop.addDefImage(imgName,
				self.root + "/ui/document?object="+node.object+"&field="+imgField+"&row_id="+node.id+"&doc_id="+imgId+"&cdisp=inline",
				50, 0, "def-image", cbk);
			return;
		}
	}
	cbk();
},

// Synchronous rendering
onDrawNode: function(n, display) {
	var self = this, pad=10,
		elt = Simplicite.Diagram.createElement;

	var imgField = self.template.imgFields[n.object];
	if (imgField && n.data) {
		// Border
		var b = n.border = elt("rect", { x:0, y:0, width:0, height:0 });
		b.addClass("border").appendTo(n.elt);
		n.radius && b.attr({ rx: n.radius, ry: n.radius });
		if (n.shadow) b.attr("filter","url(#shadow)");
		else b.removeAttr("filter");
		n.bind(b);
	
		// Picture
		var x = pad,
			imgId = n.data[imgField],
			imgName = "pict_"+n.object+"_"+n.id;
		if (imgId) {
			self.desktop.getDefImage(imgName, 50, 0)
				.appendTo(n.elt)
				.attr({ x:x, y:pad });
			x += 50+pad;
		}
		
		// Product infos
		elt("text", { x:x, y:pad, dy:"1em"}).text(n.label).appendTo(n.elt).css("font-weight", "bold");
		if (n.object=="DemoSupplier") {
			elt("text", { x:x, y:pad, dy:"2em"}).text(n.data.demoSupName).appendTo(n.elt);
		} else if (n.object=="DemoProduct") {
			elt("text", { x:x, y:pad, dy:"2em"}).text(n.data.demoPrdName).appendTo(n.elt);
			elt("text", { x:x, y:pad, dy:"3em"}).text("Unit price: "+n.data.demoPrdUnitPrice).appendTo(n.elt);
		}

		// Set final size of border
		var box = n.elt[0].getBBox();
		n.size(box.width + pad*2, box.height + pad*2);
	} else { // Default drawing for other objects
		display();
	}
}

};