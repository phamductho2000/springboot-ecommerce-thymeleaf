$(document).ready(function () {
    "use strict";

    let typeBtn = '';

    $('input[type="file"][name="file"]').on('change', function (e) {
        e.preventDefault();
        var data = new FormData();
        data.append('file', e.target.files[0]);
        data.append('path', getParentPath('#jstree-1 .jstree-anchor'));
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/admin/file-manager/upload-img",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            method: "POST",
            success: function (res) {
                // getImgs(null, getParentPath());
                // renderImgs(res);
            },
            error: function (e) {
                console.log(e);
            }
        });
    });

    window.handleUploadFile = function handleUploadFile(e) {
        console.log(e);
        var data = new FormData();
        data.append('file', e.target.files[0]);
        data.append('path', getParentPath('#jstree-1 .jstree-anchor'));
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/admin/file-manager/upload-img",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            method: "POST",
            success: function (res) {
                // getImgs(null, getParentPath());
                // renderImgs(res);
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    $('#create-sub-folder').on('click', function () {
        showModal('folder', 'Tạo mới', 'Tên thư mục mới');
        typeBtn = 'create-folder';
    })

    function getParentPath(selector) {
        let path = '';
        $(selector).each(function (i) {
            if ($(this).hasClass('jstree-clicked')) {
                path = $(this).attr('data-path');
                return;
            }
        })
        return path;
    }

    function createFolder() {
        var value = $('#input-value').val();
        var parentPath = getParentPath('#jstree-1 .jstree-anchor');
        $.ajax({
            type: "POST",
            url: "/admin/file-manager/create-sub-folder",
            data: {
                'parentPath': parentPath !== '' ? parentPath : 'uploads',
                'folderName': value
            },
            success: function (response) {
                closeModal();
                $('.page-aside-left').replaceWith($(response).find('.page-aside-left'));
                $.getScript('/admin/assets/js/vendor/jstree/jstree.min.js');
                $.getScript('/admin/assets/js/pages/file_manager/file.manager.js');
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    window.showActionFolder = function showActionFolder() {
        $('#action-img').css('display', 'none');
        $('#action-folder').css('display', 'flex');
    }

    window.showActionImg = function showActionImg(ele) {
        $('#action-folder').css('display', 'none');
        $('#action-img').css('display', 'flex');
        if (event.ctrlKey) {
            $(ele).addClass('active-img');
        } else {
            $('.container-img').removeClass('active-img');
            $(ele).addClass('active-img');
        }
    }

    $('#edit-name-img').click(function () {
        $('.container-img').each(function (i) {
            if ($(this).hasClass('active-img')) {
                var imgName = $(this).attr('data-name-img');
                var imgId = $(this).attr('data-img-id');
                var imgPath = $(this).attr('data-img-path');
                $('#myModal #input-ele-id').val(imgId);
                $('#myModal #input-ele-path').val(imgPath);
                showModal('edit-name-img', 'Đổi tên', 'Hãy nhập tên tập tin mới:', imgName);
                typeBtn = 'edit-img';
            }
        })
    })

    $('#delete-img').click(function () {
        $('.container-img').each(function (i) {
            if ($(this).hasClass('active-img')) {
                var imgName = $(this).attr('data-name-img');
                var imgId = $(this).attr('data-img-id');
                var imgPath = $(this).attr('data-img-path');
                $('#modal-delete #input-ele-dele-id').val(imgId);
                $('#modal-delete #input-ele-dele-path').val(imgPath);
                $('#modal-delete #content').text(`Bạn có chắc chắn muốn xóa tệp tin "${imgName}"`);
                showModalDelete();
                return;
            }
        })
    })

    function editImg() {
        var imgName = $('#input-value').val();
        var imgId = $('#input-ele-id').val();
        var imgPath = $('#input-ele-path').val();
        $.ajax({
            type: "POST",
            url: "/admin/file-manager/edit-img",
            data: {imgName, imgId, imgPath},
            success: function (response) {
                closeModal();
                $('.page-aside-right').replaceWith($(response).find('.page-aside-right'));
                $.getScript('/admin/assets/js/vendor/jstree/jstree.min.js');
                $.getScript('/admin/assets/js/pages/file_manager/file.manager.js');
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    // $('#btn-delete').on('click', function (e) {
    //     console.log('times')
    //     var imgId = $('#input-ele-dele-id').val();
    //     var imgPath = $('#input-ele-dele-path').val();
    //     $.ajax({
    //         type: "POST",
    //         url: "/admin/file-manager/delete-img",
    //         data: {imgId, imgPath},
    //         success: function (response) {
    //             closeModalDelete();
    //             // $('.page-aside-right').replaceWith($(response).find('.page-aside-right'));
    //             // $.getScript('/admin/assets/js/vendor/jstree/jstree.min.js');
    //             // $.getScript('/admin/assets/js/pages/file_manager/file.manager.js');
    //         },
    //         error: function (e) {
    //             console.log(e);
    //         }
    //     });
    // })

    window.deleteImg = function deleteImg() {
        var imgId = $('#input-ele-dele-id').val();
        var imgPath = $('#input-ele-dele-path').val();
        $.ajax({
            type: "POST",
            url: "/admin/file-manager/delete-img",
            data: {imgId, imgPath},
            success: function (response) {
                closeModalDelete();
                $('.page-aside-right').replaceWith($(response).find('.page-aside-right'));
                $.getScript('/admin/assets/js/vendor/jstree/jstree.min.js');
                $.getScript('/admin/assets/js/pages/file_manager/file.manager.js');
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    $('#btn-save').on('click', function () {
        switch (typeBtn) {
            case 'create-folder':
                createFolder();
                break;
            case 'edit-folder':
                break;
            case 'edit-img':
                editImg();
                break;
            default:
        }
    })

    window.getImgs = function getImgs(ele, path) {
        var path = ele ? $(ele).attr('data-path') : path;
        $.ajax({
            type: "POST",
            url: "/admin/file-manager/get-imgs-by-folder",
            data: {path},
            beforeSend: function (xhr) {
                showIconLoading();
            },
            success: function (response) {
                $('.page-aside-right').replaceWith($(response).find('.page-aside-right'));
                $.getScript('/admin/assets/js/pages/file_manager/index.js');
                $.getScript('/admin/assets/js/vendor/jstree/jstree.min.js');
                $.getScript('/admin/assets/js/pages/file_manager/file.manager.js');
            },
            error: function (e) {
                console.log(e);
            },
            done: function (response) {
                hideIconLoading();
            }
        });
    }

    window.moveImg = function moveImg() {
        var movePath = getParentPath('#jstree-modal .jstree-anchor');
        let imgId = '';
        let imgPath = '';
        $('.container-img').each(function (i) {
            if ($(this).hasClass('active-img')) {
                imgId = $(this).attr('data-img-id');
                imgPath = $(this).attr('data-img-path');
                return;
            }
        })
        $.ajax({
            type: "POST",
            url: "/admin/file-manager/move-img",
            data: {imgId, imgPath, movePath},
            beforeSend: function (xhr) {
                showIconLoading();
            },
            success: function (response) {
                closeModalMove();
                $('.page-aside-right').replaceWith($(response).find('.page-aside-right'));
                $.getScript('/admin/assets/js/pages/file_manager/index.js');
                $.getScript('/admin/assets/js/vendor/jstree/jstree.min.js');
                $.getScript('/admin/assets/js/pages/file_manager/file.manager.js');
            },
            error: function (e) {
                console.log(e);
            },
            done: function (response) {
                hideIconLoading();
            }
        });
    }

    function renderImgs(imgs) {
        $('#container-imgs').empty();
        imgs.forEach(function (image) {
            $('#container-imgs').append(`
                <div class="card me-2 container-img" style="width: 11rem;" onclick="showActionImg(this)" data-name-img="${image.name}"
                    data-img-id="${image.id}" data-img-path="${image.url}">
                    <img src="${'/' + image.url}}" class="card-img-top" alt="anh san pham">
                      <div class="card-body">
                         <p class="card-text">${image.name}</p>
                         <p class="card-text">${image.size}</p>
                      </div>
                </div>
            `)
        })
    }

    window.showModalMove = function showModalMove() {
        $.ajax({
            type: "GET",
            url: "/admin/file-manager/getAllFolder",
            success: function (response) {
                showFolders(response);
                $('#modal-move').css('display', 'flex');
                $('#modal-move').css('justify-content', 'center');
                $('#modal-move').css('align-items', 'center');
            },
            error: function (e) {
                console.log(e);
            },
        });
    }

    window.closeModalMove = function closeModalMove() {
        $('#modal-move #container-folder-tree').empty();
        $('#modal-move').css('display', 'none');
    }

    function showModal(type, title, labelValue, value) {
        if (title) {
            $('#myModal #title-modal').text(title);
        }
        if (title) {
            $('#myModal #label-input').text(labelValue);
        }
        if (value) {
            $('#myModal #input-value').val(value);
        }
        $('#myModal').css('display', 'flex');
        $('#myModal').css('justify-content', 'center');
        $('#myModal').css('align-items', 'center');
    }

    window.closeModal = function closeModal() {
        $('#myModal').css('display', 'none');
    }

    function showModalDelete() {
        $('#modal-delete').css('display', 'flex');
        $('#modal-delete').css('justify-content', 'center');
        $('#modal-delete').css('align-items', 'center');
    }

    window.closeModalDelete = function closeModalDelete() {
        $('#modal-delete').css('display', 'none');
    }

    function showIconLoading() {
        $('#loadingImgs').css('display', 'block');
    }

    function hideIconLoading() {
        $('#loadingImgs').css('display', 'none');
    }

    const renderFolders = (arr, node) => {
        return arr.map(x => {
            var newListItem = document.createElement("li");
            // newListItem.innerText = x.name;
            var linkItem = document.createElement("a");
            linkItem.innerText = x.name;
            linkItem.setAttribute('data-path', x.path);
            newListItem.appendChild(linkItem);
            if (x.children.length > 0) {
                var newSubList = document.createElement("ul");
                newListItem.appendChild(newSubList);
                renderFolders(x.children, newSubList);
            }
            if (node) {
                node.appendChild(newListItem);
            }
            return newListItem;
        })
    };

    function showFolders(data) {
        var containerParent = document.querySelector('#container-folder-tree');
        var containerDiv = document.createElement('div');
        containerDiv.id = 'jstree-modal';
        var containerUl = document.createElement('ul');

        var items = renderFolders(data);

        for (var i = 0; i < items.length; ++i) {
            containerUl.appendChild(items[i])
        }

        containerDiv.appendChild(containerUl);
        containerParent.appendChild(containerDiv);

        $('#jstree-modal').jstree({
            core: {themes: {responsive: !1}},
            types: {default: {icon: "mdi mdi-folder font-18"}, file: {icon: "ri-article-line"}},
            plugins: ["types"]
        });
    }

})