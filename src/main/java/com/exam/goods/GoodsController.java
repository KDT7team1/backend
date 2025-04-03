package com.exam.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
            @RequestParam("category_id") Long category_id,
            @RequestParam("sub_category_id") Long sub_category_id,
            @RequestParam("goods_name") String goods_name,
            @RequestParam("goods_price") Long goods_price,
            @RequestParam("goods_description") String goods_description,
            @RequestParam("goods_stock") Long goods_stock) {


        String imagePath = null;

        // 이미지 저장 로직
        if (goods_image != null && !goods_image.isEmpty()) {
            String fileName = goods_image.getOriginalFilename();
            imagePath = "images/" + fileName;

            System.out.println("Received fileName: " + fileName);

            File uploadDir = new File("C:\\upload", fileName);

            try {
                goods_image.transferTo(uploadDir);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("파일 업로드 실패");
            }
        }

        GoodsDTO dto = GoodsDTO.builder()
                .category_id(category_id)
                .goods_name(goods_name)
                .sub_category_id(sub_category_id)
                .goods_price(goods_price)
                .goods_description(goods_description)
                .goods_stock(goods_stock)
                .goods_image("/" + imagePath)
                .build();

        System.out.println("GoodsDTO:" + dto);
        goodsService.save(dto);

        return ResponseEntity.ok("상품이 저장되었습니다.");
    }


    // 상품 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<String> GoodsUpdate( @PathVariable("id") Long id,
                                               @RequestBody Map<String, Object> body){
        Long newPrice = Long.parseLong(body.get("goods_price").toString());
        goodsService.updateGoodsPrice(id,newPrice);
        return ResponseEntity.ok("상품이 수정되었습니다.");
    }

    // 할인 적용 로직
    @PutMapping("/update/discount")
    public ResponseEntity<String> applyDiscount(
            @RequestParam("id")  Long id,
            @RequestParam("discount_rate") int discountRate,
            @RequestParam("period") int period){
        goodsService.applyDiscount(id, discountRate,period);
        return ResponseEntity.ok("할인 적용 완료");
    }

    // 할인 취소 로직
    @PutMapping("/update/cancel-discount")
    public ResponseEntity<String> cancelDiscount(
        @RequestParam("id") Long id
    ){
        goodsService.cancelDiscount(id);
        return ResponseEntity.ok("할인 취소 완료");
    }

    // 연관상품 추천 로직
    @GetMapping("/recommendations")
    public ResponseEntity<List<Goods>> getRecommendedGoods(@RequestParam String subName) {
        List<Goods> list = goodsService.getRecommendedGoods(subName);
        return ResponseEntity.ok(list);
    }


}

