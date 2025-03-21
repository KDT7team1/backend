package com.exam.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    // 1. 상품 상세보기
    @GetMapping("/findById/{id}")
    public ResponseEntity<GoodsDTO> findById(@PathVariable Long id){
        System.out.println(id);
        GoodsDTO dto =  goodsService.findById(id);

        if(dto == null){
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(200).body(dto);
    }


    // 상품 저장 API
    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestParam(value="goods_image", required=false) MultipartFile goods_image, // 수정!
            @RequestParam("goods_id") Long goods_id,
            @RequestParam("category_id") Long category_id,
            @RequestParam("goods_name") String goods_name,
            @RequestParam("goods_price") Long goods_price,
            @RequestParam("goods_description") String goods_description,
            @RequestParam("goods_stock") Long goods_stock) {

        System.out.println("Received goods_image1: " + goods_image);
        System.out.println("Received goods_id1: " + goods_id);
        System.out.println("Received category_id1: " + category_id);
        System.out.println("Received goods_name1: " + goods_name);
        System.out.println("Received goods_price1: " + goods_price);
        System.out.println("Received goods_description1: " + goods_description);
        System.out.println("Received goods_stock1: " + goods_stock);

        String imagePath = null;

        // 이미지 저장 로직
        if (goods_image != null && !goods_image.isEmpty()) {
            String fileName = goods_image.getOriginalFilename();
            imagePath = "images/" + fileName;

            System.out.println("Received fileName: " + fileName);

            File uploadDir = new File("C:\\Users\\01min\\Desktop\\upload", fileName);

            try {
                goods_image.transferTo(uploadDir);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("파일 업로드 실패");
            }
        }

        // DTO에 이미지 경로 저장
//        GoodsDTO dto2 = new GoodsDTO(goods_id,
//                category_id,
//                goods_name, goods_price, goods_description, goods_stock, "/" + imagePath);

        GoodsDTO dto = GoodsDTO.builder()
                .goods_id(goods_id)
                .category_id(category_id)
                .goods_name(goods_name)
                .goods_price(goods_price)
                .goods_description(goods_description)
                .goods_stock(goods_stock)
                .goods_image("/" + imagePath)
                .build();

        System.out.println("GoodsDTO:" + dto);
        goodsService.save(dto);

        return ResponseEntity.ok("상품이 저장되었습니다.");
    }
}
