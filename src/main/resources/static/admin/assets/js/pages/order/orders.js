$(document).ready(function () {
  "use strict";
  $("#products-datatable").DataTable({
    language: {
      paginate: {
        previous: "<i class='mdi mdi-chevron-left'>",
        next: "<i class='mdi mdi-chevron-right'>",
      },
      info: "Showing orders _START_ to _END_ of _TOTAL_",
      lengthMenu:
        'Hiển thị <select class=\'form-select form-select-sm ms-1 me-1\'><option value="5">5</option><option value="10">10</option><option value="20">20</option><option value="-1">All</option></select> hóa đơn',
    },
    pageLength: 5,
    columns: [
      {
        orderable: !1,
        targets: 0,
        render: function (e, l, a, o) {
          return (
            "display" === l &&
              (e =
                '<div class="form-check"><input type="checkbox" class="form-check-input dt-checkboxes"><label class="form-check-label">&nbsp;</label></div>'),
            e
          );
        },
        checkboxes: {
          selectRow: !0,
          selectAllRender:
            '<div class="form-check"><input type="checkbox" class="form-check-input dt-checkboxes"><label class="form-check-label">&nbsp;</label></div>',
        },
      },
      { orderable: !0 },
      { orderable: !0 },
      { orderable: !0 },
      { orderable: !0 },
      { orderable: !0 },
      { orderable: !1 },
    ],
    select: { style: "multi" },
    // order: [[5, "desc"]],
    drawCallback: function () {
      $(".dataTables_paginate > .pagination").addClass("pagination-rounded"),
        $("#products-datatable_length label").addClass("form-label"),
        document
          .querySelector(".dataTables_wrapper .row")
          .querySelectorAll(".col-md-6")
          .forEach(function (e) {
            e.classList.add("col-sm-6"),
              e.classList.remove("col-sm-12"),
              e.classList.remove("col-md-6");
          });
    },
  });
});
