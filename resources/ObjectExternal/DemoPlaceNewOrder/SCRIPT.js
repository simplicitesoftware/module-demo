//-----------------------------------------------------------
// Client side JavaScript for place new order external object
//-----------------------------------------------------------

var DemoPlaceNewOrder = typeof DemoPlaceNewOrder !== "undefined" ? DemoPlaceNewOrder : (function($) {

var app, cli, sup, prd;

function render() {
	app = $ui.getAjax();

	app.setErrorHandler(function(err) {
		$("#placeneworder-err").append($("<p/>").text(app.getErrorMessage(err))).show();
	});

	$("#placeneworder-ord").append($ui.view.tools.panel({ title: "Order", content: $("<div/>")
		.append($("<div/>").append("Selected customer:"))
		.append($("<div/>", { id: "placeneworder-selcli" }).append("&lt;none&gt;")).append("<br/>")
		.append($("<div/>").append("Selected product:"))
		.append($("<div/>", { id: "placeneworder-selprd" }).append("&lt;none&gt;")).append("<br/>")
		.append($("<div/>")
			.append($("<input/>", { id: "placeneworder-qty", type: "text"}).val(1).change(total))
			.append("&nbsp;Total:&nbsp;")
			.append($("<span/>", { id: "placeneworder-total" }).append("0.00"))
			.append("&nbsp;&euro;&nbsp;")
			.append($("<button/>", { id: "placeneworder-ok", disabled: true }).addClass("btn btn-primary").text("OK").click(order))
		)
		.append($("<div/>", { id: "placeneworder-err", style: "color: red; display: none; font-weight: bold;" }))
	}));

	cli = app.getBusinessObject("DemoClient");
	cli.item = null;

	sup = app.getBusinessObject("DemoSupplier");
	sup.item = null;

	prd = app.getBusinessObject("DemoProduct");
	prd.item = null;

	getCli();
	getSup();
}

function getCli() {
	cli.search(function() {
		var d = $("<div/>");
		for (var i = 0; i < cli.list.length; i++) {
			var c = cli.list[i];
			var l = c.demoCliCode + " - " + c.demoCliFirstname + " " + c.demoCliLastname;
			d.append($("<p/>", { id: "placeneworder-cli_" + c.row_id }).addClass("obj").data("item", c).click(selCli).text(l));
		}
		$("#placeneworder-cli").append($ui.view.tools.panel({ title: "Select customer", content: d })).slideDown();
	});
}

function selCli() {
	cli.item = $(this).data("item");
	$("#placeneworder-cli").find("p").removeClass("sel");
	$("#placeneworder-cli_" + cli.item.row_id).addClass("sel");
	$("#placeneworder-selcli").empty().append($("<strong/>").text(cli.item.demoCliCode + " - " + cli.item.demoCliFirstname + " " + cli.item.demoCliLastname));
	if (prd.item) {
		$("#placeneworder-ok").attr("disabled", false);
		$("#placeneworder-qty").select();
	}
}

function getSup() {
	sup.search(function() {
		var d = $("<div/>");
		for (var i = 0; i < sup.list.length; i++) {
			var s = sup.list[i];
			var l = s.demoSupCode + " - " + s.demoSupName;
			d.append($("<p/>", { id: "placeneworder-sup_" + s.row_id }).addClass("obj").data("item", s).click(selSup).text(l));
		}
		$("#placeneworder-sup").append($ui.view.tools.panel({ title: "Select supplier", content: d })).slideDown();
	});
}

function selSup() {
	prd.item = null;
	sup.item = $(this).data("item");
	$("#placeneworder-sup").find("p").removeClass("sel");
	$("#placeneworder-sup_" + sup.item.row_id).addClass("sel");
	$("#placeneworder-prd").hide().empty();
	prd.search(function() {
		var d = $("<div/>");
		for (var i = 0; i < prd.list.length; i++) {
			var p = prd.list[i];
			var l = p.demoPrdReference + " - " + p.demoPrdName;
			d.append($("<p/>", { id: "placeneworder-prd_" + p.row_id }).addClass("obj").data("item", p).click(selPrd)
				.append($("<img/>", { src: "data:" + p.demoPrdPicture.mime + ";base64," + p.demoPrdPicture.content }).css("width", "50px"))
				.append($("<span/>").text(l)));
		}
		$("#placeneworder-prd").append($ui.view.tools.panel({ title: "Select product", content: d })).slideDown();
	}, { demoPrdSupId: sup.item.row_id }, { inlineDocs: true });
}

function selPrd() {
	prd.item = $(this).data("item")
	$("#placeneworder-prd").find("p").removeClass("sel");
	$("#placeneworder-prd_" + prd.item.row_id).addClass("sel");
	$("#placeneworder-selprd").empty()
		.append($("<img/>", { src: "data:" + prd.item.demoPrdPicture.mime + ";base64," + prd.item.demoPrdPicture.content })
		.append("<br/>").append($("<strong/>").text(prd.item.demoPrdReference + " " + prd.item.demoPrdName)));
	if (cli.item) {
		$("#placeneworder-ok").attr("disabled", false);
		$("#placeneworder-qty").select();
	}
	total();
}

function total() {
	$("#placeneworder-err").empty();
	var t = parseFloat(prd.item.demoPrdUnitPrice) * parseFloat($("#placeneworder-qty").val());
	$("#placeneworder-total").text(t.toFixed(2));
}

function order() {
	$("#placeneworder-err").empty().hide();
	var ord = app.getBusinessObject("DemoOrder");
	// ZZZ Get for create must be called to set default values
	ord.getForCreate(function() {
		ord.item.demoOrdCliId = cli.item.row_id;
		ord.item.demoOrdPrdId = prd.item.row_id;
		// ZZZ populate must be called to set all referred fields from client and product before creation
		ord.populate(function() {
			ord.item.demoOrdQuantity = $("#placeneworder-qty").val();
			ord.create(function() {
				$("#placeneworder").html("<p>Order created with number " + ord.item.demoOrdNumber + "<br/>Thank you !</p>");
			});
		});
	});
}

return { render: render };

})(jQuery);