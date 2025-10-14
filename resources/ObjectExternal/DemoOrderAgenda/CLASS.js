//-----------------------------------------------------------
// Client side JavaScript for order agenda
//-----------------------------------------------------------

/* global moment, FullCalendar */

Simplicite.UI.ExternalObjects.DemoOrderAgenda = class extends Simplicite.UI.ExternalObject {
	/** @override */
	async render(_params, _data) {
		this.debug = false;
		$ui.loadCalendar(() => {
			$ui.getUIObject('DemoOrder', 'agenda_DemoOrder', obj => {
				this.ord = obj;
				this.ord.getMetaData().then(_ => this.calendar());
			});
		});
	}

	/** Choose appropriate calendar function for current FullCalendar version */
	calendar() {
		const fc = parseInt($ui.grant.sysparams.FULLCALENDAR_VERSION) || 5;
		if (this.debug) $console.info(`FullCalendar version = ${fc}`);
		if (fc < 4)
			$('#demoOrderAgenda').text(`Fullcalendar version ${fc} is not supported`);
		else if (fc == 4)
			this.calendar4();
		else
			this.calendar5();
	}

	/** Calendar for legacy FullCalendar version 4 */
	calendar4() {
		const self = this;
		new FullCalendar.Calendar($('#demoOrderAgenda')[0], {
			plugins: [ 'dayGrid', 'timeGrid', 'list', 'interaction' ],
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'dayGridMonth,timeGridWeek,dayGridDay,listWeek'
			},
			nowIndicator: true,
			timeZone: $ui.grant.timezone || 'local',
			locale: $ui.grant.langiso || 'en',
			defaultView: 'timeGridWeek',
			editable: true,
			firstDay: 1,
			minTime: '08:00:00',
			maxTime: '20:00:00',
			businessHours: {
				dow: [ 1, 2, 3, 4, 5 ],
				start: '09:00',
				end: '18:00'
			},
			eventClick: info => {
				const id = info.event.id;
				if (self.debug) $console.info(`Order ${id} clicked`);
				$ui.displayForm(null, 'DemoOrder', id, { nav: 'add' });
			},
			eventDrop: info => {
				const s = moment(info.event.start).utc().format('YYYY-MM-DD HH:mm:ss');
				let d = info.event.extendedProps.data;
				if (self.debug) $console.info(`Order ${info.event.id} dropped to ${s}`);
				d.demoOrdDeliveryDate = s;
				this.ord.update(d).then(_ => {
					d = self.ord.item;
					if (self.debug) $console.info(`Order ${d.demoOrdNumber} delivery date updated to ${s}`);
				});
			},
			events: (info, success, _failure) => {
				const start = moment(info.start);
				const end = moment(info.end);
				const f = 'YYYY-MM-DD HH:mm:ss Z';
				const dmin = start.format(f);
				const dmax = end.format(f);
				if (self.debug) $console.info(`Calendar view range = ${dmin} to ${dmax}`);
				self.ord.search(
					{ demoOrdDeliveryDate: `${dmin};${dmax}`, demoOrdStatus: 'P;V;D' },
					{ inlineDocs: false }
				).then(list => {
					if (self.debug) $console.info(`${list.length} orders found between ${dmin} and ${dmax}`);
					const status = self.ord.getField('demoOrdStatus');
					const evts = [];
					for (const item of list) {
						if (item.demoOrdDeliveryDate !== '') { // ZZZ When using intervals empty values are included !
							const s = moment(item.demoOrdDeliveryDate);
							const e = s.add(2, 'h');
							const st = status.getEnumItem(item.demoOrdStatus);
							evts.push({
								id: item.row_id,
								data: item,
								title: `${item.demoOrdNumber}: ${item.demoOrdCliId__demoCliCode} / ${item.demoOrdPrdId__demoPrdReference}\n${status.getDisplay()}: ${status.displayValue(item.demoOrdStatus)}`,
								start: s.toDate(),
								end: e.toDate(),
								editable: item.demoOrdStatus == 'P' || item.demoOrdStatus == 'V',
								durationEditable: false,
								color: st.bgcolor,
								borderColor: st.bgcolor,
								textColor: st.color
							});
						}
					}
					if (self.debug) $console.info(`${evts.length} orders displayed between ${dmin} and ${dmax}`);
					success(evts);
				});
			}
		}).render();
	}

	/** Calendar for FullCalendar version 5 */
	calendar5() {
		const self = this;
		new FullCalendar.Calendar($('#demoOrderAgenda')[0], {
			headerToolbar: {
				start: 'prev,next today',
				center: 'title',
				end: 'dayGridMonth,timeGridWeek,dayGridDay,listWeek'
			},
			nowIndicator: true,
			timeZone: $ui.grant.timezone || 'local',
			locale: $ui.grant.langiso || 'en',
			initialView: 'timeGridWeek',
			editable: true,
			firstDay: 1,
			slotMinTime: '08:00:00',
			slotMaxTime: '20:00:00',
			businessHours: {
				daysOfWeek: [ 1, 2, 3, 4, 5 ],
				startTime: '09:00',
				endTime: '18:00'
			},
			eventClick: info => {
				const id = info.event.id;
				if (self.debug) $console.info(`Order ${id} clicked`);
				$ui.displayForm(null, 'DemoOrder', id, { nav: 'add' });
			},
			eventDrop: info => {
				const s = moment(info.event.start).utc().format('YYYY-MM-DD HH:mm:ss');
				let d = info.event.extendedProps.data;
				if (self.debug) $console.info(`Order ${info.event.id} dropped to ${s}`);
				d.demoOrdDeliveryDate = s;
				self.ord.update(d).then(_ => {
					d = self.ord.item;
					if (self.debug) $console.info(`Order ${d.demoOrdNumber} delivery date updated to ${s}`);
				});
			},
			events: (info, success, _failure) => {
				const start = moment(info.start);
				const end = moment(info.end);
				const f = 'YYYY-MM-DD HH:mm:ss Z';
				const dmin = start.format(f);
				const dmax = end.format(f);
				if (self.debug) $console.info(`Calendar view range = ${dmin} to ${dmax}`);
				self.ord.search(
					{ demoOrdDeliveryDate: `${dmin};${dmax}`, demoOrdStatus: 'P;V;D' },
					{ inlineDocs: false }
				).then(list => {
					if (self.debug) $console.info(`${list.length} orders found between ${dmin} and ${dmax}`);
					const status = self.ord.getField('demoOrdStatus');
					const evts = [];
					for (const item of list) {
						if (item.demoOrdDeliveryDate !== '') { // ZZZ When using intervals empty values are included !
							const s = moment(item.demoOrdDeliveryDate);
							const e = s.add(2, 'h');
							const st = status.getEnumItem(item.demoOrdStatus);
							evts.push({
								id: item.row_id,
								data: item,
								title: `${item.demoOrdNumber}: ${item.demoOrdCliId__demoCliCode} / ${item.demoOrdPrdId__demoPrdReference}\n${status.getDisplay()}: ${status.displayValue(item.demoOrdStatus)}`,
								start: s.toDate(),
								end: e.toDate(),
								editable: item.demoOrdStatus == 'P' || item.demoOrdStatus == 'V',
								durationEditable: false,
								color: st.bgcolor,
								borderColor: st.bgcolor,
								textColor: st.color
							});
						}
					}
					if (self.debug) $console.info(`${evts.length} orders displayed between ${dmin} and ${dmax}`);
					success(evts);
				});
			}
		}).render();
	}
};