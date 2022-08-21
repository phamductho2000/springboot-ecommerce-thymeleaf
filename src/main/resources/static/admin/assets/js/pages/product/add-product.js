$(document).ready(function () {
    "use strict";

    $(document).on('change', '.form-select-attr', function () {
        let val = $(this).val()
        let id = $(this).attr('id')
        $.ajax({
            url: '/admin/attribute/attribute-value/' + val,
            type: 'GET',
            success: function (response) {
                id = id.replace('select-attr-product', 'select-attr-value-product')
                console.log(id)
                $(`#${id}`).empty()
                response.forEach(function (obj) {
                    $(`#${id}`).append(new Option(obj.value, obj.id));
                })
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    $(".btn-remove-attr").each(function (index) {
        $(this).on('click', function () {
            $(this).closest('.row').empty();
            $('.form-select-attr').each(function (index, item) {
                let valueId = $(this).closest('.row').find('.form-select-value').attr('id')
                $(this).attr('name', `attributes[${index}].attr.id`);
                $(this).attr('id', 'select-attr-product' + index);
                $(`#${valueId}`).attr('name', `attributes[${index}].attrValue.id`);
                $(`#${valueId}`).attr('id', 'select-attr-value-product' + index);
            })
            $('#countAttr').val($('.form-select-attr').length - 1);
        })
    })

    $('.btn-remove-tag-cate').each(function (index) {
        $(this).on('click', function () {
            $(this).closest('.tag-cate-item').remove();
            $('.tag-cate-item').each(function (index, item) {
                $(this).find('input').attr('name', `categories[${index}].id`);
                $(this).find('input').attr('id', 'tagCate' + index);
            })
            $('#countCate').val($('.tag-cate-item').length - 1);
        })
    })

    $('.btn-remove-img').each(function (index) {
        $(this).on('click', function () {
            $(this).closest('.img-container').remove();
            $('.img-container').each(function (index, item) {
                $(this).find('input').attr('name', `images[${index}].id`);
            })
            $('#countImg').val($('.img-container').length - 1);
        })
    })

})