//-----------------------------------------------------------
// Client side JavaScript for place new order external object
//-----------------------------------------------------------

var DemoPlaceNewOrder = typeof DemoPlaceNewOrder !== "undefined" ? DemoPlaceNewOrder : (function($) {

var cli, sup, prd, ord;

function render() {
	// Override default error handler
	var ajax = $ui.getAjax();
	ajax.setErrorHandler(function(err) {
		$("#placeneworder-err").append($("<p/>").text(ajax.getErrorMessage(err))).show();
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

	getCli();
	getSup();
}

function getCli() {
	$ui.getUIObject("DemoClient", "pno_DemoClient", function(c) {
		cli = c;
		cli.item = null;
		console.log(cli);
		cli.search(function() {
			var div = $("<div/>");
			for (var i = 0; i < cli.list.length; i++) {
				var item = cli.list[i];
				var label = item.demoCliCode + " - " + item.demoCliFirstname + " " + item.demoCliLastname;
				div.append($("<p/>", { id: "placeneworder-cli_" + item.row_id }).addClass("obj").data("item", item).click(selCli).text(label));
			}
			$("#placeneworder-cli").append($ui.view.tools.panel({ title: "Select customer", content: div })).slideDown();
		});
	});
}

function selCli() {
	cli.item = $(this).data("item");
	$("#placeneworder-cli").find("p").removeClass("sel");
	$("#placeneworder-cli_" + cli.item.row_id).addClass("sel");
	$("#placeneworder-selcli").empty().append($("<strong/>").text(cli.item.demoCliCode + " - " + cli.item.demoCliFirstname + " " + cli.item.demoCliLastname));
	if (prd && prd.item) {
		$("#placeneworder-ok").attr("disabled", false);
		$("#placeneworder-qty").select();
	}
}

function getSup() {
	$ui.getUIObject("DemoSupplier", "pno_DemoSupplier", function(s) {
		sup = s;
		sup.item = null;
		sup.search(function() {
			var div = $("<div/>");
			for (var i = 0; i < sup.list.length; i++) {
				var item = sup.list[i];
				var label = item.demoSupCode + " - " + item.demoSupName;
				div.append($("<p/>", { id: "placeneworder-sup_" + item.row_id }).addClass("obj").data("item", item).click(selSup).text(label));
			}
			$("#placeneworder-sup").append($ui.view.tools.panel({ title: "Select supplier", content: div })).slideDown();
		});
	});
}

function selSup() {
	sup.item = $(this).data("item");
	$("#placeneworder-sup").find("p").removeClass("sel");
	$("#placeneworder-sup_" + sup.item.row_id).addClass("sel");
	$("#placeneworder-prd").hide().empty();
	$ui.getUIObject("DemoProduct", "pno_DemoProduct", function(p) {
		prd = p;
		prd.item = null;
		prd.search(function() {
			var div = $("<div/>");
			for (var i = 0; i < prd.list.length; i++) {
				var item = prd.list[i];
				var label = item.demoPrdReference + " - " + item.demoPrdName;
				div.append($("<p/>", { id: "placeneworder-prd_" + item.row_id }).addClass("obj").data("item", item).click(selPrd)
					.append($("<img/>", { src: "data:" + item.demoPrdPicture.mime + ";base64," + item.demoPrdPicture.content }).css("width", "50px"))
					.append($("<span/>").text(label)));
			}
			$("#placeneworder-prd").append($ui.view.tools.panel({ title: "Select product", content: div })).slideDown();
		}, { demoPrdSupId: sup.item.row_id }, { inlineDocs: true });
	});
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
	$ui.getUIObject("DemoOrder", "pno_DemoOrder", function(o) {
		ord = o;
		ord.item = null;
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
	});
}

return { render: render };

})(jQuery);