package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.AppConstants;
import com.webecommerce.springboot.dto.CartDTO;
import com.webecommerce.springboot.dto.DetailOrderDTO;
import com.webecommerce.springboot.dto.FormAddOrderDTO;
import com.webecommerce.springboot.dto.OrderDTO;
import com.webecommerce.springboot.entity.DetailOrderEntity;
import com.webecommerce.springboot.entity.OrderEntity;
import com.webecommerce.springboot.entity.ProductEntity;
import com.webecommerce.springboot.repository.DetailOrderRepository;
import com.webecommerce.springboot.repository.OrderRepository;
import com.webecommerce.springboot.repository.ProductRepository;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.OrderService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    final int DA_HUY = 0;

    final int CHO_XU_LY = 1;

    final int DA_TIEP_NHAN = 2;

    final int DANG_GIAO = 3;

    final int DA_GIAO = 4;

    final int HOAN_TAT = 5;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    DetailOrderRepository detailOrderRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ModelMapper mapper;

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(o -> mapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(String id) {
        Optional<OrderEntity> foundOrder = orderRepository.findById(id);
        if (!foundOrder.isPresent()) {
            throw new RuntimeException("");
        }
        return mapper.map(foundOrder.get(), OrderDTO.class);
    }

    @Override
    public OrderDTO save(String userEmail, Long addressId, HashMap<String, CartDTO> cart, long totalOrderPrice, long feeShip) {
        OrderEntity newOrder = new OrderEntity();
        newOrder.setDeliveryAddress(
                deliveryAddressService.findEntityById(addressId)
        );
        newOrder.setUser(userService.findByEmail(userEmail));
        newOrder.setTotal(totalOrderPrice);
        newOrder.setFeeShip(feeShip);
        newOrder.setStatus(CHO_XU_LY);
        newOrder.setPaymentStatus(0);
        newOrder = orderRepository.save(newOrder);
        newOrder.setDetailOrders(saveDetail(newOrder, cart));
        return mapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO adminSave(FormAddOrderDTO formAddOrderDTO) {
        OrderEntity newOrder = new OrderEntity();
        if (formAddOrderDTO.getOrderId() != null) {
            newOrder.setId(formAddOrderDTO.getOrderId());
        }
        newOrder.setUser(userService.findEntityById(formAddOrderDTO.getCusId()));
        newOrder.setDeliveryAddress(deliveryAddressService.findEntityById(formAddOrderDTO.getDeliveryAddressId()));
        newOrder.setStatus(CHO_XU_LY);
        newOrder.setPaymentStatus(0);
        newOrder.setFeeShip(formAddOrderDTO.getFeeShip());
        newOrder.setTotal(formAddOrderDTO.getTotal());
        newOrder = orderRepository.save(newOrder);
        newOrder.setDetailOrders(adminSaveDetail(newOrder, formAddOrderDTO.getDetailOrders()));
        return mapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO adminUpdate(FormAddOrderDTO formAddOrderDTO) {
        OrderEntity newOrder = findEntityById(formAddOrderDTO.getOrderId());
        newOrder.setUser(userService.findEntityById(formAddOrderDTO.getCusId()));
        newOrder.setDeliveryAddress(deliveryAddressService.findEntityById(formAddOrderDTO.getDeliveryAddressId()));
        newOrder.setStatus(formAddOrderDTO.getStatus());
        newOrder.setFeeShip(formAddOrderDTO.getFeeShip());
        newOrder.setTotal(formAddOrderDTO.getTotal());
        newOrder.getDetailOrders().stream()
                .forEach(detail -> {
                    detailOrderRepository.deleteById(detail.getId());
                });
        newOrder.setDetailOrders(adminSaveDetail(newOrder, formAddOrderDTO.getDetailOrders()));
        newOrder = orderRepository.save(newOrder);
        return mapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public void updateStatus(String id, int status) {
        OrderEntity orderEntity = findEntityById(id);
        orderEntity.setStatus(status);
        if (status == HOAN_TAT) {
            orderEntity.getDetailOrders().stream()
                    .forEach(detail -> {
                        ProductEntity p = productService.findEntityById(detail.getProduct().getId());
                        p.setQuantity(p.getQuantity() - detail.getQuantity());
                        p.setSellCount(detail.getQuantity());
                        if (p.getQuantity() == 0) {
                            p.setStatus(0);
                        }
                        productRepository.save(p);
                    });
        }
        orderRepository.save(orderEntity);
    }

    @Override
    public List<OrderDTO> findAllByUserEmail(String email) {
        return orderRepository.findAllByUser_Email(email).stream().map(o ->
                mapper.map(o, OrderDTO.class)
        ).collect(Collectors.toList());
    }

    public OrderEntity findEntityById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<DetailOrderEntity> adminSaveDetail(OrderEntity order, List<DetailOrderDTO> detailOrders) {
        return detailOrders.stream()
                .map(d -> {
                    DetailOrderEntity detailOrder = new DetailOrderEntity();
                    detailOrder.setPrice(d.getPrice());
                    detailOrder.setQuantity(d.getQuantity());
                    detailOrder.setProduct(productService.findEntityById(d.getProduct().getId()));
                    detailOrder.setTotal(d.getTotal());
                    detailOrder.setOrder(order);
                    return detailOrderRepository.save(detailOrder);
                })
                .collect(Collectors.toList());
    }

    public List<DetailOrderEntity> saveDetail(OrderEntity order, HashMap<String, CartDTO> cart) {
        return cart.entrySet().stream()
                .map(c -> {
                    DetailOrderEntity detailOrder = new DetailOrderEntity();
                    detailOrder.setPrice(c.getValue().getProduct().getPrice());
                    detailOrder.setQuantity(c.getValue().getQuantity());
                    detailOrder.setProduct(productService.findEntityById(c.getKey()));
                    detailOrder.setTotal(c.getValue().getTotalPrice());
                    detailOrder.setOrder(order);
                    return detailOrderRepository.save(detailOrder);
                })
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findAllByIds(List<String> ids) {
        return orderRepository.findAllById(ids).stream()
                .map(o -> mapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void exportExcel(HttpServletResponse response, List<String> ids) throws IOException {
        workbook = new XSSFWorkbook();
        List<OrderDTO> orders = null;
        if(ids != null && !ids.isEmpty()) {
            orders = findAllByIds(ids);
        } else {
            orders = findAll();
        }
        writeHeaderLine();
        writeDataLines(orders);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Hóa đơn");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Mã đơn hàng", style);
        createCell(row, 1, "Khách hàng", style);
        createCell(row, 2, "Trạng thái đơn hàng", style);
        createCell(row, 3, "Tổng tiền", style);
        createCell(row, 4, "Ngày tạo hóa đơn", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if(value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if(value instanceof Timestamp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date dt = new Date(((Timestamp) value).getTime());
            cell.setCellValue(simpleDateFormat.format(dt));
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(List<OrderDTO> orders) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (OrderDTO order : orders) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, order.getId(), style);
            createCell(row, columnCount++, order.getUser().getName(), style);
            createCell(row, columnCount++, transferOrderStatus(order.getStatus()), style);
            createCell(row, columnCount++, order.getTotal(), style);
            createCell(row, columnCount++, order.getCreatedAt(), style);

        }
    }

    private String transferOrderStatus(int status) {
        String result = "";
        switch (status) {
            case AppConstants.CHO_XU_LY:
                result = "Chờ xử lý";
                break;
            case AppConstants.DA_TIEP_NHAN:
                result = "Đã tiếp nhận";
                break;
            case AppConstants.ORDER_SHIPPING:
                result = "Đang giao hàng";
                break;
            case AppConstants.DA_GIAO:
                result = "Đã giao hàng";
                break;
            case AppConstants.ORDER_DONE:
                result = "Hoàn thành";
                break;
            default:
                result = "Đã hủy";
                break;
        }
        return result;
    }
}
