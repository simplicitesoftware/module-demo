//-----------------------------------------------------------
// Client side JavaScript for place new order external object
//-----------------------------------------------------------

DemoPlaceNewOrder = (function($) {

var $s;
var $ui;
var cli, sup, prd;

function start() {
	$s = new Simplicite.Ajax();
	$s.setErrorHandler(function(err) {
		$("#orderr").append($("<p/>").text($s.getErrorMessage(err))).show();
	});
	cli = $s.getBusinessObject("DemoClient");
	cli.item = null;
	sup = $s.getBusinessObject("DemoSupplier");
	sup.item = null;
	prd = $s.getBusinessObject("DemoProduct");
	prd.item = null;

	$ui = Simplicite.UI;

	getCli();
	getSup();
}

function getCli() {
	cli.search(function() {
		var div = $("<div>").addClass("workareacontent");
		for (var i = 0; i < cli.list.length; i++) {
			var c = cli.list[i];
			var l = c.demoCliCode + " - " + c.demoCliFirstname + " " + c.demoCliLastname;
			div.append($("<p/>", { id: "cli_" + c.row_id }).addClass("obj").data("item", c).click(selCli).append(l));
		}
		$("#cli")
			.append($("<div/>").addClass("workareatitle").append("Select customer"))
			.append($("<div/>").addClass("workarea").append(div))
			.slideDown();
	});
}

function selCli() {
	cli.item = $(this).data("item");
	$("#cli").find("p").removeClass("sel");
	$("#cli_" + cli.item.row_id).addClass("sel");
	$("#selcli").empty().append($("<strong/>").append(cli.item.demoCliCode + " - " + cli.item.demoCliFirstname + " " + cli.item.demoCliLastname));
	if (prd.item) { $("#ok").attr("disabled", false); $("#qty").select(); }
}

function getSup() {
	sup.search(function() {
		var div = $("<div>").addClass("workareacontent");
		for (var i = 0; i < sup.list.length; i++) {
			var s = sup.list[i];
			var l = s.demoSupCode + " - " + s.demoSupName;
			div.append($("<p/>", { id: "sup_" + s.row_id }).addClass("obj").data("item", s).click(selSup).append(l));
		}
		$("#sup")
			.append($("<div/>").addClass("workareatitle").append("Select supplier"))
			.append($("<div/>").addClass("workarea").append(div))
			.slideDown();
	});
}

function selSup() {
	prd.item = null;
	sup.item = $(this).data("item");
	$("#sup").find("p").removeClass("sel");
	$("#sup_" + sup.item.row_id).addClass("sel");
	$("#prd").hide().empty();
	prd.search(function() {
		var div = $("<div>").addClass("workareacontent");
		for (var i = 0; i < prd.list.length; i++) {
			var p = prd.list[i];
			var l = p.demoPrdReference + " - " + p.demoPrdName;
			div.append($("<p/>", { id: "prd_" + p.row_id }).addClass("obj").data("item", p).click(selPrd).append($("<img/>", { src: "data:" + p.demoPrdPicture.mime + ";base64," + p.demoPrdPicture.content }).css("width", "50px")).append(l));
		}
		$("#prd")
			.append($("<div/>").addClass("workareatitle").append("Select product"))
			.append($("<div/>").addClass("workarea").append(div))
			.slideDown();
	}, { demoPrdSupId: sup.item.row_id }, { inlineDocs: true });
}

function selPrd() {
	prd.item = $(this).data("item")
	$("#prd").find("p").removeClass("sel");
	$("#prd_" + prd.item.row_id).addClass("sel");
	$("#selprd").empty()
		.append($("<img/>", { src: "data:" + prd.item.demoPrdPicture.mime + ";base64," + prd.item.demoPrdPicture.content })
		.append("<br/>").append($("<strong/>").append(prd.item.demoPrdReference + " " + prd.item.demoPrdName)));
	if (cli.item) { $("#ok").attr("disabled", false); $("#qty").select(); }
	total();
}

function total() {
	$("#orderr").empty();
	var t = parseFloat(prd.item.demoPrdUnitPrice) * parseFloat($("#qty").val());
	$("#total").text(t.toFixed(2));
}

function order() {
	$("#orderr").empty().hide();
	var ord = $s.getBusinessObject("DemoOrder");
	// ZZZ Get for create must be called to set default values
	ord.getForCreate(function() {
		ord.item.demoOrdCliId = cli.item.row_id;
		ord.item.demoOrdPrdId = prd.item.row_id;
		// ZZZ populate must be called to set all referred fields from client and product before creation
		ord.populate(function() {
			ord.item.demoOrdQuantity = $("#qty").val();
			ord.create(function() {
				$("#main").html("<p>Order created with number " + ord.item.demoOrdNumber + "<br/>Thank you !</p>");
				setTimeout(function () { location.reload(); }, 2000);
			});
		});
	});
}

return { start: start, order: order, total: total };

})(jQuery);