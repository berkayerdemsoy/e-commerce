package com.example.shipment_service.controller;

import com.example.shipment_service.dto.ShipmentResponse;
import com.example.shipment_service.mapper.ShipmentMapper;
import com.example.shipment_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("/{id}")
    public ResponseEntity<Page<ShipmentResponse>> getShipmentByUserId(@PathVariable("id") Long id , Pageable pageable){
        return ResponseEntity.ok(shipmentService.getAllShipments(pageable,id));
    }

}
