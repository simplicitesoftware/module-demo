//-----------------------------------------------------------
// Client side JavaScript for place new this.order external object
//-----------------------------------------------------------

class DemoPlaceNewOrder extends Simplicite.UI.ExternalObject {
	/** @override */
	async render(_params, _data) {
		// Override default error handler
		const ajax = $ui.getAjax();
		ajax.setErrorHandler(err => {
			$('#demoplaceneworder-err').append($('<p/>').text(ajax.getErrorMessage(err))).show();
		});

		const self = this;
		$('#demoplaceneworder-ord').append($ui.view.tools.panel({ title: 'Order', content: $('<div/>')
			.append($('<div/>').append('Selected customer:'))
			.append($('<div/>', { id: 'demoplaceneworder-selcli' }).append('&lt;none&gt;')).append('<br/>')
			.append($('<div/>').append('Selected product:'))
			.append($('<div/>', { id: 'demoplaceneworder-selprd' }).append('&lt;none&gt;')).append('<br/>')
			.append($('<div/>')
				.append($('<input/>', { id: 'demoplaceneworder-qty', type: 'text'}).val(1).on('change', () => self.calculateTotal()))
				.append('&nbsp;Total:&nbsp;')
				.append($('<span/>', { id: 'demoplaceneworder-total' }).append('0.00'))
				.append('&nbsp;&euro;&nbsp;')
				.append($('<button/>', { id: 'demoplaceneworder-ok', disabled: true }).addClass('btn btn-primary').text('OK').on('click', () => this.placeOrder()))
			)
			.append($('<div/>', { id: 'demoplaceneworder-err', style: 'color: red; display: none; font-weight: bold;' }))
		}));

		this.getClients();
		this.getSuppliers();
		if (this.prd)
			this.prd.item = undefined;
	}

	/**
	 * Get all customers
 	 */
	getClients() {
		$ui.getUIObject('DemoClient', 'pno_DemoClient', obj => {
			this.cli = obj;
			this.cli.item = null;
			const click = evt => this.selectClient(evt.data);
			this.cli.search(list => {
				const div = $('<div/>');
				for (const item of list) {
					const label = `${item.demoCliCode} - ${item.demoCliFirstname} ${item.demoCliLastname}`;
					div.append($('<p/>', { id: `demoplaceneworder-cli-${item.row_id}` }).addClass('obj').on('click', item, click).text(label));
				}
				$('#demoplaceneworder-cli').append($ui.view.tools.panel({ title: 'Select customer', content: div })).slideDown();
			});
		});
	}

	/**
	 * Select one customer
	 * @param {object} item Customer item
 	 */
	selectClient(item) {
		this.cli.item = item;
		$('#demoplaceneworder-cli').find('p').removeClass('sel');
		$(`#demoplaceneworder-cli-${this.cli.item.row_id}`).addClass('sel');
		$('#demoplaceneworder-selcli').empty().append($('<strong/>').text(`${this.cli.item.demoCliCode} - ${this.cli.item.demoCliFirstname} ${this.cli.item.demoCliLastname}`));
		if (this.prd?.item) {
			$('#demoplaceneworder-ok').attr('disabled', false);
			$('#demoplaceneworder-qty').select();
		}
	}

	/**
	 * Get all suppliers
 	 */
	getSuppliers() {
		$ui.getUIObject('DemoSupplier', 'pno_DemoSupplier', obj => {
			this.sup = obj;
			this.sup.item = null;
			const click = evt => this.selectSupplier(evt.data);
			this.sup.search(list => {
				const div = $('<div/>');
				for (const item of list) {
					const label = `${item.demoSupCode} - ${item.demoSupName}`;
					div.append($('<p/>', { id: `demoplaceneworder-sup-${item.row_id}` }).addClass('obj').on('click', item, click).text(label));
				}
				$('#demoplaceneworder-sup').append($ui.view.tools.panel({ title: 'Select supplier', content: div })).slideDown();
			});
		});
	}

	/**
	 * Select one supplier
	 * @param {object} item Supplier item
 	 */
	selectSupplier(item) {
		this.sup.item = item;
		$('#demoplaceneworder-sup').find('p').removeClass('sel');
		$(`#demoplaceneworder-sup-${this.sup.item.row_id}`).addClass('sel');
		$('#demoplaceneworder-prd').hide().empty();
		this.getProducts(this.sup.item.row_id);
	}

	/**
	 * Get all products for the specified supplier ID
	 * @param {string} supId Supplier ID
 	 */
	getProducts(supId) {
		$ui.getUIObject('DemoProduct', 'pno_DemoProduct', obj => {
			this.prd = obj;
			this.prd.item = null;
			const click = evt => this.selectProduct(evt.data);
			this.prd.search(list => {
				const div = $('<div/>');
				for (const item of list) {
					const label = `${item.demoPrdReference} - ${item.demoPrdName}`;
					div.append($('<p/>', { id: `demoplaceneworder-prd-${item.row_id}` }).addClass('obj').on('click', item, click)
						.append($('<img/>', { src: `data:${item.demoPrdPicture.mime};base64,${item.demoPrdPicture.content}` }).css('width', '50px'))
						.append($('<span/>').text(label)));
				}
				$('#demoplaceneworder-prd').append($ui.view.tools.panel({ title: 'Select product', content: div })).slideDown();
			}, { demoPrdSupId: supId }, { inlineDocs: true });
		});
	}
	
	/**
	 * Select one product
	 * @param {object} item Product item
 	 */
	selectProduct(item) {
		this.prd.item = item;
		$('#demoplaceneworder-prd').find('p').removeClass('sel');
		$(`#demoplaceneworder-prd-${this.prd.item.row_id}`).addClass('sel');
		$('#demoplaceneworder-selprd').empty()
			.append($('<img/>', { src: `data:${this.prd.item.demoPrdPicture.mime};base64,${this.prd.item.demoPrdPicture.content}` }))
			.append('<br/>').append($('<strong/>').text(`${this.prd.item.demoPrdReference} ${this.prd.item.demoPrdName}`));
		if (this.cli?.item) {
			$('#demoplaceneworder-ok').attr('disabled', false);
			$('#demoplaceneworder-qty').select();
		}
		this.calculateTotal();
	}

	/**
	 * (Re)caculate the order total
 	 */
	calculateTotal() {
		$('#demoplaceneworder-err').empty();
		const t = parseFloat(this.prd.item.demoPrdUnitPrice) * parseFloat($('#demoplaceneworder-qty').val());
		$('#demoplaceneworder-total').text(t.toFixed(2));
	}

	/**
	 * Place the order
 	 */
	placeOrder() {
		$('#demoplaceneworder-err').empty().hide();
		$ui.getUIObject('DemoOrder', 'pno_DemoOrder', obj => {
			this.ord = obj;
			this.ord.item = null;
			// ZZZ Get for create must be called to set default values
			this.ord.getForCreate(() => {
				this.ord.item.demoOrdCliId = this.cli.item.row_id;
				this.ord.item.demoOrdPrdId = this.prd.item.row_id;
				// ZZZ populate must be called to set all referred fields from this.client and product before creation
				this.ord.populate(() => {
					this.ord.item.demoOrdQuantity = $('#demoplaceneworder-qty').val();
					this.ord.create(() => {
						$('#demoplaceneworder').html(`<p>Order created with number ${this.ord.item.demoOrdNumber}<br/>Thank you !</p>`);
						$ui.view.notify({ type: 'create', object: this.ord, rowId: this.ord.item.row_id }); // Notify UI components (e.g. menu)
					});
				});
			});
		});
	}
}