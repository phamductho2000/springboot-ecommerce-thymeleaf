$(function () {
    "use strict";

    const tab1 = document.querySelector('#myTab #cus-tab')
    const tab2 = document.querySelector('#myTab #address-tab')
    const tab3 = document.querySelector('#myTab #detail-tab')
    const tab4 = document.querySelector('#myTab #total-tab')

    var step = 1;

    function updateTotalPrice() {
        let feeShip = Number($('#feeShip').val());
        let totalCart = 0;
        $('#table-detail-order tbody tr').each(function (index) {
            totalCart += Number($(this).find('.product-total').val())
        })
        $('#totalCart').val(totalCart);
        $('#totalPriceOrder').val(totalCart + Number(feeShip))

        $('#total-cart').text(formatToCurrency(totalCart) + ' đ');
        $('#total-price-order').text(formatToCurrency($('#totalPriceOrder').val()) + ' đ');
    }

    $('#btn-next').on('click', function () {
        if (step == 1 && $('#customerForm').valid()) {
            step = 2;
            let userId = $("#customerId").val();
            renderAddressesByUserId(userId);
            var secondTab = new bootstrap.Tab(tab2);
            secondTab.show();
            // checkAddressChecked();
            return;
        }
        if (step == 2) {
            step = 3
            var thirdTab = new bootstrap.Tab(tab3)
            thirdTab.show()
            $('#btn-next').css('display', 'none');
            $('#btn-save').css('display', 'block');
            return;
        }
    })

    function checkAddressChecked() {
        var existAddressId = $('#checkedAddress').val();
        if (existAddressId != 0) {
            $('.radio-address').each(function (index) {
                console.log($(this).val())
                if($(this).val() == existAddressId) {
                    $(this).prop('checked', true);
                    return false;
                }
            })
        }
    }

    $('#btn-back').on('click', function () {
        if (step == 3) {
            step = 2;
            var secondTab = new bootstrap.Tab(tab2)
            secondTab.show()
            $('#btn-next').css('display', 'block');
            $('#btn-save').css('display', 'none');
            return;
        }
        if (step == 2) {
            step = 1;
            var firstTab = new bootstrap.Tab(tab1)
            firstTab.show()
            return;
        }
    })

    $(document).on('change', 'input[type=radio]', function () {
        if ($(this).prop("checked")) {
            var id = $(this).val();
            $.ajax({
                url: '/delivery-address/' + id,
                type: 'GET',
                success: function (address) {
                    caculateFeeShip(address);
                },
                error: function (error) {
                    console.log(error);
                }
            })
        }
    })

    $('#customerId').on('change', function () {
        renderInfoUserByUserId($(this).val())
    })

    $('#productId').on('change', function () {
        $.ajax({
            url: '/admin/product/api/getInfo/' + $(this).val(),
            type: 'GET',
            success: function (product) {
                $('#p-price').val(product.price);
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    function renderAddressesByUserId(id) {
        $.ajax({
            url: '/admin/user/api/getAddresses/' + id,
            type: 'GET',
            success: function (addresses) {
                $('#content-addresses').empty();
                addresses.forEach(function (address, index) {
                    $('#content-addresses').append(`
                    <div class='form-check mb-2'>
                        <input type='radio' id='radioAddress${index}' value='${address.id}' class='form-check-input radio-address'>
                        <label class='form-check-label' for='radioAddress${index}'>
                            <div>${address.customerName}, ${address.customerPhone}</div>
                            <div>${address.deliveryAddress}, ${address.wardsName}, ${address.districtName}, ${address.provinceName}</div>
                        </label>
                    </div>
                    `)
                })
                var existAddressId = $('#checkedAddress').val();
                if (existAddressId != 0) {
                    $('.radio-address').each(function (index) {
                        console.log($(this).val())
                        if($(this).val() == existAddressId) {
                            $(this).prop('checked', true);
                            return false;
                        }
                    })
                }
            },
            error: function (error) {
                console.log(error);
            }
        })
    }

    function renderInfoUserByUserId(id) {
        $.ajax({
            url: '/admin/user/api/getInfo/' + id,
            type: 'GET',
            success: function (user) {
                $('#fullname').val(user.name);
                $('#email').val(user.email);
                $('#phone').val(user.phone);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }

    $('.btn-remove-item-detail').each(function (index) {
        $(this).on('click', function () {
            $(this).closest('tr').remove();
            $('#table-detail-order tbody tr').each(function (index, item) {
                $(this).find('.product-id').attr('name', `detailOrders[${index}].product.id`);
                $(this).find('.product-quantity').attr('name', `detailOrders[${index}].quantity`);
                $(this).find('.product-price').attr('name', `detailOrders[${index}].price`);
                $(this).find('.product-total').attr('name', `detailOrders[${index}].total`);
            })
            $('#countItem').val($('#table-detail-order tbody tr').length - 1);
            updateTotalPrice();
        })
    })

    function caculateFeeShip(address) {
        $.ajax({
            url: 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee',
            type: 'GET',
            headers: {
                "Token": "cf8dbcb8-cd38-11ec-a4f1-1ab3296c576f",
                "Content-Type": "application/json",
                "ShopId": 2735528
            },
            data: {
                "from_district_id": 1482,
                "service_id": null,
                "service_type_id": 2,
                "to_district_id": address.districtId,
                "to_ward_code": `${address.wardsCode}`,
                "height": 50,
                "length": 20,
                "weight": 200,
                "width": 20,
                "insurance_value": 10000,
                "coupon": null
            },
            success: function (response) {
                var feeShip = (response.data.total).toLocaleString('it-IT', {
                    style: 'currency',
                    currency: 'VND',
                });
                $('#feeShip').val(response.data.total)
                $('#fee-ship').text(feeShip.substring(0, feeShip.length - 4) + ' đ');
            },
            error: function (error) {
                console.log(error);
            }
        })
    }


});
