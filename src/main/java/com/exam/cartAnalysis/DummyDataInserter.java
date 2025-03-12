//package com.exam.cartAnalysis;
//
//import com.exam.cartAnalysis.entity.Orders;
//import com.exam.cartAnalysis.entity.SaleData;
//import com.exam.cartAnalysis.repository.OrdersRepository;
//import com.exam.cartAnalysis.repository.SaleDataRepository;
//import com.exam.userGoods.entity.Goods;
//import com.exam.member.Member;
//import com.exam.member.MemberRepository;
//
//import com.exam.userGoods.repository.GoodsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class DummyDataInserter implements CommandLineRunner {
//
//    @Autowired
//    private OrdersRepository ordersRepo;
//
//    @Autowired
//    private SaleDataRepository saleDataRepo;
//
//    @Autowired
//    private GoodsRepository goodsRepo;
//
//    @Autowired
//    private MemberRepository memberRepo;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Random random = new Random();
//
//        // 1. 특정 카테고리 상품 목록 가져오기 (주류, 과자, 라면, 삼각김밥)
//        List<Goods> beerList = goodsRepo.findBySubCategoryId(22L);   // 맥주
//        System.out.println("🔥 맥주 리스트 개수: " + beerList.size());
//        for (Goods beer : beerList) {
//            System.out.println("상품명: " + beer.getGoods_name() + " | ID: " + beer.getGoods_id());
//        }
//        List<Goods> sojuList = goodsRepo.findBySubCategoryId(23L);   // 소주
//        List<Goods> snackList = goodsRepo.findBySubCategoryId(11L);  // 감자칩
//        List<Goods> ramenList = goodsRepo.findBySubCategoryId(7L);   // 컵라면
//        List<Goods> riceList = goodsRepo.findBySubCategoryId(1L);    // 삼각김밥
//        List<Goods> drinkList = goodsRepo.findBySubCategoryId(17L);  // 코카콜라
//
//        for(int i=0;i<100;i++){
//            // 1) orders (헤더 데이터) 생성하기
//            Orders order = new Orders();
//            Member member = memberRepo.findById(1L + random.nextInt(5)).orElseThrow(()-> new RuntimeException("멤버 데이터를 먼저 삽입하세요")); // 회원ID(1~5)
//            order.setMember(member);
//            order.setOrders_status("COMPLETE");
//            order.setPaymentMethod("CARD");
//            Orders savedOrder = ordersRepo.save(order);
//            Long newOrderId =  savedOrder.getOrdersId(); // 만들어진 주문헤더 ID
//
//            // 2) 확률 로직
//            double p = random.nextDouble();
//            if(p <0.2){
//                // 20%확률로 "맥주+과자" 묶음
//                if(!beerList.isEmpty() && !snackList.isEmpty()){
//                    Goods beer = beerList.get(random.nextInt(beerList.size())); // 비어 종류중 랜덤으로 하나 뽑음
//                    Goods snack = snackList.get(random.nextInt(snackList.size())); //  마찬가리로 스낵도 랜덤으로 뽑기
//                    insertSaleData(newOrderId, beer.getGoods_id(), 1L, beer.getGoods_price());
//                    insertSaleData(newOrderId, snack.getGoods_id(), 1L, snack.getGoods_price());
//                }
//            }else if(p <0.4){
//                if (!sojuList.isEmpty() && !riceList.isEmpty()) {
//                    Goods soju = sojuList.get(random.nextInt(sojuList.size()));
//                    Goods rice = riceList.get(random.nextInt(riceList.size()));
//                    insertSaleData(newOrderId, soju.getGoods_id(), 1L, soju.getGoods_price());
//                    insertSaleData(newOrderId, rice.getGoods_id(), 1L, rice.getGoods_price());
//                }
//            }else if (p < 0.6) {
//                // 20% 확률로 "컵라면 + 코카콜라" 조합
//                if (!ramenList.isEmpty() && !drinkList.isEmpty()) {
//                    Goods ramen = ramenList.get(random.nextInt(ramenList.size()));
//                    Goods drink = drinkList.get(random.nextInt(drinkList.size()));
//                    insertSaleData(newOrderId, ramen.getGoods_id(), 1L, ramen.getGoods_price());
//                    insertSaleData(newOrderId, drink.getGoods_id(), 1L, drink.getGoods_price());
//                }
//            }
//            else{
//                // 나머지 40%는 랜덤 상품 1~5개 추가
//                int itemCount = 2+random.nextInt(4);
//                List<Goods> allGoods = goodsRepo.findAll();
//                for(int j = 0;j<itemCount;j++){
//                    Goods randomGoods = allGoods.get(random.nextInt(allGoods.size()));
//                    insertSaleData(newOrderId, randomGoods.getGoods_id(), 1L, randomGoods.getGoods_price());
//                }
//            }
//
//        }
//        System.out.println("더미 데이터 삽입 성공!!");
//
//    }
//
//    private void insertSaleData(Long ordersId, Long goodsId, Long saleAmount, Long salePrice) {
//        SaleData saleData = new SaleData();
//        Orders orders = ordersRepo.findById(ordersId).orElse(null);
//        saleData.setOrders(orders);
//        Goods goods = goodsRepo.findById(goodsId).orElse(null);
//        saleData.setGoods(goods);
//        saleData.setSaleAmount(saleAmount);
//        saleData.setSalePrice(salePrice);
//
//        saleDataRepo.save(saleData);
//    }
//}
