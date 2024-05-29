//-----------------------------------------------------------
// Client side JavaScript for place new order external object
//-----------------------------------------------------------

var DemoPlaceNewOrder = DemoPlaceNewOrder || (function() {

let cli, sup, prd, ord;

function render() {
	// Override default error handler
	const ajax = $ui.getAjax();
	ajax.setErrorHandler(function(err) {
		$("#DemoPlaceNewOrder-err").append($("<p/>").text(ajax.getErrorMessage(err))).show();
	});

	$("#DemoPlaceNewOrder-ord").append($ui.view.tools.panel({ title: "Order", content: $("<div/>")
		.append($("<div/>").append("Selected customer:"))
		.append($("<div/>", { id: "DemoPlaceNewOrder-selcli" }).append("&lt;none&gt;")).append("<br/>")
		.append($("<div/>").append("Selected product:"))
		.append($("<div/>", { id: "DemoPlaceNewOrder-selprd" }).append("&lt;none&gt;")).append("<br/>")
		.append($("<div/>")
			.append($("<input/>", { id: "DemoPlaceNewOrder-qty", type: "text"}).val(1).change(total))
			.append("&nbsp;Total:&nbsp;")
			.append($("<span/>", { id: "DemoPlaceNewOrder-total" }).append("0.00"))
			.append("&nbsp;&euro;&nbsp;")
			.append($("<button/>", { id: "DemoPlaceNewOrder-ok", disabled: true }).addClass("btn btn-primary").text("OK").click(order))
		)
		.append($("<div/>", { id: "DemoPlaceNewOrder-err", style: "color: red; display: none; font-weight: bold;" }))
	}));

	getCli();
	getSup();
	if (prd) prd.item = undefined;
}


function getCli() {
	$ui.getUIObject("DemoClient", "pno_DemoClient", function(c) {
		cli = c;
		cli.item = null;
		console.log(cli);
		cli.search(function() {
			const div = $("<div/>");
			for (const item of cli.list) {
				const label = item.demoCliCode + " - " + item.demoCliFirstname + " " + item.demoCliLastname;
				div.append($("<p/>", { id: "DemoPlaceNewOrder-cli_" + item.row_id }).addClass("obj").data("item", item).click(selCli).text(label));
			}
			$("#DemoPlaceNewOrder-cli").append($ui.view.tools.panel({ title: "Select customer", content: div })).slideDown();
		});
	});
}

function selCli() {
	cli.item = $(this).data("item");
	$("#DemoPlaceNewOrder-cli").find("p").removeClass("sel");
	$("#DemoPlaceNewOrder-cli_" + cli.item.row_id).addClass("sel");
	$("#DemoPlaceNewOrder-selcli").empty().append($("<strong/>").text(cli.item.demoCliCode + " - " + cli.item.demoCliFirstname + " " + cli.item.demoCliLastname));
	if (prd?.item) {
		$("#DemoPlaceNewOrder-ok").attr("disabled", false);
		$("#DemoPlaceNewOrder-qty").select();
	}
}

function getSup() {
	$ui.getUIObject("DemoSupplier", "pno_DemoSupplier", function(s) {
		sup = s;
		sup.item = null;
		sup.search(function() {
			const div = $("<div/>");
			for (const item of sup.list) {
				const label = item.demoSupCode + " - " + item.demoSupName;
				div.append($("<p/>", { id: "DemoPlaceNewOrder-sup_" + item.row_id }).addClass("obj").data("item", item).click(selSup).text(label));
			}
			$("#DemoPlaceNewOrder-sup").append($ui.view.tools.panel({ title: "Select supplier", content: div })).slideDown();
		});
	});
}

function selSup() {
	sup.item = $(this).data("item");
	$("#DemoPlaceNewOrder-sup").find("p").removeClass("sel");
	$("#DemoPlaceNewOrder-sup_" + sup.item.row_id).addClass("sel");
	$("#DemoPlaceNewOrder-prd").hide().empty();
	$ui.getUIObject("DemoProduct", "pno_DemoProduct", function(p) {
		prd = p;
		prd.item = null;
		prd.search(function() {
			const div = $("<div/>");
			for (const item of prd.list) {
				const label = item.demoPrdReference + " - " + item.demoPrdName;
				div.append($("<p/>", { id: "DemoPlaceNewOrder-prd_" + item.row_id }).addClass("obj").data("item", item).click(selPrd)
					.append($("<img/>", { src: "data:" + item.demoPrdPicture.mime + ";base64," + item.demoPrdPicture.content }).css("width", "50px"))
					.append($("<span/>").text(label)));
			}
			$("#DemoPlaceNewOrder-prd").append($ui.view.tools.panel({ title: "Select product", content: div })).slideDown();
		}, { demoPrdSupId: sup.item.row_id }, { inlineDocs: true });
	});
}

function selPrd() {
	prd.item = $(this).data("item");
	$("#DemoPlaceNewOrder-prd").find("p").removeClass("sel");
	$("#DemoPlaceNewOrder-prd_" + prd.item.row_id).addClass("sel");
	$("#DemoPlaceNewOrder-selprd").empty()
		.append($("<img/>", { src: "data:" + prd.item.demoPrdPicture.mime + ";base64," + prd.item.demoPrdPicture.content })
		.append("<br/>").append($("<strong/>").text(prd.item.demoPrdReference + " " + prd.item.demoPrdName)));
	if (cli?.item) {
		$("#DemoPlaceNewOrder-ok").attr("disabled", false);
		$("#DemoPlaceNewOrder-qty").select();
	}
	total();
}

function total() {
	$("#DemoPlaceNewOrder-err").empty();
	const t = parseFloat(prd.item.demoPrdUnitPrice) * parseFloat($("#DemoPlaceNewOrder-qty").val());
	$("#DemoPlaceNewOrder-total").text(t.toFixed(2));
}

function order() {
	$("#DemoPlaceNewOrder-err").empty().hide();
	$ui.getUIObject("DemoOrder", "pno_DemoOrder", function(o) {
		ord = o;
		ord.item = null;
		// ZZZ Get for create must be called to set default values
		ord.getForCreate(function() {
			ord.item.demoOrdCliId = cli.item.row_id;
			ord.item.demoOrdPrdId = prd.item.row_id;
			// ZZZ populate must be called to set all referred fields from client and product before creation
			ord.populate(function() {
				ord.item.demoOrdQuantity = $("#DemoPlaceNewOrder-qty").val();
				ord.create(function() {
					$("#DemoPlaceNewOrder").html("<p>Order created with number " + ord.item.demoOrdNumber + "<br/>Thank you !</p>");
					$ui.view.notify({ type: "create", object: ord, rowId: ord.item.row_id }); // Notify UI components (e.g. menu)
				});
			});
		});
	});
}

return { render: render };

})();