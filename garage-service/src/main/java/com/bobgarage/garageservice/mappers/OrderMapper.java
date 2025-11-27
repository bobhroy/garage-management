package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.InvoiceDto;
import com.bobgarage.garageservice.dtos.OrderDto;
import com.bobgarage.garageservice.entities.Customer;
import com.bobgarage.garageservice.entities.Invoice;
import com.bobgarage.garageservice.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "status", source = "order.status")
    @Mapping(target = "dateCreated", source = "order.dateCreated")
    @Mapping(target = "dateUpdated", source = "order.dateUpdated")
    @Mapping(target = "items", source = "order.items", qualifiedByName = "invoiceToDtoList")
    @Mapping(target = "totalPrice", expression = "java(order.getTotalPrice())")
    OrderDto toDto(Order order, Customer customer);

    @Named("invoiceToDtoList")
    default List<InvoiceDto> mapInvoices(Set<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) return List.of();
        return invoices.stream()
                .map(inv -> {
                    InvoiceDto dto = new InvoiceDto();
                    dto.setServiceTypeName(inv.getServiceTypeName());
                    dto.setPrice(inv.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
