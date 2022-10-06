$().ready(function () {
    $("#category-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "name": {
                required: true,
            },
            "slug": {
                required: true,
            }
        },
        messages: {
            "name": {
                required: "Thông tin bắt buộc nhập",
            },
            "slug": {
                required: "Thông tin bắt buộc nhập",
            }
        }
    });
});
