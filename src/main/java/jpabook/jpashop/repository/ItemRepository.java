package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if(item.getId() == null) { // 완전히 처음 등록하는 경우
            em.persist(item);
        } else {
            // 상품 수정한 경우
            em.merge(item); // update와 유사
        }

        // item은 이후 영속성 컨텍스트에서 관리되지 않음
        // 이후 사용하려면 em.merge() 후 반환된 객체를 써야함
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
