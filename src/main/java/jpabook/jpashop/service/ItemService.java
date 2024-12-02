package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // 데이터 저장이므로 readOnly = false
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 준영속 엔티티 변경 - 변경 감지 방법
    @Transactional
    public void updateItem(Long id, String name, int price, int StockQuantity) {
        Item item = itemRepository.findOne(id); // id를 기반으로 실제 DB에 있는 영속 상태 엔티티를 찾아옴
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(StockQuantity);
        //.. 나머지 필드도 채우기

        // sava를 호출할 필요가 없음
        // 이유: 트랜잭션이 커밋되면서 JPA가 flush하기 때문(변경된 엔티티에 대해 업데이트 쿼리 날림)
//        itemRepository.save(item);

    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
