//-----------------------------------------------------------
// Client side JavaScript for order agenda
//-----------------------------------------------------------

var DemoOrderAgenda = DemoOrderAgenda || (function($) {

var ord, debug = false;

function render(url) {
	$ui.loadCalendar(function() {
		$ui.getUIObject("DemoOrder", "agenda_DemoOrder", function(o) {
			ord = o;
			ord.getMetaData(calendar);
		});
	});
}

// Choose appropriate calendar function for current FullCalendar version
function calendar() {
	var fc = parseInt($ui.grant.sysparams.FULLCALENDAR_VERSION) || 3;
	if (debug) console.log("FullCalendar version = " + fc);
	if (fc == 3) calendar3(); else calendar4();
}

// For FullCalendar version 3
function calendar3() {
	$("#demoOrderAgenda").fullCalendar({
		header: { left: "prev,next today", center: "title", right: "month,agendaWeek" },
		timezone: $ui.grant.timezone || "local",
		locale: $ui.grant.langiso || "en",
		defaultView: "agendaWeek",
		editable: true,
		firstDay: 1, minTime: "08:00:00", maxTime: "20:00:00",
		businessHours: { dow: [ 1, 2, 3, 4, 5 ], start: "09:00", end: "18:00" },
		eventClick: function(e) {
			if (debug) console.log("Order " + e.id + " clicked");
			$ui.displayForm(null, "DemoOrder", e.id, { nav: "add" });
		},
		eventDrop: function(e) {
			var s = e.start.format( "YYYY-MM-DD HH:mm:ss");
			if (debug) console.log("Order " + e.id + " dropped to " + s);
			e.data.demoOrdDeliveryDate = s;
			ord.update(function() {
				e.data = ord.item;
				if (debug) console.log("Order " + e.data.demoOrdNumber + " delivery date updated to " + s);
			}, e.data);
		},
		events: function(start, end, tz, callback) {
			var f = "YYYY-MM-DD HH:mm:ss Z";
			var dmin = start.format(f);
			var dmax = end.format(f);
			if (debug) console.log("Calendar view range = " + dmin + " to " + dmax);
			ord.search(function() {
				if (debug) console.log(ord.list.length + " orders found between " + dmin + " and " + dmax);
				var status = ord.getField("demoOrdStatus");
				var evts = [];
				for (var i = 0; i < ord.list.length; i++) {
					var item = ord.list[i];
					if (item.demoOrdDeliveryDate !== "") { // ZZZ When using intervals empty values are included !
						var s = moment(item.demoOrdDeliveryDate);
						var e = s.add(2, "h");
						var st = status.getEnumItem(item.demoOrdStatus);
						evts.push({
							id: item.row_id,
							data: item,
							title: item.demoOrdNumber + ":" + item.demoOrdCliId__demoCliCode + " / " + item.demoOrdPrdId__demoPrdReference + "\n" + status.getDisplay() + ": " + status.displayValue(item.demoOrdStatus),
							start: s.toDate(),
							end: e.toDate(),
							editable: item.demoOrdStatus == "P" || item.demoOrdStatus == "V",
							durationEditable: false,
							color: st.bgcolor,
							borderColor: st.bgcolor,
							textColor: st.color
						});
					}
				}
				if (debug) console.log(evts.length + " orders displayed between " + dmin + " and " + dmax);
				callback(evts);
			}, { demoOrdDeliveryDate: dmin + ";" + dmax, demoOrdStatus: "P;V;D" }, { inlineDocs: false });
		}
	});
}

// For FullCalendar version 4
function calendar4() {
	new FullCalendar.Calendar($("#demoOrderAgenda")[0], {
		plugins: [ 'dayGrid', 'timeGrid', 'list', 'interaction' ],
		header: { left: "prev,next today", center: "title", right: "dayGridMonth,timeGridWeek,dayGridDay,listWeek" },
		nowIndicator: true,
		timezone: $ui.grant.timezone || "local",
		locale: $ui.grant.langiso || "en",
		defaultView: "timeGridWeek",
		editable: true,
		firstDay: 1, minTime: "08:00:00", maxTime: "20:00:00",
		businessHours: { dow: [ 1, 2, 3, 4, 5 ], start: "09:00", end: "18:00" },
		eventClick: function(info) {
			var id = info.event.id;
			if (debug) console.log("Order " + id + " clicked");
			$ui.displayForm(null, "DemoOrder", id, { nav: "add" });
		},
		eventDrop: function(info) {
			var s = moment(info.event.start).format("YYYY-MM-DD HH:mm:ss");
			var d = info.event.extendedProps.data;
			if (debug) console.log("Order " + info.event.id + " dropped to " + s);
			d.demoOrdDeliveryDate = s;
			ord.update(function() {
				d = ord.item;
				if (debug) console.log("Order " + d.demoOrdNumber + " delivery date updated to " + s);
			}, d);
		},
		events: function(info, success, failure) {
			var start = moment(info.start);
			var end = moment(info.end);
			var f = "YYYY-MM-DD HH:mm:ss Z";
			var dmin = start.format(f);
			var dmax = end.format(f);
			if (debug) console.log("Calendar view range = " + dmin + " to " + dmax);
			ord.search(function() {
				if (debug) console.log(ord.list.length + " orders found between " + dmin + " and " + dmax);
				var status = ord.getField("demoOrdStatus");
				var evts = [];
				for (var i = 0; i < ord.list.length; i++) {
					var item = ord.list[i];
					if (item.demoOrdDeliveryDate !== "") { // ZZZ When using intervals empty values are included !
						var s = moment(item.demoOrdDeliveryDate);
						var e = s.add(2, "h");
						var st = status.getEnumItem(item.demoOrdStatus);
						evts.push({
							id: item.row_id,
							data: item,
							title: item.demoOrdNumber + ": " + item.demoOrdCliId__demoCliCode + " / " + item.demoOrdPrdId__demoPrdReference + "\n" + status.getDisplay() + ": " + status.displayValue(item.demoOrdStatus),
							start: s.toDate(),
							end: e.toDate(),
							editable: item.demoOrdStatus == "P" || item.demoOrdStatus == "V",
							durationEditable: false,
							color: st.bgcolor,
							borderColor: st.bgcolor,
							textColor: st.color
						});
					}
				}
				if (debug) console.log(evts.length + " orders displayed between " + dmin + " and " + dmax);
				success(evts);
			}, { demoOrdDeliveryDate: dmin + ";" + dmax, demoOrdStatus: "P;V;D" }, { inlineDocs: false });
		}
	}).render();
}

return { render: render };

})(jQuery);
