package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

	@Autowired
	ItemRepository itemRepository;

	@Test
	public void save()
	{
		Item item = new Item("A");
		itemRepository.save(item);
	}
}