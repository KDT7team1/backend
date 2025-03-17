//package com.exam;
//
//import com.exam.entity.Goods;
//import com.exam.entity.Member;
//import com.exam.entity.Orders;
//import com.exam.entity.SaleData;
//import com.exam.repository.GoodsRepository;
//import com.exam.repository.MemberRepository;
//import com.exam.repository.OrdersRepository;
//import com.exam.repository.SaleDataRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//
//@Component
//public class AssociationDummyDataInserter implements CommandLineRunner {
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
//    private static final int NUM_ORDERS = 100000; // 주문 데이터 개수 (랜덤+연관 데이터 포함)
//    private static final int NUM_SALE_DATA = 500000; // 판매 데이터 개수
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//
//        Random random = new Random();
//        LocalDateTime currentDateTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
//        int saleDataCnt = 0;
//
//        // ✅ 상품 카테고리별 목록 가져오기
//        Map<String, List<Goods>> categoryMap = new HashMap<>();
//        categoryMap.put("삼각김밥", goodsRepo.findBySubCategoryId(1L));
//        categoryMap.put("도시락", goodsRepo.findBySubCategoryId(4L)); //
//        categoryMap.put("햄버거", goodsRepo.findBySubCategoryId(6L));
//        categoryMap.put("컵라면", goodsRepo.findBySubCategoryId(7L));
//        categoryMap.put("샌드위치", goodsRepo.findBySubCategoryId(11L));
//        categoryMap.put("도넛", goodsRepo.findBySubCategoryId(13L));
//        categoryMap.put("베이글", goodsRepo.findBySubCategoryId(14L));
//        categoryMap.put("핫바", goodsRepo.findBySubCategoryId(18L));
//
//        categoryMap.put("감자칩", goodsRepo.findBySubCategoryId(20L));
//        categoryMap.put("팝콘", goodsRepo.findBySubCategoryId(21L));
//        categoryMap.put("젤리", goodsRepo.findBySubCategoryId(24L));
//        categoryMap.put("초콜릿", goodsRepo.findBySubCategoryId(25L));
//        categoryMap.put("아이스크림", goodsRepo.findBySubCategoryId(26L));
//        categoryMap.put("요거트", goodsRepo.findBySubCategoryId(28L));
//        categoryMap.put("커피", goodsRepo.findBySubCategoryId(29L));
//        categoryMap.put("콜라", goodsRepo.findBySubCategoryId(33L));
//        categoryMap.put("에너지드링크", goodsRepo.findBySubCategoryId(35L));
//        categoryMap.put("오렌지주스", goodsRepo.findBySubCategoryId(36L));
//        categoryMap.put("비타민음료", goodsRepo.findBySubCategoryId(37L));
//        categoryMap.put("맥주", goodsRepo.findBySubCategoryId(41L));
//        categoryMap.put("소주", goodsRepo.findBySubCategoryId(42L));
//        categoryMap.put("칫솔", goodsRepo.findBySubCategoryId(50L));
//        categoryMap.put("면도기", goodsRepo.findBySubCategoryId(52L));
//
//
//        List<Goods> allGoods = goodsRepo.findAll();
//
//        // ✅ 시간대별 연관 상품 조합 (확률적 조정 가능)
//        Map<String, List<String[]>> timePairs = new HashMap<>();
//        timePairs.put("아침", Arrays.asList(new String[]{"삼각김밥", "커피"}, new String[]{"샌드위치", "콜라"}, new String[]{"베이글", "에너지드링크"}));
//        timePairs.put("점심", Arrays.asList(new String[]{"도시락", "에너지드링크"}, new String[]{"삼각김밥", "컵라면"}, new String[]{"핫바", "오렌지주스"}));
//        timePairs.put("한산한 오후", Arrays.asList(new String[]{"초콜릿", "커피"}, new String[]{"감자칩", "젤리"}, new String[]{"아이스크림", "칫솔"}));
//        timePairs.put("저녁", Arrays.asList(new String[]{"맥주", "팝콘"}, new String[]{"햄버거", "콜라"}, new String[]{"칫솔", "면도기"}));
//        timePairs.put("심야", Arrays.asList(new String[]{"소주", "비타민음료"}, new String[]{"닭강정", "아이스크림"},  new String[]{"도넛", "요거트"}));
//
//
//
//        // 🔥 주문 데이터 생성 (랜덤 + 연관 데이터)
//        for (int i = 0; i < NUM_ORDERS; i++) {
//            if (saleDataCnt >= NUM_SALE_DATA) break;
//
//            // 🔹 랜덤 시간 간격 적용
//            List<Integer> timeIntervals = getTimeIntervalForTime(currentDateTime.toLocalTime());
//            int randomNum = timeIntervals.get(random.nextInt(timeIntervals.size()));
//            currentDateTime = currentDateTime.plusMinutes(randomNum);
//
//            // 🔹 주문 생성
//            Orders order = new Orders();
//            Member member = memberRepo.findById(1L + random.nextInt(100))
//                    .orElseThrow(() -> new RuntimeException("멤버 데이터를 먼저 삽입하세요"));
//            order.setMember(member);
//            order.setOrdersDate(currentDateTime);
//
//            Orders savedOrder = ordersRepo.save(order);
//
//            // 🔥 60% 확률로 시간대별 연관 데이터 추가
//            if (random.nextDouble() < 0.5) {
//                String timeText = getTimeSlot(currentDateTime.toLocalTime());
//                if(timeText.equals("기본")){ // 기본이 나오면 다른 랜덤 시간대로 보내기
//                    List<String> keys = new ArrayList<>(timePairs.keySet());
//                    timeText = keys.get(random.nextInt(keys.size()));
//                }
//                List<String[]> pairs = timePairs.get(timeText);
//
//                if (pairs != null) {
//                    for (int k = 0; k < 2; k++) { // 같은 시간대에서 2개 조합 추가
//                        String[] selectedPair = pairs.get(random.nextInt(pairs.size()));
//
//                        List<Goods> itemA = categoryMap.get(selectedPair[0]);
//                        List<Goods> itemB = categoryMap.get(selectedPair[1]);
//
//                        if (itemA != null && !itemA.isEmpty() && itemB != null && !itemB.isEmpty()) {
//                            createSaleData(savedOrder, itemA.get(random.nextInt(itemA.size())), currentDateTime, random);
//                            createSaleData(savedOrder, itemB.get(random.nextInt(itemB.size())), currentDateTime, random);
//                        }
//                    }
//                }
//            }
//
//            // 🔥 40% 확률로 랜덤 상품 1~3개 추가
//            if (random.nextDouble() < 0.5) {
//                int numRandomGoods = 1 + random.nextInt(3);
//                for (int j = 0; j < numRandomGoods; j++) {
//                    Goods randomGoods = allGoods.get(random.nextInt(allGoods.size()));
//                    createSaleData(savedOrder, randomGoods, currentDateTime, random);
//                }
//            }
//
//        }
//
//        System.out.println("🔥 랜덤 + 연관 데이터 삽입 성공!!");
//    }
//
//    private List<Integer> getTimeIntervalForTime(LocalTime time) {
//        if (time.isAfter(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(8, 0))) {
//            return Arrays.asList(8, 10, 12, 15); // 출근 시간
//        } else if (time.isAfter(LocalTime.of(11, 30)) && time.isBefore(LocalTime.of(13, 30))) {
//            return Arrays.asList(8, 10, 12, 15); // 점심 피크
//        } else if (time.isAfter(LocalTime.of(15, 0)) && time.isBefore(LocalTime.of(17, 0))) {
//            return Arrays.asList(12, 15, 18); // 오후 한산한 시간
//        } else if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(22, 0))) {
//            return Arrays.asList(8, 10, 12, 15); // 퇴근+저녁 시간
//        } else if (time.isAfter(LocalTime.of(0, 0)) && time.isBefore(LocalTime.of(5, 0))) {
//            return Arrays.asList(15, 20, 25, 30); // 심야 시간
//        } else {
//            return Arrays.asList(10, 15, 20); // 기본 간격
//        }
//    }
//
//    private String getTimeSlot(LocalTime time) {
//        if (time.isAfter(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(8, 0))) return "아침";
//        if (time.isAfter(LocalTime.of(11, 30)) && time.isBefore(LocalTime.of(13, 30))) return "점심";
//        if (time.isAfter(LocalTime.of(15, 0)) && time.isBefore(LocalTime.of(17, 0))) return "한산한 오후";
//        if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(22, 0))) return "저녁";
//        if (time.isAfter(LocalTime.of(0, 0)) && time.isBefore(LocalTime.of(5, 0))) return "심야";
//        return "기본";
//    }
//
//    private SaleData createSaleData(Orders order, Goods goods, LocalDateTime dateTime, Random random) {
//        SaleData saleData = new SaleData();
//       saleData.setOrders(order);
//       saleData.setGoods(goods);
//       saleData.setSaleAmount(1+ random.nextLong(3)); // 1개에서 3개 랜덤으로
//        saleData.setSalePrice(goods.getGoods_price());
//        saleData.setSaleDate(dateTime);
//
//        saleDataRepo.save(saleData);
//        return saleData;
//    }
//}
