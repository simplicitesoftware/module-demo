//-----------------------------------------------------------
// Client side JavaScript for place new order external object
//-----------------------------------------------------------

var DemoPlaceNewOrder = DemoPlaceNewOrder || (() => {
	let cli, sup, prd, ord;

	function render() {
		// Override default error handler
		const ajax = $ui.getAjax();
		ajax.setErrorHandler(function(err) {
			$('#demoplaceneworder-err').append($('<p/>').text(ajax.getErrorMessage(err))).show();
		});

		$('#demoplaceneworder-ord').append($ui.view.tools.panel({ title: 'Order', content: $('<div/>')
			.append($('<div/>').append('Selected customer:'))
			.append($('<div/>', { id: 'demoplaceneworder-selcli' }).append('&lt;none&gt;')).append('<br/>')
			.append($('<div/>').append('Selected product:'))
			.append($('<div/>', { id: 'demoplaceneworder-selprd' }).append('&lt;none&gt;')).append('<br/>')
			.append($('<div/>')
				.append($('<input/>', { id: 'demoplaceneworder-qty', type: 'text'}).val(1).change(total))
				.append('&nbsp;Total:&nbsp;')
				.append($('<span/>', { id: 'demoplaceneworder-total' }).append('0.00'))
				.append('&nbsp;&euro;&nbsp;')
				.append($('<button/>', { id: 'demoplaceneworder-ok', disabled: true }).addClass('btn btn-primary').text('OK').click(order))
			)
			.append($('<div/>', { id: 'demoplaceneworder-err', style: 'color: red; display: none; font-weight: bold;' }))
		}));

		getCli();
		getSup();
		if (prd) prd.item = undefined;
	}

	function getCli() {
		$ui.getUIObject('DemoClient', 'pno_DemoClient', function(c) {
			cli = c;
			cli.item = null;
			cli.search(function() {
				const div = $('<div/>');
				for (const item of cli.list) {
					const label = item.demoCliCode + ' - ' + item.demoCliFirstname + ' ' + item.demoCliLastname;
					div.append($('<p/>', { id: 'demoplaceneworder-cli_' + item.row_id }).addClass('obj').data('item', item).click(selCli).text(label));
				}
				$('#demoplaceneworder-cli').append($ui.view.tools.panel({ title: 'Select customer', content: div })).slideDown();
			});
		});
	}

	function selCli() {
		cli.item = $(this).data('item');
		$('#demoplaceneworder-cli').find('p').removeClass('sel');
		$('#demoplaceneworder-cli_' + cli.item.row_id).addClass('sel');
		$('#demoplaceneworder-selcli').empty().append($('<strong/>').text(cli.item.demoCliCode + ' - ' + cli.item.demoCliFirstname + ' ' + cli.item.demoCliLastname));
		if (prd?.item) {
			$('#demoplaceneworder-ok').attr('disabled', false);
			$('#demoplaceneworder-qty').select();
		}
	}

	function getSup() {
		$ui.getUIObject('DemoSupplier', 'pno_DemoSupplier', function(s) {
			sup = s;
			sup.item = null;
			sup.search(function() {
				const div = $('<div/>');
				for (const item of sup.list) {
					const label = item.demoSupCode + ' - ' + item.demoSupName;
					div.append($('<p/>', { id: 'demoplaceneworder-sup-' + item.row_id }).addClass('obj').data('item', item).click(selSup).text(label));
				}
				$('#demoplaceneworder-sup').append($ui.view.tools.panel({ title: 'Select supplier', content: div })).slideDown();
			});
		});
	}

	function selSup() {
		sup.item = $(this).data('item');
		$('#demoplaceneworder-sup').find('p').removeClass('sel');
		$('#demoplaceneworder-sup-' + sup.item.row_id).addClass('sel');
		$('#demoplaceneworder-prd').hide().empty();
		$('#demoplaceneworder-selprd').empty();
		$('#demoplaceneworder-ok').attr('disabled', true);
		$ui.getUIObject('DemoProduct', 'pno_DemoProduct', function(p) {
			prd = p;
			prd.item = null;
			prd.search(function() {
				const div = $('<div/>');
				for (const item of prd.list) {
					const label = item.demoPrdReference + ' - ' + item.demoPrdName;
					div.append($('<p/>', { id: 'demoplaceneworder-prd-' + item.row_id }).addClass('obj').data('item', item).click(selPrd)
						.append($('<img/>', { src: 'data:' + item.demoPrdPicture.mime + ';base64,' + item.demoPrdPicture.content }).css('width', '50px'))
						.append($('<span/>').text(label)));
				}
				$('#demoplaceneworder-prd').append($ui.view.tools.panel({ title: 'Select product', content: div })).slideDown();
			}, { demoPrdSupId: sup.item.row_id }, { inlineDocs: true });
		});
	}

	function selPrd() {
		prd.item = $(this).data('item');
		$('#demoplaceneworder-prd').find('p').removeClass('sel');
		$('#demoplaceneworder-prd-' + prd.item.row_id).addClass('sel');
		$('#demoplaceneworder-selprd').empty()
			.append($('<img/>', { src: 'data:' + prd.item.demoPrdPicture.mime + ';base64,' + prd.item.demoPrdPicture.content }))
			.append('<br/>').append($('<strong/>').text(prd.item.demoPrdReference + ' ' + prd.item.demoPrdName));
		if (cli?.item) {
			$('#demoplaceneworder-ok').attr('disabled', false);
			$('#demoplaceneworder-qty').select();
		}
		total();
	}

	function total() {
		$('#demoplaceneworder-err').empty();
		const t = parseFloat(prd.item.demoPrdUnitPrice) * parseFloat($('#demoplaceneworder-qty').val());
		$('#demoplaceneworder-total').text(t.toFixed(2));
	}

	function order() {
		$('#demoplaceneworder-err').empty().hide();
		$ui.getUIObject('DemoOrder', 'pno_DemoOrder', function(o) {
			ord = o;
			ord.item = null;
			// ZZZ Get for create must be called to set default values
			ord.getForCreate(function() {
				ord.item.demoOrdCliId = cli.item.row_id;
				ord.item.demoOrdPrdId = prd.item.row_id;
				// ZZZ populate must be called to set all referred fields from client and product before creation
				ord.populate(function() {
					ord.item.demoOrdQuantity = $('#demoplaceneworder-qty').val();
					ord.create(function() {
						$('#demoplaceneworder').html('<p>Order created with number ' + ord.item.demoOrdNumber + '<br/>Thank you !</p>');
						$ui.view.notify({ type: 'create', object: ord, rowId: ord.item.row_id }); // Notify UI components (e.g. menu)
					});
				});
			});
		});
	}

	return { render: render };
})();