function editValue(valueId) {
    $.ajax({
        url: '/admin/attribute/edit-value/'+valueId,
        type: 'GET',
        success: function (response) {
           $('#input-edit-value').val(response.value)
        },
        error: function (error) {
            console.log(error);
        }
    })
    var myModal = new bootstrap.Modal(document.getElementById('modalEditValue'))
    myModal.show();
    saveValue(valueId);
}

function saveValue(valueId) {
    $('#btn-update-new-value').on('click', function () {
        let attrId = $('#attr-id').val();
        let value = $('#input-edit-value').val();
        $.ajax({
            url: '/admin/attribute/value/save/'+attrId,
            type: 'POST',
            data: {
                "id": valueId,
                "value": value
            },
            success: function (response) {
                $('.close-modal').trigger('click');
                $('#attribute-values-datatable').DataTable().destroy();
                $('#attribute-values-datatable').replaceWith($(response).find('#attribute-values-datatable'));
                $.getScript('/admin/assets/js/pages/attribute/attribute.values.js')
            },
            error: function (error) {
                console.log(error);
            }
        })
    })
}

function handleOnblurPrice(ele) {
    const oldValue = $(ele).val().toString().replaceAll(",", "");
    let formatPrice = formatToCurrency(oldValue);
    $(ele).val(formatPrice);
    const input = $(ele).closest('.mb-3').find('input[type=number]');
    $(input).val(oldValue);
}

function handleOnPastePrice(ele) {
    const oldValue = $(ele).val().toString().replaceAll(",", "");
    const input = $(ele).closest('.mb-3').find('input[type=number]');
    $(input).val(oldValue);
}

const formatToCurrency = amount => {
    return amount.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
}