//-----------------------------------------------------------
// Client side JavaScript for order agenda
//-----------------------------------------------------------

var DemoOrderAgenda = DemoOrderAgenda || (function() {

const debug = false;

let ord;

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
	const fc = parseInt($ui.grant.sysparams.FULLCALENDAR_VERSION) || 5;
	if (debug) console.log("FullCalendar version = " + fc);
	if (fc <= 3)
		calendar3();
	else if (fc == 4)
		calendar4();
	else
		calendar5();
}

// For legacy FullCalendar version 3
function calendar3() {
	$("#demoOrderAgenda").fullCalendar({
		header: {
			left: "prev,next today",
			center: "title",
			right: "month,agendaWeek"
		},
		timezone: $ui.grant.timezone || "local",
		locale: $ui.grant.langiso || "en",
		defaultView: "agendaWeek",
		editable: true,
		firstDay: 1,
		minTime: "08:00:00",
		maxTime: "20:00:00",
		businessHours: {
			dow: [ 1, 2, 3, 4, 5 ],
			start: "09:00",
			end: "18:00"
		},
		eventClick: function(e) {
			if (debug) console.log("Order " + e.id + " clicked");
			$ui.displayForm(null, "DemoOrder", e.id, { nav: "add" });
		},
		eventDrop: function(e) {
			const s = e.start.format( "YYYY-MM-DD HH:mm:ss");
			if (debug) console.log("Order " + e.id + " dropped to " + s);
			e.data.demoOrdDeliveryDate = s;
			ord.update(function() {
				e.data = ord.item;
				if (debug) console.log("Order " + e.data.demoOrdNumber + " delivery date updated to " + s);
			}, e.data);
		},
		events: function(start, end, tz, callback) {
			const f = "YYYY-MM-DD HH:mm:ss Z";
			const dmin = start.format(f);
			const dmax = end.format(f);
			if (debug) console.log("Calendar view range = " + dmin + " to " + dmax);
			ord.search(function() {
				if (debug) console.log(ord.list.length + " orders found between " + dmin + " and " + dmax);
				const status = ord.getField("demoOrdStatus");
				const evts = [];
				for (const item of ord.list) {
					if (item.demoOrdDeliveryDate !== "") { // ZZZ When using intervals empty values are included !
						const s = moment(item.demoOrdDeliveryDate);
						const e = s.add(2, "h");
						const st = status.getEnumItem(item.demoOrdStatus);
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

// For legacy FullCalendar version 4
function calendar4() {
	new FullCalendar.Calendar($("#demoOrderAgenda")[0], {
		plugins: [ 'dayGrid', 'timeGrid', 'list', 'interaction' ],
		header: {
			left: "prev,next today",
			center: "title",
			right: "dayGridMonth,timeGridWeek,dayGridDay,listWeek"
		},
		nowIndicator: true,
		timeZone: $ui.grant.timezone || "local",
		locale: $ui.grant.langiso || "en",
		defaultView: "timeGridWeek",
		editable: true,
		firstDay: 1,
		minTime: "08:00:00",
		maxTime: "20:00:00",
		businessHours: {
			dow: [ 1, 2, 3, 4, 5 ],
			start: "09:00",
			end: "18:00"
		},
		eventClick: function(info) {
			const id = info.event.id;
			if (debug) console.log("Order " + id + " clicked");
			$ui.displayForm(null, "DemoOrder", id, { nav: "add" });
		},
		eventDrop: function(info) {
			const s = moment(info.event.start).utc().format("YYYY-MM-DD HH:mm:ss");
			let d = info.event.extendedProps.data;
			if (debug) console.log("Order " + info.event.id + " dropped to " + s);
			d.demoOrdDeliveryDate = s;
			ord.update(function() {
				d = ord.item;
				if (debug) console.log("Order " + d.demoOrdNumber + " delivery date updated to " + s);
			}, d);
		},
		events: function(info, success, failure) {
			const start = moment(info.start);
			const end = moment(info.end);
			const f = "YYYY-MM-DD HH:mm:ss Z";
			const dmin = start.format(f);
			const dmax = end.format(f);
			if (debug) console.log("Calendar view range = " + dmin + " to " + dmax);
			ord.search(function() {
				if (debug) console.log(ord.list.length + " orders found between " + dmin + " and " + dmax);
				const status = ord.getField("demoOrdStatus");
				const evts = [];
				for (const item of ord.list) {
					if (item.demoOrdDeliveryDate !== "") { // ZZZ When using intervals empty values are included !
						const s = moment(item.demoOrdDeliveryDate);
						const e = s.add(2, "h");
						const st = status.getEnumItem(item.demoOrdStatus);
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

// For FullCalendar version 5
function calendar5() {
	new FullCalendar.Calendar($("#demoOrderAgenda")[0], {
		headerToolbar: {
			start: 'prev,next today',
			center: 'title',
			end: 'dayGridMonth,timeGridWeek,dayGridDay,listWeek'
		},
		nowIndicator: true,
		timeZone: $ui.grant.timezone || "local",
		locale: $ui.grant.langiso || "en",
		initialView: "timeGridWeek",
		editable: true,
		firstDay: 1,
		slotMinTime: "08:00:00",
		slotMaxTime: "20:00:00",
		businessHours: {
			daysOfWeek: [ 1, 2, 3, 4, 5 ],
			startTime: "09:00",
			endTime: "18:00"
		},
		eventClick: function(info) {
			const id = info.event.id;
			if (debug) console.log("Order " + id + " clicked");
			$ui.displayForm(null, "DemoOrder", id, { nav: "add" });
		},
		eventDrop: function(info) {
			const s = moment(info.event.start).utc().format("YYYY-MM-DD HH:mm:ss");
			let d = info.event.extendedProps.data;
			if (debug) console.log("Order " + info.event.id + " dropped to " + s);
			d.demoOrdDeliveryDate = s;
			ord.update(function() {
				d = ord.item;
				if (debug) console.log("Order " + d.demoOrdNumber + " delivery date updated to " + s);
			}, d);
		},
		events: function(info, success, failure) {
			const start = moment(info.start);
			const end = moment(info.end);
			const f = "YYYY-MM-DD HH:mm:ss Z";
			const dmin = start.format(f);
			const dmax = end.format(f);
			if (debug) console.log("Calendar view range = " + dmin + " to " + dmax);
			ord.search(function() {
				if (debug) console.log(ord.list.length + " orders found between " + dmin + " and " + dmax);
				const status = ord.getField("demoOrdStatus");
				const evts = [];
				for (const item of ord.list) {
					if (item.demoOrdDeliveryDate !== "") { // ZZZ When using intervals empty values are included !
						const s = moment(item.demoOrdDeliveryDate);
						const e = s.add(2, "h");
						const st = status.getEnumItem(item.demoOrdStatus);
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

})();
