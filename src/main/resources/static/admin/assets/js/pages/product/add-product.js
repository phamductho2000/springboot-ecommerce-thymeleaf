$(document).ready(function () {
    "use strict";

    // renderAllTypeProduct();

    var categoryStore = [];

    // function renderAllTypeProduct() {
    //     $.ajax({
    //         url: '/admin/type-product/api/get-all',
    //         type: 'GET',
    //         success: function (response) {
    //             response.forEach(function (type) {
    //                 $(`#select-types`).append(new Option(type.name, type.id));
    //             })
    //         },
    //         error: function (error) {
    //             console.log(error);
    //         }
    //     })
    // }

    $(document).on('change', '#select-types', function () {
        let selectValue = $("#select-types option:selected").val();
        if (Number(selectValue) === 0) {
            return;
        }
        getAttrsByTypeId(Number(selectValue));
    })

    function getAttrsByTypeId(id) {
        if (id != null) {
            $('#content-add-attribute').empty();
            $.ajax({
                url: '/admin/attribute/get-all-by-type-product-id',
                type: 'POST',
                data: {
                    "typeProductId": id,
                },
                success: function (response) {
                    response.forEach(function (item, index) {
                        $('#content-add-attribute').append(`
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="select-attr-product" class="form-label">Tên thuộc tính</label>
                                    <input class="form-control" type="text" value="${item.attr.name}" readonly>
                                    <input class="form-control" type="number" value="${item.attr.id}" name="attributes[${index}].attr.id" hidden>
        
                                </div>
                            </div>
        
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="select-attr-value-product" class="form-label">Giá trị</label>
                                    <select class="form-select form-select-value" id="select-attr-value-product${index}" name="attributes[${index}].attrValue.id">
                                    </select>
                                </div>
                            </div>
                        </div>
                        `);

                        item.attrValues.forEach(function (value) {
                            $(`#select-attr-value-product${index}`).append(new Option(value.value, value.id));
                        })
                    })

                },
                error: function (error) {
                    console.log(error);
                }
            })
        }
    }

    $('#btn-save-attr-and-value').click(function () {
        $.ajax({
            url: '/admin/attribute/add-attr-and-value',
            type: 'POST',
            data: {
                "attr": $('#input-attr-name').val(),
                "value": $('#input-attr-value').val()
            },
            success: function (response) {
                $('#btn-close-modal').trigger('click');
                $.toast('Thêm thuộc tính mới thành công')
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    $("#add-attribute").click(function (e) {
        $.ajax({
            url: '/admin/product/attribute',
            type: 'GET',
            success: function (response) {
                let count = $('#countAttr').val();

                $('#countAttr').val(count);
                $("#content-add-attribute").append(response);
                $("#select-attr-product").attr('name', `attributes[${count}].attr.id`);
                $("#select-attr-product").attr('id', 'select-attr-product' + count);
                $("#select-attr-value-product").attr('name', `attributes[${count}].attrValue.id`);
                $("#select-attr-value-product").attr('id', 'select-attr-value-product' + count);
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    // $('#show-upload-images').click(function () {
    //     $.ajax({
    //         url: '/admin/upload/getAllFromDb',
    //         type: 'GET',
    //         success: function (images) {
    //             $('#upload-images').empty();
    //             images.forEach(function (image, index) {
    //                 $('#upload-images').append(
    //                     `<div class='card me-2 item-image' id="image-list${index}" style='width: 10rem'>
    //                        <img src='${image.url}' class='card-img-top' alt='...'>
    //                        <div class="card-body">
    //                            <h5 class='card-title'>${image.name}</h5>
    //                        <input type='hidden' value='${image.id}'>
    //                        </div>
    //                     </div>`
    //                 )
    //                 $(`#image-list${index}`).click(function () {
    //                     $('.item-image').removeClass('border border-primary');
    //                     $(this).addClass('border border-primary');
    //                 })
    //             })
    //             var myModal = new bootstrap.Modal(document.getElementById('modal-show-upload-images'));
    //             myModal.show();
    //         },
    //         error: function (error) {
    //             console.log(error);
    //         }
    //     })
    // })

    $('#show-upload-images').click(function () {
        $('#body-modal-file-manager').load("/admin/file-manager/home", function () {
            var myModal = new bootstrap.Modal(document.getElementById('modal-show-upload-images'));
            myModal.show();
        })
    })

    $('#add-image').click(function () {
        $('.container-img').each(function (index) {
            if ($(this).hasClass('active-img')) {
                let count = $('#countImg').val();
                count++;
                $('#countImg').val(count);
                $('#render-upload-image').append(
                    `<div class='img-container'>
                        <img src='${$(this).find('img').attr('src')}'
                            width='150' height='150' class='hover-image img-thumbnail me-2' alt='...'>
                             <div class='hover-middle'>
                                <button type="button" class='btn btn-danger btn-remove-img'>Xóa</button>
                             </div>
                        <input type='hidden' name='images[${count}].id' value='${$(this).find('input').val()}'>
                    </div>`
                )
            }
        })
        var myModal = new bootstrap.Modal(document.getElementById('modal-show-upload-images'));
        myModal.hide();
    })

    $(document).on('change', '#select-categories', function () {
        let selectValue = $("#select-categories option:selected").val();
        if (!(selectValue === '0') && !categoryStore.includes(selectValue)) {
            categoryStore.push(selectValue);
            let count = $('#countCate').val();
            count++;
            $('#countCate').val(count);
            let formatName = $("#select-categories option:selected").text().split('>');
            let name = formatName[formatName.length - 1];

            $('#tag-categories').append(
                `<div class="tag-cate-item">
                    <a class='btn btn-info btn-sm mb-1 me-1'>
                        ${name}
                        <i class="fa-solid fa-xmark btn-remove-tag-cate"></i>
                    </a>
                    <input type='hidden' name='categories[${count}].id' value='${selectValue}'>
                </div>`
            )
        }
    })

    $(document).on('change', '.form-select-attr', function () {
        let val = $(this).val()
        let id = $(this).attr('id')
        $.ajax({
            url: '/admin/attribute/attribute-value/' + val,
            type: 'GET',
            success: function (response) {
                id = id.replace('select-attr-product', 'select-attr-value-product')
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

    window.renderCates = function renderCates(cates) {
        $('#countCate').val(cates.length - 1);
        cates.forEach(function (item, index) {
            $('#tag-categories').append(
                `
                        <div class="tag-cate-item">
                            <a class='btn btn-info btn-sm mb-1 me-1'>
                                    ${item.name}
                                <i class="fa-solid fa-xmark btn-remove-tag-cate"></i>
                            </a>
                            <input type='hidden' name='categories[${index}].id' value='${item.id}'>
                        </div>
                        `
            )
            categoryStore.push(item.id.toString());
        })
    }

    window.renderAttrs = function renderAttrs(attrs) {
        $('#countAttr').val(attrs.length - 1);
        attrs.forEach(function (item, index) {
            $('#content-add-attribute').append(
                `<div class="row">
                   <div class="col-md-5">
                       <div class="mb-3">
                           <label for="select-attr-product" class="form-label">Tên thuộc tính</label>
                           <input class="form-control" type="text" value="${item.attr.name}" readonly>
                           <input class="form-control" type="number" value="${item.attr.id}" name="attributes[${index}].attr.id" hidden>
                       </div>
                   </div>

                   <div class="col-md-5">
                       <div class="mb-3">
                           <label for="select-attr-value-product" class="form-label">Giá trị</label>
                           <select class="form-select form-select-value" id="select-attr-value-product${index}" name="attributes[${index}].attrValue.id">
                           </select>
                       </div>
                   </div>
               </div>`);

            $.ajax({
                url: '/admin/attribute/attribute-value/' + item.attr.id,
                type: 'GET',
                success: function (response) {
                    response.forEach(function (value) {
                        $(`#select-attr-value-product${index}`).append(new Option(value.value, value.id));
                    })
                    $(`#select-attr-value-product${index}`).val(item.attrValue.id);
                },
                error: function (error) {
                    console.log(error);
                }
            })

        })
    }

    window.renderPrices = function renderPrices(product) {
        $('#ori-price-input').val(formatToCurrency(product.originalPrice.toString()))
        $('#price-input').val(formatToCurrency(product.price.toString()))
    }

    window.renderImages = function renderImages(imgs) {
        $('#countImg').val(imgs.length - 1);
        imgs.forEach(function (item, index) {
            $('#render-upload-image').append(
                `<div class='img-container'>
                     <img src='${item.url}'
                            width='150' height='150' class='hover-image img-thumbnail me-2' alt='...'>
                          <div class='hover-middle'>
                                        <button type="button" class='btn btn-danger btn-remove-img'>Xóa</button>
                          </div>
                     <input type='hidden' name='images[${index}].id' value='${item.id}'>
                </div>`
            )
        })
    }

})