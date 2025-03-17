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
    @PostMapping("/save2")
    public ResponseEntity<String> save2( String theText,@RequestBody MultipartFile theFile) {

        System.out.println("Received goods_id: " + theText);
        System.out.println("Received goods_image: " + theFile);
//        System.out.println("Received category_id: " + dto.getCategory_id());
//
//        if (dto.getCategory_id() == null) {
//            return ResponseEntity.badRequest().body("category_id는 필수 값입니다.");
//        }
//
//        goodsService.save(dto);
        return  ResponseEntity.ok("상품이 저장되었습니다.");
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
             MultipartFile goods_image,
             Long goods_id,
             Long category_id,
             String goods_name,
             Long goods_price,
             String goods_description,
            Long goods_stock) {

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

            File uploadDir = new File("c:\\upload", fileName); // ✅ 이미지 저장 폴더
            if (!uploadDir.exists()) uploadDir.mkdirs();

            File dest = new File(uploadDir, fileName);
            try {
                goods_image.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("파일 업로드 실패");
            }
        }

        // DTO에 이미지 경로 저장
        GoodsDTO dto2 = new GoodsDTO(goods_id,
                category_id,
                goods_name, goods_price, goods_description, goods_stock, "/" + imagePath);

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






    // 상품 조회 API
//    @GetMapping("/{goodsId}")
//    public ResponseEntity<GoodsDTO> getGoods(@PathVariable Long goodsId) {
//        GoodsDTO dto = goodsService.findById(goodsId);
//        return ResponseEntity.ok(dto);
//    }

    // 3. 상품 재고 수정하기
    @PutMapping("/updateStock/{goodsId}")
    public ResponseEntity<String> updateStock(@PathVariable Long goodsId, @RequestParam("newStock") Long newStock) {
        goodsService.updateGoodsStock(goodsId, newStock);
        return ResponseEntity.ok("재고 업데이트 완료");
    }



}
