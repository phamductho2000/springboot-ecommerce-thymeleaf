(function () {
    addEventToBtnEditAddress();

    function addEventToBtnEditAddress() {
        var btnEditAddresses = document.getElementsByClassName("btn-edit-address");

        for (i = 0; i < btnEditAddresses.length; i++) {
            let id = btnEditAddresses[i].getElementsByTagName("input")[0].value;
            btnEditAddresses[i].addEventListener("click", function () {
                showModalEditAddress(id);
            });
        }
    }

    function showModalEditAddress(id) {
        $.ajax({
            url: '/delivery-address/' + id,
            type: 'GET',
            success: function (response) {
                $('#modal-edit-address').find('#customer_name').val(response.customerName);
                $('#modal-edit-address').find('#customer_phone').val(response.customerPhone);
                $('#modal-edit-address #calc_shipping_provinces option:selected').text(response.provinceName);
                $('#modal-edit-address #calc_shipping_provinces option:selected').val(response.provinceId);
                $('#modal-edit-address #calc_shipping_district option:selected').text(response.districtName);
                $('#modal-edit-address #calc_shipping_district option:selected').val(response.districtId);
                $('#modal-edit-address #calc_shipping_wards option:selected').text(response.wardsName);
                $('#modal-edit-address #calc_shipping_wards option:selected').val(response.wardsCode);
                $('#modal-edit-address').find('#delivery_adddress').val(response.deliveryAddress);
                $('#btn-update-address').on("click", function () {
                    updateAddress(response.id);
                });
                var modalFormEditAddress = new bootstrap.Modal(document.getElementById('modal-edit-address'))
                modalFormEditAddress.show();
            },
            error: function (error) {
                console.log(error);
            }
        })
    }

    $('#btn-save-address').click(function () {
        $.ajax({
            url: '/address/save',
            type: 'POST',
            data: {
                customerName: $('#modal-address #customer_name').val(),
                customerPhone: $('#modal-address #customer_phone').val(),
                provinceId: $('#modal-address #calc_shipping_provinces').val(),
                provinceName: $("#modal-address #calc_shipping_provinces option:selected").text(),
                districtId: $('#modal-address #calc_shipping_district').val(),
                districtName: $('#modal-address #calc_shipping_district option:selected').text(),
                wardsCode: $('#modal-address #calc_shipping_wards').val(),
                wardsName: $('#modal-address #calc_shipping_wards option:selected').text(),
                deliveryAddress: $('#modal-address #delivery_adddress').val()
            },
            success: function (response) {
                $('.btn-close').trigger('click');
                appendAddressUser(response);
                addEventToBtnEditAddress();
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    function updateAddress(id) {
        if (id) {
            $.ajax({
                url: '/delivery-address/update/' + id,
                type: 'POST',
                data: {
                    customerName: $('#modal-edit-address-user #customer_name').val(),
                    customerPhone: $('#modal-edit-address-user #customer_phone').val(),
                    provinceId: $('#modal-edit-address-user #calc_shipping_provinces').val(),
                    provinceName: $("#modal-edit-address-user #calc_shipping_provinces option:selected").text(),
                    districtId: $('#modal-edit-address-user #calc_shipping_district').val(),
                    districtName: $('#modal-edit-address-user #calc_shipping_district option:selected').text(),
                    wardsCode: $('#modal-edit-address-user #calc_shipping_wards').val(),
                    wardsName: $('#modal-edit-address-user #calc_shipping_wards option:selected').text(),
                    deliveryAddress: $('#modal-edit-address-user #delivery_adddress').val()
                },
                success: function (response) {
                    $('.btn-close').trigger('click');
                    $('#container-address').replaceWith($(response).find('#container-address'));
                    var myToastEl = document.getElementById('alert-toast');
                    $('#alert-toast-content').text('C???p nh???t ?????a ch??? th??nh c??ng');
                    var myToast = new bootstrap.Toast(myToastEl);
                    myToast.show();
                    addEventToBtnEditAddress();
                },
                error: function (error) {
                    console.log(error);
                }
            })
        }
    }

    $.ajax({
        url: 'https://online-gateway.ghn.vn/shiip/public-api/master-data/province',
        headers: {
            'Token': 'cf8dbcb8-cd38-11ec-a4f1-1ab3296c576f',
            'Content-Type': 'application/json'
        },
        type: 'GET',
        success: function (response) {
            $('.calc_shipping_provinces').append('<option selected>T???nh / Th??nh ph???</option>')
            response.data.forEach(function (obj) {
                if ($('.calc_shipping_provinces option:selected').val() === obj.ProvinceID) {
                    $('.calc_shipping_provinces').append(new Option(obj.NameExtension[1], obj.ProvinceID, true));
                } else {
                    $('.calc_shipping_provinces').append(new Option(obj.NameExtension[1], obj.ProvinceID));
                }
            })
        },
        error: function (error) {
            console.log(error);
        }
    })

    $('.calc_shipping_provinces').on('change', function () {
        $.ajax({
            url: 'https://online-gateway.ghn.vn/shiip/public-api/master-data/district',
            type: 'GET',
            headers: {
                "Token": "cf8dbcb8-cd38-11ec-a4f1-1ab3296c576f",
                "Content-Type": "application/json"
            },
            data: {
                "province_id": $(this).val()
            },
            success: function (response) {
                $('.calc_shipping_district').empty();
                $('.calc_shipping_district').append('<option selected>Qu???n / Huy???n</option>')
                response.data.forEach(function (obj) {
                    if ($('.calc_shipping_district option:selected').val() === obj.DistrictID) {
                        $('.calc_shipping_district').append(new Option(obj.NameExtension[1], obj.DistrictID, true));
                    } else {
                        $('.calc_shipping_district').append(new Option(obj.NameExtension[1], obj.DistrictID));
                    }
                })
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    $('.calc_shipping_district').on('change', function () {
        $.ajax({
            url: 'https://online-gateway.ghn.vn/shiip/public-api/master-data/ward',
            type: 'GET',
            headers: {
                "Token": "cf8dbcb8-cd38-11ec-a4f1-1ab3296c576f",
                "Content-Type": "application/json"
            },
            data: {
                "district_id": $(this).val()
            },
            success: function (response) {
                $('.calc_shipping_wards').empty();
                $('.calc_shipping_wards').append('<option selected>Ph?????ng / X??</option>')
                response.data.forEach(function (obj) {
                    if ($('.calc_shipping_wards option:selected').val() === obj.WardCode) {
                        $('.calc_shipping_wards').append(new Option(obj.NameExtension[1], obj.WardCode, true));
                    } else {
                        $('.calc_shipping_wards').append(new Option(obj.NameExtension[1], obj.WardCode));
                    }
                })
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    $('#calc_shipping_wards').on('change', function () {
        $.ajax({
            url: 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services',
            type: 'GET',
            headers: {
                "Token": "cf8dbcb8-cd38-11ec-a4f1-1ab3296c576f",
                "Content-Type": "application/json"
            },
            data: {
                "shop_id": 2735528,
                "from_district": 1482,
                "to_district": $('#calc_shipping_district option:selected').val()
            },
            success: function (response) {
                $('#option-delivery').empty();
                response.data.forEach(function (obj) {
                    // caculateFeeShip(obj.service_id, obj.short_name)
                    createDeliveryOption(obj.service_id, obj.short_name)
                })
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    function createDeliveryOption(serviceId, name) {
        var radiobox = document.createElement('input');
        radiobox.type = 'radio';
        radiobox.id = serviceId;
        radiobox.value = serviceId;
        radiobox.className = 'form-check-input';

        var label = document.createElement('label')
        label.htmlFor = serviceId;
        label.className = 'form-check-label';

        var description = document.createTextNode(`Giao H??ng ${name}`);
        label.appendChild(description);

        var newline = document.createElement('br');

        var div = document.createElement('div')
        div.className = 'form-check';
        div.appendChild(radiobox);
        div.appendChild(label);
        div.appendChild(newline);

        var container = document.getElementById('option-delivery');
        container.appendChild(div);
    }

    function appendAddressUser(address) {
        $('#container-address').append(
            `<div class='col-md-6 mt-3'>
                <div class='card'>
                    <div class='card-body'>
                        <div class='form-check'>
                            <div class='d-flex'>
                                <div style='width: 90%'>
                                    <label class='form-check-label'
                                            for='${address.id}'>
                                        <div class='d-flex justify-content-between'>
                                            <h6 class='card-title'>${address.customerName}, ${address.customerPhone}</h6>
                                        </div>
                                        <p class='card-text'>${address.deliveryAddress}, ${address.wardsName},
                                            ${address.districtName}, ${address.provinceName}</p>
                                    </label>
                                </div>
                                <div class='btn-edit-address-user' style='width: 10%'>
                                    <label for='${'address' + address.id}'>
                                        <i class='fa-regular fa-pen-to-square fs-4 mdi-cursor-pointer'></i>
                                    </label>
                                    <input type='hidden' id='${'address' + address.id}'
                                            value='${address.id}'>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`
        )
    }
})();